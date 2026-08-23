package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserAccount
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userAccount: UserAccount,
    onSignOut: () -> Unit,
    onShareApp: () -> Unit
) {
    var soundFxEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var turboModeEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Text(
                text = "Booster Profile",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // User Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, Brush.horizontalGradient(listOf(OlibiPurple, OlibiCyanAccent)), RoundedCornerShape(22.dp))
                    .testTag("user_profile_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(OlibiPurple, OlibiElectricBlue)))
                                .border(2.dp, OlibiCyanAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userAccount.displayName.take(1).uppercase(),
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userAccount.displayName,
                                    color = TextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = OlibiCyanAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = userAccount.email,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = OlibiGold.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, OlibiGold)
                            ) {
                                Text(
                                    text = userAccount.vipTier,
                                    color = OlibiGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = DarkSurfaceHighlight)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileStatItem(
                            label = "Coins Earned",
                            value = String.format("%,d", userAccount.totalCoinsEarned),
                            icon = Icons.Default.MonetizationOn,
                            tint = OlibiGold
                        )
                        ProfileStatItem(
                            label = "Total Boosts",
                            value = "${userAccount.totalBoostsCompleted}",
                            icon = Icons.Default.RocketLaunch,
                            tint = OlibiPurpleLight
                        )
                        ProfileStatItem(
                            label = "Referrals",
                            value = "${userAccount.friendsReferred}",
                            icon = Icons.Default.People,
                            tint = OlibiCyanAccent
                        )
                    }
                }
            }
        }

        // About Sponsor: Dj Ambani Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OlibiGold.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.bg_dj_ambani_banner),
                        contentDescription = "DJ Ambani Stage",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "About DJ Ambani",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = OlibiGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "OFFICIAL SPONSOR",
                                    color = OlibiGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "International DJ, music producer & digital entrepreneur. DJ Ambani sponsors Olibi Booster to democratize high-velocity creator amplification worldwide.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // App Settings & Preferences
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceHighlight, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Booster Engine Settings",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SettingToggleRow(
                        title = "DJ Ambani Turbo Delivery",
                        subtitle = "Prioritize ultra-fast 3x speed node delivery",
                        checked = turboModeEnabled,
                        onCheckedChange = { turboModeEnabled = it }
                    )

                    HorizontalDivider(color = DarkSurfaceHighlight, modifier = Modifier.padding(vertical = 8.dp))

                    SettingToggleRow(
                        title = "Sound & Beat Effects",
                        subtitle = "Audio feedback on rewards and order creation",
                        checked = soundFxEnabled,
                        onCheckedChange = { soundFxEnabled = it }
                    )

                    HorizontalDivider(color = DarkSurfaceHighlight, modifier = Modifier.padding(vertical = 8.dp))

                    SettingToggleRow(
                        title = "Push Notifications",
                        subtitle = "Get alerted when boost campaign reaches 100%",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }
        }

        // Action Buttons: Share & Sign Out
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onShareApp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("share_app_button"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OlibiCyanAccent),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = OlibiCyanAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Share Olibi Booster", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Button(
                    onClick = onSignOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("sign_out_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = ErrorRed),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", tint = ErrorRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Switch Account / Sign Out", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = label, color = TextSecondary, fontSize = 10.sp)
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = OlibiPurple,
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = DarkSurfaceHighlight
            )
        )
    }
}
