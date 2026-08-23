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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EarnTask
import com.example.data.model.TaskCategory
import com.example.data.model.UserAccount
import com.example.ui.theme.*

@Composable
fun EarnScreen(
    userAccount: UserAccount,
    tasks: List<EarnTask>,
    onClaimDailyStreak: () -> Unit,
    onOpenWheel: () -> Unit,
    onOpenSponsoredMedia: (EarnTask) -> Unit,
    onCompleteTask: (String) -> Unit,
    onShareReferral: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var copiedReferral by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("earn_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Earn Free Coins",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = OlibiGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%,d", userAccount.coins),
                            color = OlibiGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Complete quick partner tasks & unlock unlimited boosts",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Daily Check-in Streak Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Brush.horizontalGradient(listOf(OlibiPurple, OlibiCyanAccent)), RoundedCornerShape(20.dp))
                    .testTag("daily_checkin_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Whatshot,
                                contentDescription = "Fire",
                                tint = WarningOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Daily Check-In Streak",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Day ${userAccount.checkInStreak} Active",
                            color = WarningOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 7-day streak bubbles
                    val rewards = listOf(50, 65, 80, 100, 125, 150, 200)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (i in 0 until 7) {
                            val isClaimed = i < (userAccount.checkInStreak % 7)
                            val isCurrent = i == (userAccount.checkInStreak % 7)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isClaimed) SuccessGreen.copy(alpha = 0.2f)
                                            else if (isCurrent) OlibiGold.copy(alpha = 0.25f)
                                            else DarkBackground
                                        )
                                        .border(
                                            1.dp,
                                            if (isClaimed) SuccessGreen else if (isCurrent) OlibiGold else DarkSurfaceHighlight,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isClaimed) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Claimed",
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "+${rewards[i]}",
                                            color = if (isCurrent) OlibiGold else TextTertiary,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "D${i + 1}",
                                    color = if (isCurrent) OlibiGold else TextTertiary,
                                    fontSize = 10.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onClaimDailyStreak,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("claim_daily_streak_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OlibiGold,
                            contentColor = DarkBackground
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "CLAIM TODAY'S REWARD (+${rewards[userAccount.checkInStreak % 7]} COINS)",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Lucky Wheel Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onOpenWheel() }
                    .border(1.dp, OlibiGold.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .testTag("lucky_wheel_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(DarkSurfaceVariant, OlibiPurpleDark)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = OlibiGold.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, OlibiGold)
                            ) {
                                Text(
                                    text = "SPIN & WIN",
                                    color = OlibiGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "DJ Ambani Lucky Wheel",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Win up to 500 free booster coins every turn",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = onOpenWheel,
                            colors = ButtonDefaults.buttonColors(containerColor = OlibiPurple),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("SPIN 🎯", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section: Partner Tasks
        item {
            Text(
                text = "Partner Missions & Tasks",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Task Items
        items(tasks) { task ->
            TaskItemCard(
                task = task,
                onStartTask = {
                    if (task.category == TaskCategory.DJ_SPECIAL || task.category == TaskCategory.SPONSORED_VIDEO) {
                        onOpenSponsoredMedia(task)
                    } else {
                        onCompleteTask(task.id)
                    }
                }
            )
        }

        // Referral Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OlibiCyanAccent.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                    .testTag("referral_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Referral",
                                tint = OlibiCyanAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Invite Friends, Earn +150",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${userAccount.friendsReferred} Invited",
                            color = OlibiCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Share your unique DJ Ambani referral code. Both you and your friend get 150 Booster Coins upon signup.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkBackground)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = userAccount.referralCode,
                            color = OlibiGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )

                        Row {
                            TextButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(userAccount.referralCode))
                                    copiedReferral = true
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (copiedReferral) "Copied!" else "Copy Code",
                                    color = if (copiedReferral) SuccessGreen else OlibiPurpleLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = onShareReferral,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = OlibiCyanAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItemCard(
    task: EarnTask,
    onStartTask: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (task.isCompleted) SuccessGreen.copy(alpha = 0.3f) else DarkSurfaceHighlight,
                RoundedCornerShape(16.dp)
            )
            .testTag("task_item_${task.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = when (task.category) {
                        TaskCategory.DJ_SPECIAL -> OlibiPurple.copy(alpha = 0.2f)
                        TaskCategory.SPONSORED_VIDEO -> OlibiElectricBlue.copy(alpha = 0.2f)
                        else -> OlibiCyanAccent.copy(alpha = 0.15f)
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (task.isCompleted) SuccessGreen else OlibiPurple
                    )
                ) {
                    Icon(
                        imageVector = if (task.isCompleted) Icons.Default.Check
                        else if (task.category == TaskCategory.DJ_SPECIAL) Icons.Default.MusicNote
                        else if (task.category == TaskCategory.SPONSORED_VIDEO) Icons.Default.PlayCircle
                        else Icons.Default.Star,
                        contentDescription = "Task Icon",
                        tint = if (task.isCompleted) SuccessGreen else OlibiCyanAccent,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = task.title,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.description,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (task.isCompleted) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SuccessGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "DONE",
                        color = SuccessGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            } else {
                Button(
                    onClick = onStartTask,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (task.category == TaskCategory.DJ_SPECIAL) OlibiGold else OlibiPurple,
                        contentColor = if (task.category == TaskCategory.DJ_SPECIAL) DarkBackground else TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("start_task_button_${task.id}")
                ) {
                    Text(
                        text = "+${task.rewardCoins}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
