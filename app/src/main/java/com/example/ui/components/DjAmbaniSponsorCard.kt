package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Verified
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
import com.example.ui.theme.*

@Composable
fun DjAmbaniSponsorCard(
    modifier: Modifier = Modifier,
    onListenTrack: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(OlibiPurpleLight.copy(alpha = 0.5f), OlibiCyanAccent.copy(alpha = 0.5f))
                ),
                RoundedCornerShape(20.dp)
            )
            .testTag("dj_ambani_sponsor_card"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background DJ banner image
            Image(
                painter = painterResource(id = R.drawable.bg_dj_ambani_banner),
                contentDescription = "DJ Ambani Sponsor Stage",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.35f
            )

            // Gradient scrim
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                DarkSurfaceVariant.copy(alpha = 0.85f),
                                DarkSurfaceVariant
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = OlibiGold.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, OlibiGold)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "DJ Icon",
                                tint = OlibiGold,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "SPONSORED BY",
                                    color = OlibiGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Sponsor",
                                    tint = OlibiCyanAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "Dj Ambani Official",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // VIP Sponsor Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = OlibiPurple.copy(alpha = 0.25f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OlibiPurpleLight)
                    ) {
                        Text(
                            text = "OFFICIAL PARTNER",
                            color = OlibiPurpleLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Powering high-speed viral growth, instant engagement & club vibe soundtrack for all Olibi Boosters worldwide.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive Audio Preview Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkBackground.copy(alpha = 0.7f))
                        .clickable {
                            isPlaying = !isPlaying
                            onListenTrack()
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                isPlaying = !isPlaying
                                onListenTrack()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = OlibiCyanAccent
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Dj Ambani - Booster Drop (Club VIP Mix)",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (isPlaying) "Playing sponsored preview • Tap for +75 Coins" else "Tap to listen & earn bonus coins",
                                color = if (isPlaying) OlibiGold else TextTertiary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Equalizer visualizer
                    EqualizerVisualizer(isAnimating = isPlaying)
                }
            }
        }
    }
}

@Composable
fun EqualizerVisualizer(isAnimating: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq_anim")

    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f, targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 16f, targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(280, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 8f, targetValue = 22f,
        animationSpec = infiniteRepeatable(
            animation = tween(420, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h3"
    )
    val h4 by infiniteTransition.animateFloat(
        initialValue = 18f, targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(310, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h4"
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.height(24.dp)
    ) {
        listOf(h1, h2, h3, h4).forEach { height ->
            Box(
                modifier = Modifier
                    .width(3.5.dp)
                    .height(if (isAnimating) height.dp else 6.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(OlibiCyanAccent, OlibiPurple)
                        )
                    )
            )
        }
    }
}
