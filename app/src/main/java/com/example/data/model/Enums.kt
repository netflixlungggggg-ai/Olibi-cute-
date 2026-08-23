package com.example.data.model

enum class SocialPlatform(
    val displayName: String,
    val brandColorHex: Long,
    val iconName: String
) {
    INSTAGRAM("Instagram", 0xFFE1306C, "instagram"),
    YOUTUBE("YouTube", 0xFFFF0000, "youtube"),
    TIKTOK("TikTok", 0xFF00F2FE, "tiktok"),
    FACEBOOK("Facebook", 0xFF1877F2, "facebook"),
    X_TWITTER("X / Twitter", 0xFF1DA1F2, "twitter"),
    SPOTIFY("Spotify", 0xFF1DB954, "spotify")
}

enum class ServiceType(
    val displayName: String,
    val costPerUnit: Int,
    val unitName: String,
    val defaultQty: Int,
    val minQty: Int,
    val maxQty: Int
) {
    LIKES("Boost Likes", 2, "Likes", 100, 20, 5000),
    FOLLOWERS("Boost Followers", 5, "Followers", 50, 10, 2000),
    VIEWS("Boost Views", 1, "Views", 200, 50, 10000),
    COMMENTS("Boost Comments", 10, "Comments", 20, 5, 500),
    SUBSCRIBERS("Boost Subscribers", 8, "Subscribers", 25, 10, 1000)
}

enum class BoostSpeed(val displayName: String, val multiplier: Float, val badgeText: String) {
    STANDARD("Standard (1x)", 1.0f, "Normal"),
    FAST("Fast Delivery (1.5x)", 1.2f, "High Speed"),
    TURBO_DJ("DJ Ambani Turbo (3x)", 1.5f, "⚡ Ultra Fast")
}

enum class OrderStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    CANCELLED
}
