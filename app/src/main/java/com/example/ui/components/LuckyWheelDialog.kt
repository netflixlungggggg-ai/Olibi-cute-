package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LuckyWheelDialog(
    onDismiss: () -> Unit,
    onSpinComplete: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var wonAmount by remember { mutableStateOf<Int?>(null) }
    val rotationAngle = remember { Animatable(0f) }

    val segments = listOf(
        Pair(50, Color(0xFF7C4DFF)),
        Pair(100, Color(0xFF2962FF)),
        Pair(30, Color(0xFF00E5FF)),
        Pair(250, Color(0xFFFFD700)),
        Pair(75, Color(0xFF5E35B1)),
        Pair(500, Color(0xFFFF5252)),
        Pair(150, Color(0xFF00E676)),
        Pair(100, Color(0xFFFF9100))
    )

    Dialog(onDismissRequest = { if (!isSpinning) onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(
                    2.dp,
                    Brush.linearGradient(listOf(OlibiGold, OlibiCyanAccent, OlibiPurple)),
                    RoundedCornerShape(24.dp)
                )
                .testTag("lucky_wheel_dialog"),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = OlibiGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DJ Ambani Lucky Wheel",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        enabled = !isSpinning,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Spin the wheel to win up to 500 free booster coins!",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // The Wheel & Pointer Container
                Box(
                    modifier = Modifier.size(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Wheel Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(rotationAngle.value)
                    ) {
                        val sweepAngle = 360f / segments.size
                        val diameter = size.minDimension
                        val radius = diameter / 2f
                        val center = Offset(radius, radius)

                        for (i in segments.indices) {
                            val startAngle = i * sweepAngle
                            drawArc(
                                color = segments[i].second,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = true,
                                size = Size(diameter, diameter)
                            )
                        }

                        // Outer border
                        drawCircle(
                            color = OlibiGold,
                            radius = radius - 2,
                            center = center,
                            style = Stroke(width = 6f)
                        )

                        // Center knob
                        drawCircle(
                            color = DarkBackground,
                            radius = 28f,
                            center = center
                        )
                        drawCircle(
                            color = OlibiGold,
                            radius = 18f,
                            center = center
                        )
                    }

                    // Top Pointer indicator
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-6).dp)
                            .size(22.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(OlibiGold)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (wonAmount != null) {
                    Surface(
                        color = OlibiGold.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OlibiGold),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Won Coins",
                                tint = OlibiGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🎉 You Won +$wonAmount Coins!",
                                color = OlibiGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                Button(
                    onClick = {
                        if (isSpinning) return@Button
                        isSpinning = true
                        wonAmount = null
                        coroutineScope.launch {
                            val randomTurns = (4..8).random()
                            val chosenSegmentIndex = (0 until segments.size).random()
                            val sweep = 360f / segments.size
                            val targetAngle = (randomTurns * 360f) + (chosenSegmentIndex * sweep) + (sweep / 2f)

                            rotationAngle.animateTo(
                                targetValue = targetAngle,
                                animationSpec = tween(
                                    durationMillis = 3200,
                                    easing = FastOutSlowInEasing
                                )
                            )

                            val won = segments[chosenSegmentIndex].first
                            wonAmount = won
                            isSpinning = false
                            onSpinComplete(won)
                        }
                    },
                    enabled = !isSpinning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("spin_wheel_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OlibiPurple,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isSpinning) "SPINNING..." else if (wonAmount != null) "SPIN AGAIN" else "SPIN FOR COINS 🚀",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
