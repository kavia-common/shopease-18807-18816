package com.shopease.app.data.network

import com.shopease.app.data.model.*
import retrofit2.http.*

/**
 * PUBLIC_INTERFACE
 * ApiService
 *
 * Retrofit service definition for backend REST APIs.
 * NOTE: In this project, we will wire a MockApiService to return placeholder data.
 */
interface ApiService {

    // PUBLIC_INTERFACE
    @GET("products")
    suspend fun getProducts(@Query("q") query: String? = null): List<Product>

    // PUBLIC_INTERFACE
    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: String): Product

    // PUBLIC_INTERFACE
    @POST("auth/login")
    suspend fun login(@Body request: Map<String, String>): User

    // PUBLIC_INTERFACE
    @POST("auth/signup")
    suspend fun signup(@Body request: Map<String, String>): User

    // PUBLIC_INTERFACE
    @GET("orders")
    suspend fun getOrders(@Header("Authorization") bearer: String?): List<Order>

    // PUBLIC_INTERFACE
    @POST("orders")
    suspend fun placeOrder(
        @Header("Authorization") bearer: String?,
        @Body items: List<CartItem>
    ): Order
}
