package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.BoosterRepository
import com.example.ui.components.LuckyWheelDialog
import com.example.ui.components.RefillCoinsBottomSheet
import com.example.ui.components.SponsoredMediaDialog
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MainContainerScreen(
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val userAccount by BoosterRepository.userAccount.collectAsState()
    val activeOrders by BoosterRepository.orders.collectAsState()
    val tasks by BoosterRepository.tasks.collectAsState()
    val transactions by BoosterRepository.transactions.collectAsState()

    var currentTab by remember { mutableIntStateOf(0) }
    var presetPlatform by remember { mutableStateOf<SocialPlatform?>(null) }
    var presetService by remember { mutableStateOf<ServiceType?>(null) }

    var showWheelDialog by remember { mutableStateOf(false) }
    var showMediaDialog by remember { mutableStateOf<EarnTask?>(null) }
    var showRefillSheet by remember { mutableStateOf(false) }

    fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "🚀 Join Olibi Booster - Sponsored by DJ Ambani! Turbocharge your social growth and get +150 Free Coins: https://olibi.app/ref/${userAccount.referralCode}"
            )
        }
        context.startActivity(Intent.createChooser(intent, "Share Olibi Booster"))
        BoosterRepository.addReferral()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("app_snackbar")
            ) { data ->
                Snackbar(
                    containerColor = DarkSurfaceVariant,
                    contentColor = TextPrimary,
                    actionColor = OlibiGold,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    snackbarData = data
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black,
                contentColor = TextPrimary,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                // Tab 0: Home / Lobby
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextPrimary,
                        selectedTextColor = OlibiPurple,
                        indicatorColor = OlibiPurple,
                        unselectedIconColor = TextTertiary,
                        unselectedTextColor = TextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_home")
                )

                // Tab 1: Earn
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    icon = { Icon(Icons.Default.MonetizationOn, contentDescription = "Earn") },
                    label = { Text("Earn", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextPrimary,
                        selectedTextColor = OlibiPurple,
                        indicatorColor = OlibiPurple,
                        unselectedIconColor = TextTertiary,
                        unselectedTextColor = TextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_earn")
                )

                // Tab 2: Boost (Center Action)
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(OlibiPurple)
                                .border(1.5.dp, OlibiCyanAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Boost",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    label = { Text("Boost", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OlibiCyanAccent) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextPrimary,
                        selectedTextColor = OlibiCyanAccent,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = TextTertiary,
                        unselectedTextColor = TextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_boost")
                )

                // Tab 3: Wallet
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet") },
                    label = { Text("Wallet", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextPrimary,
                        selectedTextColor = OlibiPurple,
                        indicatorColor = OlibiPurple,
                        unselectedIconColor = TextTertiary,
                        unselectedTextColor = TextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_wallet")
                )

                // Tab 4: Profile
                NavigationBarItem(
                    selected = currentTab == 4,
                    onClick = { currentTab = 4 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextPrimary,
                        selectedTextColor = OlibiPurple,
                        indicatorColor = OlibiPurple,
                        unselectedIconColor = TextTertiary,
                        unselectedTextColor = TextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> LobbyScreen(
                    userAccount = userAccount,
                    activeOrders = activeOrders,
                    onNavigateToBoost = { platform, service ->
                        presetPlatform = platform
                        presetService = service
                        currentTab = 2
                    },
                    onNavigateToEarn = { currentTab = 1 },
                    onNavigateToWallet = { currentTab = 3 },
                    onPlaySponsorTrack = {
                        val trackTask = tasks.find { it.category == TaskCategory.DJ_SPECIAL }
                        if (trackTask != null) {
                            showMediaDialog = trackTask
                        } else {
                            showMediaDialog = EarnTask(
                                id = "track_preview",
                                title = "Dj Ambani - Booster Drop (Club VIP Mix)",
                                description = "Exclusive 15s track preview",
                                rewardCoins = 75,
                                category = TaskCategory.DJ_SPECIAL
                            )
                        }
                    }
                )

                1 -> EarnScreen(
                    userAccount = userAccount,
                    tasks = tasks,
                    onClaimDailyStreak = {
                        BoosterRepository.claimDailyBonus()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("🔥 Daily streak bonus claimed!")
                        }
                    },
                    onOpenWheel = { showWheelDialog = true },
                    onOpenSponsoredMedia = { task -> showMediaDialog = task },
                    onCompleteTask = { taskId ->
                        val coins = BoosterRepository.completeTask(taskId)
                        if (coins > 0) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("🎉 Task complete! +$coins Coins added")
                            }
                        }
                    },
                    onShareReferral = { shareApp() }
                )

                2 -> BoostScreen(
                    userAccount = userAccount,
                    orders = activeOrders,
                    initialPlatform = presetPlatform,
                    initialService = presetService,
                    onPlaceOrder = { platform, service, url, qty, speed ->
                        val res = BoosterRepository.placeBoostOrder(platform, service, url, qty, speed)
                        if (res.isSuccess) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("🚀 Boost Order Launched! Turbo delivery active")
                            }
                        }
                        res
                    },
                    onOpenWallet = { showRefillSheet = true }
                )

                3 -> WalletScreen(
                    userAccount = userAccount,
                    transactions = transactions,
                    onOpenRefillSheet = { showRefillSheet = true },
                    onRedeemVoucher = { code ->
                        val res = BoosterRepository.redeemVoucherCode(code)
                        if (res.first) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(res.second)
                            }
                        }
                        res
                    }
                )

                4 -> ProfileScreen(
                    userAccount = userAccount,
                    onSignOut = onSignOut,
                    onShareApp = { shareApp() }
                )
            }
        }
    }

    // Minigame & Media Dialogs
    if (showWheelDialog) {
        LuckyWheelDialog(
            onDismiss = { showWheelDialog = false },
            onSpinComplete = { won ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("🎉 Lucky Wheel Jackpot! +$won Coins Added!")
                }
            }
        )
    }

    if (showMediaDialog != null) {
        val task = showMediaDialog!!
        SponsoredMediaDialog(
            title = task.title,
            rewardAmount = task.rewardCoins,
            durationSeconds = if (task.durationSeconds > 0) task.durationSeconds else 15,
            onDismiss = { showMediaDialog = null },
            onClaimReward = {
                BoosterRepository.completeTask(task.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("🎉 Media Complete! +${task.rewardCoins} Coins Claimed!")
                }
            }
        )
    }

    if (showRefillSheet) {
        RefillCoinsBottomSheet(
            onDismiss = { showRefillSheet = false },
            onBuyPack = { title, coins, bonus, price ->
                BoosterRepository.buyCoinPack(title, coins, bonus, price)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("🎉 Successfully added ${coins + bonus} Coins to your balance!")
                }
            },
            onRedeemVoucher = { code ->
                BoosterRepository.redeemVoucherCode(code)
            }
        )
    }
}
