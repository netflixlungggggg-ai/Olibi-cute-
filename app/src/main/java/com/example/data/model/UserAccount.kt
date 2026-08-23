package com.example.data.model

data class UserAccount(
    val uid: String = "user_dj_vip_1",
    val displayName: String = "Alex Booster",
    val email: String = "alex.booster@example.com",
    val avatarUrl: String = "",
    val coins: Int = 1250,
    val isVip: Boolean = true,
    val vipTier: String = "DJ Ambani VIP Gold",
    val checkInStreak: Int = 3,
    val lastCheckInDate: String = "",
    val totalCoinsEarned: Int = 3450,
    val totalBoostsCompleted: Int = 14,
    val referralCode: String = "OLIBI-AMBANI-777",
    val friendsReferred: Int = 4
)
