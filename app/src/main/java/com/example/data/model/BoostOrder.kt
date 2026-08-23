package com.example.data.model

data class BoostOrder(
    val id: String,
    val platform: SocialPlatform,
    val serviceType: ServiceType,
    val targetUrl: String,
    val quantity: Int,
    val coinCost: Int,
    val deliveredCount: Int = 0,
    val status: OrderStatus = OrderStatus.PROCESSING,
    val speed: BoostSpeed = BoostSpeed.STANDARD,
    val createdAt: Long = System.currentTimeMillis()
) {
    val progress: Float
        get() = if (quantity > 0) (deliveredCount.toFloat() / quantity).coerceIn(0f, 1f) else 0f
}
