package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.CoinBalanceCard
import com.example.ui.components.DjAmbaniSponsorCard
import com.example.ui.theme.*

@Composable
fun LobbyScreen(
    userAccount: UserAccount,
    activeOrders: List<BoostOrder>,
    onNavigateToBoost: (SocialPlatform?, ServiceType?) -> Unit,
    onNavigateToEarn: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onPlaySponsorTrack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("lobby_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Card
        item {
            CoinBalanceCard(
                userAccount = userAccount,
                onRefillClick = onNavigateToWallet,
                onDailyStreakClick = onNavigateToEarn
            )
        }

        // DJ Ambani Sponsor Card
        item {
            DjAmbaniSponsorCard(
                onListenTrack = onPlaySponsorTrack
            )
        }

        // Section Title: Boost Your Social
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Boost Your Social",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    TextButton(onClick = { onNavigateToBoost(null, null) }) {
                        Text(
                            text = "View All",
                            color = OlibiCyanAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Select a category to launch automated growth",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // 2x3 Grid of Action Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LobbyActionCard(
                        icon = Icons.Default.Favorite,
                        title = "Boost Likes",
                        subtitle = "Instagram, YT, TikTok",
                        badge = "POPULAR",
                        color = Color(0xFFE1306C),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToBoost(SocialPlatform.INSTAGRAM, ServiceType.LIKES) }
                    )
                    LobbyActionCard(
                        icon = Icons.Default.PersonAdd,
                        title = "Followers",
                        subtitle = "Real & Fast Delivery",
                        badge = "HOT",
                        color = OlibiPurpleLight,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToBoost(SocialPlatform.INSTAGRAM, ServiceType.FOLLOWERS) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LobbyActionCard(
                        icon = Icons.Default.Visibility,
                        title = "Boost Views",
                        subtitle = "Reels, Shorts, YT",
                        badge = "FAST",
                        color = OlibiCyanAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToBoost(SocialPlatform.YOUTUBE, ServiceType.VIEWS) }
                    )
                    LobbyActionCard(
                        icon = Icons.Default.Comment,
                        title = "Comments",
                        subtitle = "Custom Engagement",
                        badge = "VIRAL",
                        color = OlibiGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToBoost(SocialPlatform.TIKTOK, ServiceType.COMMENTS) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LobbyActionCard(
                        icon = Icons.Default.Subscriptions,
                        title = "Subscribers",
                        subtitle = "Channel Monetization",
                        badge = "PRO",
                        color = Color(0xFFFF0000),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToBoost(SocialPlatform.YOUTUBE, ServiceType.SUBSCRIBERS) }
                    )
                    LobbyActionCard(
                        icon = Icons.Default.Share,
                        title = "Shares & Retweets",
                        subtitle = "Algorithm Multiplier",
                        badge = "TRENDING",
                        color = OlibiElectricBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToBoost(SocialPlatform.X_TWITTER, ServiceType.LIKES) }
                    )
                }
            }
        }

        // Active Orders Live Terminal
        if (activeOrders.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Live Booster Terminal",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${activeOrders.count { it.status == OrderStatus.PROCESSING }} Active",
                            color = OlibiCyanAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            activeOrders.take(2).forEach { order ->
                                OrderTerminalRow(order = order)
                            }
                        }
                    }
                }
            }
        }

        // How it Works Guide
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceHighlight, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚀 How Olibi Booster Works",
                        color = OlibiGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HowItWorksStep(step = "1", text = "Earn Coins via daily login, wheel spin or listening to DJ Ambani beats")
                    Spacer(modifier = Modifier.height(8.dp))
                    HowItWorksStep(step = "2", text = "Select social network & configure target boost order")
                    Spacer(modifier = Modifier.height(8.dp))
                    HowItWorksStep(step = "3", text = "Watch real-time delivery with DJ Ambani high-speed turbo booster!")
                }
            }
        }
    }
}

@Composable
fun LobbyActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, DarkSurfaceHighlight, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("action_card_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = color,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.35f)
                ) {
                    Text(
                        text = badge,
                        color = color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun OrderTerminalRow(order: BoostOrder) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${order.platform.displayName} • ${order.serviceType.displayName}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                if (order.status == OrderStatus.PROCESSING) {
                    Text(
                        text = "TURBO",
                        color = OlibiCyanAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Text(
                text = "${order.deliveredCount}/${order.quantity}",
                color = if (order.status == OrderStatus.COMPLETED) SuccessGreen else OlibiGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { order.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = if (order.status == OrderStatus.COMPLETED) SuccessGreen else OlibiPurple,
            trackColor = DarkSurfaceHighlight
        )
    }
}

@Composable
fun HowItWorksStep(step: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = OlibiPurple.copy(alpha = 0.25f),
            modifier = Modifier.size(22.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = step,
                    color = OlibiPurpleLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}
