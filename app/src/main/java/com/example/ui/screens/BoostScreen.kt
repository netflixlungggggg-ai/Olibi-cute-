package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun BoostScreen(
    userAccount: UserAccount,
    orders: List<BoostOrder>,
    initialPlatform: SocialPlatform? = null,
    initialService: ServiceType? = null,
    onPlaceOrder: (SocialPlatform, ServiceType, String, Int, BoostSpeed) -> Result<BoostOrder>,
    onOpenWallet: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    var selectedPlatform by remember { mutableStateOf(initialPlatform ?: SocialPlatform.INSTAGRAM) }
    var selectedService by remember { mutableStateOf(initialService ?: ServiceType.LIKES) }
    var targetUrl by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(100) }
    var selectedSpeed by remember { mutableStateOf(BoostSpeed.TURBO_DJ) }

    var orderError by remember { mutableStateOf<String?>(null) }
    var orderSuccess by remember { mutableStateOf<BoostOrder?>(null) }

    val baseCost = quantity * selectedService.costPerUnit
    val totalCost = (baseCost * selectedSpeed.multiplier).toInt()
    val canAfford = userAccount.coins >= totalCost

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("boost_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create Boost Order",
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
                    text = "Automated high-velocity social amplification by DJ Ambani network",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // 1. Social Platform Selector
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
                        text = "1. Select Social Platform",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(SocialPlatform.values()) { platform ->
                            val isSelected = selectedPlatform == platform
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(platform.brandColorHex).copy(alpha = 0.25f) else DarkSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(platform.brandColorHex) else DarkSurfaceHighlight
                                ),
                                modifier = Modifier
                                    .clickable { selectedPlatform = platform }
                                    .testTag("platform_chip_${platform.name.lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(platform.brandColorHex))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = platform.displayName,
                                        color = if (isSelected) TextPrimary else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Service Type Selector
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
                        text = "2. Select Service Type",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ServiceType.values().take(3).forEach { service ->
                            val isSelected = selectedService == service
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) OlibiPurple.copy(alpha = 0.25f) else DarkSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) OlibiPurple else DarkSurfaceHighlight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedService = service
                                        quantity = service.defaultQty
                                    }
                                    .testTag("service_chip_${service.name.lowercase()}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = service.unitName,
                                        color = if (isSelected) TextPrimary else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${service.costPerUnit} C / unit",
                                        color = OlibiGold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ServiceType.values().drop(3).forEach { service ->
                            val isSelected = selectedService == service
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) OlibiPurple.copy(alpha = 0.25f) else DarkSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) OlibiPurple else DarkSurfaceHighlight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedService = service
                                        quantity = service.defaultQty
                                    }
                                    .testTag("service_chip_${service.name.lowercase()}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = service.unitName,
                                        color = if (isSelected) TextPrimary else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${service.costPerUnit} C / unit",
                                        color = OlibiGold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Target Link URL Input
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
                        text = "3. Target Profile / Post URL",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = targetUrl,
                        onValueChange = { targetUrl = it },
                        placeholder = {
                            Text(
                                text = "https://${selectedPlatform.name.lowercase()}.com/p/your_post_id",
                                color = TextTertiary,
                                fontSize = 12.sp
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                val text = clipboardManager.getText()?.text
                                if (!text.isNullOrBlank()) {
                                    targetUrl = text
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = OlibiCyanAccent
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("target_url_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OlibiPurple,
                            unfocusedBorderColor = DarkSurfaceHighlight,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // 4. Quantity & Speed Selector
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceHighlight, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "4. Boost Quantity",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${String.format("%,d", quantity)} ${selectedService.unitName}",
                            color = OlibiCyanAccent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Slider(
                        value = quantity.toFloat(),
                        onValueChange = { quantity = it.toInt() },
                        valueRange = selectedService.minQty.toFloat()..selectedService.maxQty.toFloat(),
                        steps = 19,
                        colors = SliderDefaults.colors(
                            thumbColor = OlibiGold,
                            activeTrackColor = OlibiPurple,
                            inactiveTrackColor = DarkSurfaceHighlight
                        ),
                        modifier = Modifier.testTag("quantity_slider")
                    )

                    // Quick Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(50, 100, 250, 500, 1000).forEach { chipQty ->
                            if (chipQty in selectedService.minQty..selectedService.maxQty) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (quantity == chipQty) OlibiGold.copy(alpha = 0.2f) else DarkSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(
                                        0.5.dp,
                                        if (quantity == chipQty) OlibiGold else DarkSurfaceHighlight
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { quantity = chipQty }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$chipQty",
                                            color = if (quantity == chipQty) OlibiGold else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Delivery Speed",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BoostSpeed.values().forEach { speed ->
                            val isSelected = selectedSpeed == speed
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) OlibiPurple.copy(alpha = 0.25f) else DarkSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) OlibiPurpleLight else DarkSurfaceHighlight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedSpeed = speed }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = speed.badgeText,
                                        color = if (isSelected) OlibiCyanAccent else TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Summary & Launch CTA
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(OlibiPurple, OlibiGold)),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL ESTIMATED COST",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = "Coin",
                                    tint = OlibiGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%,d Coins", totalCost),
                                    color = OlibiGold,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        if (!canAfford) {
                            Button(
                                onClick = onOpenWallet,
                                colors = ButtonDefaults.buttonColors(containerColor = OlibiGold),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Refill + Coins",
                                    color = DarkBackground,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    if (orderError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = orderError!!,
                            color = ErrorRed,
                            fontSize = 12.sp
                        )
                    }

                    if (orderSuccess != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🎉 Boost Order #${orderSuccess!!.id} Launched Successfully!",
                            color = SuccessGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            orderError = null
                            val res = onPlaceOrder(selectedPlatform, selectedService, targetUrl, quantity, selectedSpeed)
                            res.onSuccess {
                                orderSuccess = it
                                targetUrl = ""
                            }.onFailure {
                                orderError = it.message ?: "Failed to place boost order"
                            }
                        },
                        enabled = canAfford,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("launch_boost_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OlibiPurple,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "Launch",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (canAfford) "LAUNCH VIRAL BOOST 🚀" else "INSUFFICIENT COINS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // Active Orders List Section
        item {
            Text(
                text = "Active & Recent Campaigns (${orders.size})",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(orders) { order ->
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceHighlight, RoundedCornerShape(16.dp))
                    .testTag("order_card_${order.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
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
                                    .background(if (order.status == OrderStatus.COMPLETED) SuccessGreen else OlibiCyanAccent)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${order.platform.displayName} • ${order.serviceType.displayName}",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (order.status == OrderStatus.COMPLETED) SuccessGreen.copy(alpha = 0.2f) else OlibiPurple.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = if (order.status == OrderStatus.COMPLETED) "DELIVERED" else "BOOSTING (${order.speed.badgeText})",
                                color = if (order.status == OrderStatus.COMPLETED) SuccessGreen else OlibiPurpleLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.targetUrl,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Delivered: ${order.deliveredCount} / ${order.quantity}",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${(order.progress * 100).toInt()}%",
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
        }
    }
}
