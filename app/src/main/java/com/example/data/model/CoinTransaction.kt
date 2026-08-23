package com.example.data.model

data class CoinTransaction(
    val id: String,
    val title: String,
    val description: String,
    val amount: Int,
    val isEarned: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String = "Bonus"
)
