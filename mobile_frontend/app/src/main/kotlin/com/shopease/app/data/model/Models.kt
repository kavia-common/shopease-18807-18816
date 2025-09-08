package com.shopease.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val rating: Float = 4.3f,
    val inStock: Boolean = true
)

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val token: String? = null
)

@JsonClass(generateAdapter = true)
data class CartItem(
    val product: Product,
    val quantity: Int
)

@JsonClass(generateAdapter = true)
data class Order(
    val id: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val placedAt: Long
)

enum class OrderStatus { PLACED, CONFIRMED, SHIPPED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED }
