package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object BoosterRepository {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _userAccount = MutableStateFlow(
        UserAccount(
            uid = "ambani_booster_user_01",
            displayName = "Alex Carter",
            email = "alex.booster@olibi.app",
            avatarUrl = "",
            coins = 1500,
            isVip = true,
            vipTier = "DJ Ambani VIP Gold",
            checkInStreak = 3,
            lastCheckInDate = "",
            totalCoinsEarned = 4200,
            totalBoostsCompleted = 18,
            referralCode = "DJ-AMBANI-777",
            friendsReferred = 5
        )
    )
    val userAccount: StateFlow<UserAccount> = _userAccount.asStateFlow()

    private val _orders = MutableStateFlow<List<BoostOrder>>(
        listOf(
            BoostOrder(
                id = "ORD-8821",
                platform = SocialPlatform.INSTAGRAM,
                serviceType = ServiceType.LIKES,
                targetUrl = "https://instagram.com/p/C9x81aB_DJAmbani",
                quantity = 250,
                coinCost = 500,
                deliveredCount = 180,
                status = OrderStatus.PROCESSING,
                speed = BoostSpeed.TURBO_DJ,
                createdAt = System.currentTimeMillis() - 1000 * 60 * 25
            ),
            BoostOrder(
                id = "ORD-7643",
                platform = SocialPlatform.YOUTUBE,
                serviceType = ServiceType.SUBSCRIBERS,
                targetUrl = "https://youtube.com/@DjAmbaniOfficial",
                quantity = 100,
                coinCost = 800,
                deliveredCount = 100,
                status = OrderStatus.COMPLETED,
                speed = BoostSpeed.FAST,
                createdAt = System.currentTimeMillis() - 1000 * 60 * 180
            ),
            BoostOrder(
                id = "ORD-6512",
                platform = SocialPlatform.TIKTOK,
                serviceType = ServiceType.VIEWS,
                targetUrl = "https://tiktok.com/@olibi_booster/video/7281",
                quantity = 1000,
                coinCost = 1000,
                deliveredCount = 620,
                status = OrderStatus.PROCESSING,
                speed = BoostSpeed.TURBO_DJ,
                createdAt = System.currentTimeMillis() - 1000 * 60 * 10
            )
        )
    )
    val orders: StateFlow<List<BoostOrder>> = _orders.asStateFlow()

    private val _tasks = MutableStateFlow<List<EarnTask>>(
        listOf(
            EarnTask(
                id = "task_dj_beat",
                title = "Listen to DJ Ambani's Electro Drop",
                description = "Experience the exclusive sponsor audio preview for 15s to claim reward",
                rewardCoins = 75,
                category = TaskCategory.DJ_SPECIAL,
                durationSeconds = 15,
                iconName = "music_note",
                actionUrl = "https://djambani.music/track/electro-booster"
            ),
            EarnTask(
                id = "task_sponsored_ad",
                title = "Watch Sponsored Partner Clip",
                description = "Quick 10s interactive video clip sponsored by Ambani Media",
                rewardCoins = 50,
                category = TaskCategory.SPONSORED_VIDEO,
                durationSeconds = 10,
                iconName = "play_circle",
                actionUrl = "https://olibi.booster/sponsored-video"
            ),
            EarnTask(
                id = "task_ig_follow",
                title = "Follow DJ Ambani on Instagram",
                description = "Support the official DJ Ambani artist page & get verified boost coins",
                rewardCoins = 100,
                category = TaskCategory.SOCIAL_FOLLOW,
                iconName = "star",
                actionUrl = "https://instagram.com/djambaniofficial"
            ),
            EarnTask(
                id = "task_yt_sub",
                title = "Subscribe to Olibi Studio YouTube",
                description = "Get notified of live drops, DJ sets and booster coin giveaways",
                rewardCoins = 120,
                category = TaskCategory.SOCIAL_FOLLOW,
                iconName = "subscriptions",
                actionUrl = "https://youtube.com/@OlibiBooster"
            ),
            EarnTask(
                id = "task_invite_share",
                title = "Share Booster Link with a Friend",
                description = "Send your referral link to friends on WhatsApp or Telegram",
                rewardCoins = 150,
                category = TaskCategory.REFERRAL,
                iconName = "share",
                actionUrl = "https://olibi.app/ref/DJ-AMBANI-777"
            )
        )
    )
    val tasks: StateFlow<List<EarnTask>> = _tasks.asStateFlow()

    private val _transactions = MutableStateFlow<List<CoinTransaction>>(
        listOf(
            CoinTransaction(
                id = "TX-901",
                title = "Welcome DJ Ambani Bonus",
                description = "Special sponsorship onboarding reward",
                amount = 500,
                isEarned = true,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24,
                tag = "Sponsor Bonus"
            ),
            CoinTransaction(
                id = "TX-902",
                title = "Daily Check-In Day 3",
                description = "Consecutive streak bonus claimed",
                amount = 75,
                isEarned = true,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 5,
                tag = "Streak"
            ),
            CoinTransaction(
                id = "TX-903",
                title = "Boost Order: Instagram Likes",
                description = "Launched 250 Turbo Likes order #ORD-8821",
                amount = 500,
                isEarned = false,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 25,
                tag = "Boost Order"
            ),
            CoinTransaction(
                id = "TX-904",
                title = "Wheel Spin Jackpot",
                description = "Lucky booster spin multiplier reward",
                amount = 250,
                isEarned = true,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
                tag = "Lucky Spin"
            )
        )
    )
    val transactions: StateFlow<List<CoinTransaction>> = _transactions.asStateFlow()

    init {
        // Start simulated live delivery updates for processing orders
        scope.launch {
            while (isActive) {
                delay(4000)
                updateSimulatedDeliveries()
            }
        }
    }

    private fun updateSimulatedDeliveries() {
        _orders.update { currentOrders ->
            currentOrders.map { order ->
                if (order.status == OrderStatus.PROCESSING) {
                    val increment = when (order.speed) {
                        BoostSpeed.TURBO_DJ -> (8..20).random()
                        BoostSpeed.FAST -> (4..12).random()
                        BoostSpeed.STANDARD -> (2..6).random()
                    }
                    val newCount = (order.deliveredCount + increment).coerceAtMost(order.quantity)
                    val newStatus = if (newCount >= order.quantity) OrderStatus.COMPLETED else OrderStatus.PROCESSING
                    order.copy(deliveredCount = newCount, status = newStatus)
                } else {
                    order
                }
            }
        }
    }

    fun claimDailyBonus(): Boolean {
        val current = _userAccount.value
        val bonus = when (current.checkInStreak % 7) {
            0 -> 50
            1 -> 65
            2 -> 80
            3 -> 100
            4 -> 125
            5 -> 150
            else -> 200
        }
        val newStreak = current.checkInStreak + 1
        _userAccount.update {
            it.copy(
                coins = it.coins + bonus,
                checkInStreak = newStreak,
                totalCoinsEarned = it.totalCoinsEarned + bonus
            )
        }
        addTransaction(
            title = "Daily Streak Reward (Day $newStreak)",
            description = "+$bonus Coins added for consecutive login",
            amount = bonus,
            isEarned = true,
            tag = "Daily Bonus"
        )
        return true
    }

    fun completeTask(taskId: String): Int {
        val task = _tasks.value.find { it.id == taskId } ?: return 0
        if (task.isCompleted) return 0

        _tasks.update { list ->
            list.map { if (it.id == taskId) it.copy(isCompleted = true) else it }
        }

        _userAccount.update {
            it.copy(
                coins = it.coins + task.rewardCoins,
                totalCoinsEarned = it.totalCoinsEarned + task.rewardCoins
            )
        }

        addTransaction(
            title = task.title,
            description = "Completed task: ${task.description.take(40)}...",
            amount = task.rewardCoins,
            isEarned = true,
            tag = "Task Reward"
        )

        return task.rewardCoins
    }

    fun spinLuckyWheel(): Int {
        val possibleRewards = listOf(30, 50, 75, 100, 150, 250, 500)
        val reward = possibleRewards.random()

        _userAccount.update {
            it.copy(
                coins = it.coins + reward,
                totalCoinsEarned = it.totalCoinsEarned + reward
            )
        }

        addTransaction(
            title = "DJ Ambani Lucky Spin",
            description = "Hit the lucky multiplier segment! Won $reward Coins",
            amount = reward,
            isEarned = true,
            tag = "Lucky Spin"
        )

        return reward
    }

    fun placeBoostOrder(
        platform: SocialPlatform,
        serviceType: ServiceType,
        targetUrl: String,
        quantity: Int,
        speed: BoostSpeed
    ): Result<BoostOrder> {
        val baseCost = quantity * serviceType.costPerUnit
        val totalCost = (baseCost * speed.multiplier).toInt()

        val currentCoins = _userAccount.value.coins
        if (currentCoins < totalCost) {
            return Result.failure(Exception("Insufficient coins. You have $currentCoins coins, but need $totalCost coins."))
        }

        val order = BoostOrder(
            id = "ORD-${(1000..9999).random()}",
            platform = platform,
            serviceType = serviceType,
            targetUrl = targetUrl.trim().ifEmpty { "https://${platform.name.lowercase()}.com/user/post" },
            quantity = quantity,
            coinCost = totalCost,
            deliveredCount = 0,
            status = OrderStatus.PROCESSING,
            speed = speed,
            createdAt = System.currentTimeMillis()
        )

        _userAccount.update {
            it.copy(
                coins = it.coins - totalCost,
                totalBoostsCompleted = it.totalBoostsCompleted + 1
            )
        }

        _orders.update { listOf(order) + it }

        addTransaction(
            title = "${platform.displayName} ${serviceType.displayName}",
            description = "Ordered $quantity ${serviceType.unitName} (${speed.badgeText})",
            amount = totalCost,
            isEarned = false,
            tag = "Boost Order"
        )

        return Result.success(order)
    }

    fun buyCoinPack(packTitle: String, coinsGiven: Int, bonusCoins: Int, priceLabel: String): Boolean {
        val total = coinsGiven + bonusCoins
        _userAccount.update {
            it.copy(
                coins = it.coins + total,
                totalCoinsEarned = it.totalCoinsEarned + total
            )
        }

        addTransaction(
            title = "$packTitle ($priceLabel)",
            description = "Refilled wallet with $coinsGiven + $bonusCoins Bonus Coins",
            amount = total,
            isEarned = true,
            tag = "Coin Pack"
        )
        return true
    }

    fun redeemVoucherCode(code: String): Pair<Boolean, String> {
        val cleanCode = code.trim().uppercase()
        val reward = when (cleanCode) {
            "DJAMBANI", "AMBANI2026" -> 1000
            "BOOSTER777", "OLIBI" -> 500
            "ROCKET100" -> 100
            "VIPDROP" -> 300
            else -> 0
        }

        if (reward == 0) {
            return Pair(false, "Invalid promo code. Try 'DJAMBANI' or 'BOOSTER777'")
        }

        _userAccount.update {
            it.copy(
                coins = it.coins + reward,
                totalCoinsEarned = it.totalCoinsEarned + reward
            )
        }

        addTransaction(
            title = "Voucher Code: $cleanCode",
            description = "Redeemed DJ Ambani sponsor promo code",
            amount = reward,
            isEarned = true,
            tag = "Promo Code"
        )

        return Pair(true, "Successfully claimed $reward Free Bonus Coins!")
    }

    fun updateProfile(name: String, email: String) {
        _userAccount.update { it.copy(displayName = name, email = email) }
    }

    fun addReferral() {
        val bonus = 150
        _userAccount.update {
            it.copy(
                coins = it.coins + bonus,
                friendsReferred = it.friendsReferred + 1,
                totalCoinsEarned = it.totalCoinsEarned + bonus
            )
        }
        addTransaction(
            title = "Referral Friend Joined",
            description = "Friend used your invite code: ${userAccount.value.referralCode}",
            amount = bonus,
            isEarned = true,
            tag = "Referral"
        )
    }

    private fun addTransaction(
        title: String,
        description: String,
        amount: Int,
        isEarned: Boolean,
        tag: String
    ) {
        val tx = CoinTransaction(
            id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
            title = title,
            description = description,
            amount = amount,
            isEarned = isEarned,
            timestamp = System.currentTimeMillis(),
            tag = tag
        )
        _transactions.update { listOf(tx) + it }
    }
}
