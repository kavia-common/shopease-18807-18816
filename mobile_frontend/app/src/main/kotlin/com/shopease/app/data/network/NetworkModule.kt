package com.shopease.app.data.network

import android.util.Log
import com.shopease.app.data.model.*
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.random.Random

/**
 * PUBLIC_INTERFACE
 * NetworkModule
 *
 * Creates Retrofit ApiService. Defaults to MockApiService for now.
 * Replace provideApiService() with real Retrofit when backend is available.
 */
object NetworkModule {

    // PUBLIC_INTERFACE
    fun provideApiService(useMock: Boolean = true): ApiService {
        return if (useMock) MockApiService() else createRetrofitService()
    }

    private fun createRetrofitService(): ApiService {
        val logging = HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Replace via .env or BuildConfig at integration time
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

/**
 * MockApiService returns placeholder data with simulated delays.
 */
class MockApiService : ApiService {

    private val images = listOf(
        "https://picsum.photos/seed/1/600/600",
        "https://picsum.photos/seed/2/600/600",
        "https://picsum.photos/seed/3/600/600",
        "https://picsum.photos/seed/4/600/600",
        "https://picsum.photos/seed/5/600/600"
    )

    private fun sampleProducts(): List<Product> {
        return (1..20).map {
            Product(
                id = it.toString(),
                title = "Sample Product $it",
                description = "High quality product number $it with great features and value.",
                price = Random.nextDouble(10.0, 499.0),
                imageUrl = images.random(),
                rating = Random.nextDouble(3.5, 5.0).toFloat(),
                inStock = it % 5 != 0
            )
        }
    }

    override suspend fun getProducts(query: String?): List<Product> {
        delay(400)
        val all = sampleProducts()
        return if (query.isNullOrBlank()) all else all.filter { it.title.contains(query, true) }
    }

    override suspend fun getProductDetail(id: String): Product {
        delay(250)
        return sampleProducts().first { it.id == id }
    }

    override suspend fun login(request: Map<String, String>): User {
        delay(350)
        val email = request["email"].orEmpty()
        return User(
            id = "u1",
            name = email.substringBefore("@").ifBlank { "User" }.replaceFirstChar { it.uppercase() },
            email = email,
            token = "mock-token-123"
        )
    }

    override suspend fun signup(request: Map<String, String>): User {
        delay(450)
        val email = request["email"].orEmpty()
        val name = request["name"].orEmpty().ifBlank { "New User" }
        return User(
            id = "u2",
            name = name,
            email = email,
            token = "mock-token-456"
        )
    }

    override suspend fun getOrders(bearer: String?): List<Order> {
        delay(300)
        val items = listOf(
            CartItem(product = sampleProducts().first(), quantity = 1),
            CartItem(product = sampleProducts()[1], quantity = 2)
        )
        return listOf(
            Order(
                id = "o1",
                items = items,
                totalAmount = items.sumOf { it.product.price * it.quantity },
                status = OrderStatus.OUT_FOR_DELIVERY,
                placedAt = System.currentTimeMillis() - 86_400_000L
            ),
            Order(
                id = "o2",
                items = listOf(items.first()),
                totalAmount = items.first().product.price,
                status = OrderStatus.DELIVERED,
                placedAt = System.currentTimeMillis() - 3 * 86_400_000L
            )
        )
    }

    override suspend fun placeOrder(bearer: String?, items: List<CartItem>): Order {
        delay(400)
        return Order(
            id = "o${Random.nextInt(100, 999)}",
            items = items,
            totalAmount = items.sumOf { it.product.price * it.quantity },
            status = OrderStatus.PLACED,
            placedAt = System.currentTimeMillis()
        )
    }
}
