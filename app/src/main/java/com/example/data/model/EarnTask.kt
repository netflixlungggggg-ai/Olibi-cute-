package com.example.data.model

enum class TaskCategory {
    DAILY_CHECKIN,
    SPONSORED_VIDEO,
    DJ_SPECIAL,
    SOCIAL_FOLLOW,
    SPIN_WHEEL,
    REFERRAL
}

data class EarnTask(
    val id: String,
    val title: String,
    val description: String,
    val rewardCoins: Int,
    val category: TaskCategory,
    val isCompleted: Boolean = false,
    val iconName: String = "monetization_on",
    val durationSeconds: Int = 0,
    val actionUrl: String = ""
)
