package com.shopease.app.ui.common

import androidx.lifecycle.*
import com.shopease.app.data.model.*
import com.shopease.app.data.repository.*
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * HomeViewModel loads deals and product list.
 */
class HomeViewModel(private val repo: ProductRepository) : ViewModel() {
    val loading = MutableLiveData(false)
    val products = MutableLiveData<List<Product>>(emptyList())
    val error = MutableLiveData<String?>()

    // PUBLIC_INTERFACE
    fun load() {
        viewModelScope.launch {
            try {
                loading.value = true
                products.value = repo.getProducts(null)
            } catch (t: Throwable) {
                error.value = t.message
            } finally {
                loading.value = false
            }
        }
    }
}

/**
 * PUBLIC_INTERFACE
 * SearchViewModel handles query and results.
 */
class SearchViewModel(private val repo: ProductRepository) : ViewModel() {
    val query = MutableLiveData("")
    val results = MutableLiveData<List<Product>>(emptyList())
    val loading = MutableLiveData(false)

    // PUBLIC_INTERFACE
    fun search(text: String) {
        query.value = text
        viewModelScope.launch {
            loading.value = true
            results.value = repo.getProducts(text)
            loading.value = false
        }
    }
}

/**
 * PUBLIC_INTERFACE
 * CartViewModel exposes cart operations.
 */
class CartViewModel(private val cartRepo: CartRepository) : ViewModel() {
    val items: LiveData<List<CartItem>> = cartRepo.cartItems().asLiveData()

    // PUBLIC_INTERFACE
    fun add(p: Product) = cartRepo.add(p)

    // PUBLIC_INTERFACE
    fun remove(productId: String) = cartRepo.remove(productId)

    // PUBLIC_INTERFACE
    fun clear() = cartRepo.clear()

    // PUBLIC_INTERFACE
    fun totalAmount(): Double = items.value.orEmpty().sumOf { it.product.price * it.quantity }
}

/**
 * PUBLIC_INTERFACE
 * OrdersViewModel fetches user orders.
 */
class OrdersViewModel(private val repo: OrderRepository) : ViewModel() {
    val orders = MutableLiveData<List<Order>>(emptyList())
    val loading = MutableLiveData(false)

    // PUBLIC_INTERFACE
    fun load() {
        viewModelScope.launch {
            loading.value = true
            orders.value = repo.orders()
            loading.value = false
        }
    }
}

/**
 * PUBLIC_INTERFACE
 * ProductDetailViewModel loads detail and adds to cart.
 */
class ProductDetailViewModel(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository
) : ViewModel() {
    val detail = MutableLiveData<Product?>()
    val loading = MutableLiveData(false)

    // PUBLIC_INTERFACE
    fun load(id: String) {
        viewModelScope.launch {
            loading.value = true
            detail.value = productRepo.getProductDetail(id)
            loading.value = false
        }
    }

    // PUBLIC_INTERFACE
    fun addToCart() {
        detail.value?.let { cartRepo.add(it) }
    }
}

/**
 * PUBLIC_INTERFACE
 * AuthViewModel manages login/signup/logout flows.
 */
class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
    val token: LiveData<String?> = repo.token().asLiveData()

    // PUBLIC_INTERFACE
    fun login(email: String, password: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            try {
                repo.login(email, password)
                onResult(null)
            } catch (t: Throwable) {
                onResult(t)
            }
        }
    }

    // PUBLIC_INTERFACE
    fun signup(name: String, email: String, password: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            try {
                repo.signup(name, email, password)
                onResult(null)
            } catch (t: Throwable) {
                onResult(t)
            }
        }
    }

    // PUBLIC_INTERFACE
    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.logout()
            onDone()
        }
    }
}

/**
 * Factory helpers to create ViewModels with ServiceLocator dependencies.
 */
@Suppress("UNCHECKED_CAST")
class VMFactory<T>(private val creator: () -> T) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}

object VMProviders {
    // PUBLIC_INTERFACE
    fun home(repo: ProductRepository) = VMFactory { HomeViewModel(repo) }

    // PUBLIC_INTERFACE
    fun search(repo: ProductRepository) = VMFactory { SearchViewModel(repo) }

    // PUBLIC_INTERFACE
    fun cart(cartRepo: CartRepository) = VMFactory { CartViewModel(cartRepo) }

    // PUBLIC_INTERFACE
    fun orders(repo: OrderRepository) = VMFactory { OrdersViewModel(repo) }

    // PUBLIC_INTERFACE
    fun productDetail(productRepo: ProductRepository, cartRepo: CartRepository) =
        VMFactory { ProductDetailViewModel(productRepo, cartRepo) }

    // PUBLIC_INTERFACE
    fun auth(authRepo: AuthRepository) = VMFactory { AuthViewModel(authRepo) }
}
