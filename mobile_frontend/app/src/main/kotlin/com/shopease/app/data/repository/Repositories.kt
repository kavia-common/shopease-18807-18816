package com.shopease.app.data.repository

import android.content.Context
import com.shopease.app.data.local.AuthStorage
import com.shopease.app.data.model.*
import com.shopease.app.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * PUBLIC_INTERFACE
 * AuthRepository handles login/signup and token storage.
 */
class AuthRepository(
    private val api: ApiService,
    private val storage: AuthStorage
) {
    // PUBLIC_INTERFACE
    suspend fun login(email: String, password: String): User {
        val user = api.login(mapOf("email" to email, "password" to password))
        storage.setSession(user.name, user.email, user.token)
        return user
    }

    // PUBLIC_INTERFACE
    suspend fun signup(name: String, email: String, password: String): User {
        val user = api.signup(mapOf("name" to name, "email" to email, "password" to password))
        storage.setSession(user.name, user.email, user.token)
        return user
    }

    // PUBLIC_INTERFACE
    fun token(): Flow<String?> = storage.token()

    // PUBLIC_INTERFACE
    suspend fun logout() = storage.clear()
}

/**
 * PUBLIC_INTERFACE
 * ProductRepository fetches product lists and product details.
 */
class ProductRepository(private val api: ApiService) {
    // PUBLIC_INTERFACE
    suspend fun getProducts(query: String? = null): List<Product> = api.getProducts(query)

    // PUBLIC_INTERFACE
    suspend fun getProductDetail(id: String): Product = api.getProductDetail(id)
}

/**
 * PUBLIC_INTERFACE
 * CartRepository manages a simple in-memory cart store.
 */
class CartRepository {
    private val cart = MutableStateFlow<List<CartItem>>(emptyList())

    // PUBLIC_INTERFACE
    fun cartItems(): Flow<List<CartItem>> = cart

    // PUBLIC_INTERFACE
    fun add(product: Product) {
        val current = cart.value.toMutableList()
        val idx = current.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) {
            val existing = current[idx]
            current[idx] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        cart.value = current
    }

    // PUBLIC_INTERFACE
    fun remove(productId: String) {
        val current = cart.value.toMutableList()
        val idx = current.indexOfFirst { it.product.id == productId }
        if (idx >= 0) {
            val existing = current[idx]
            if (existing.quantity > 1) current[idx] = existing.copy(quantity = existing.quantity - 1)
            else current.removeAt(idx)
        }
        cart.value = current
    }

    // PUBLIC_INTERFACE
    fun clear() { cart.value = emptyList() }
}

/**
 * PUBLIC_INTERFACE
 * OrderRepository places and retrieves orders via API.
 */
class OrderRepository(
    private val api: ApiService,
    private val authRepository: AuthRepository
) {
    // PUBLIC_INTERFACE
    suspend fun placeOrder(items: List<CartItem>): Order {
        val token = authRepository.token().first()
        return api.placeOrder(bearer = token?.let { "Bearer $it" }, items = items)
    }

    // PUBLIC_INTERFACE
    suspend fun orders(): List<Order> {
        val token = authRepository.token().first()
        return api.getOrders(token?.let { "Bearer $it" })
    }
}

/**
 * PUBLIC_INTERFACE
 * ServiceLocator provides simple manual DI across the app.
 */
object ServiceLocator {
    private var initialized = false

    lateinit var api: ApiService
    lateinit var authStorage: AuthStorage
    lateinit var authRepository: AuthRepository
    lateinit var productRepository: ProductRepository
    lateinit var cartRepository: CartRepository
    lateinit var orderRepository: OrderRepository

    // PUBLIC_INTERFACE
    fun init(context: Context) {
        if (initialized) return
        api = com.shopease.app.data.network.NetworkModule.provideApiService(useMock = true)
        authStorage = AuthStorage(context)
        authRepository = AuthRepository(api, authStorage)
        productRepository = ProductRepository(api)
        cartRepository = CartRepository()
        orderRepository = OrderRepository(api, authRepository)
        initialized = true
    }
}
