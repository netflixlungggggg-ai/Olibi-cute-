package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class CoinPack(
    val id: String,
    val title: String,
    val coins: Int,
    val bonusCoins: Int,
    val price: String,
    val isBestValue: Boolean = false,
    val badge: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefillCoinsBottomSheet(
    onDismiss: () -> Unit,
    onBuyPack: (String, Int, Int, String) -> Unit,
    onRedeemVoucher: (String) -> Pair<Boolean, String>
) {
    var promoCodeInput by remember { mutableStateOf("") }
    var promoMessage by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

    val packs = listOf(
        CoinPack("pack_1", "Starter Booster", 500, 0, "$0.99", false, null),
        CoinPack("pack_2", "Viral Pro Pack", 1500, 250, "$2.49", true, "POPULAR"),
        CoinPack("pack_3", "DJ Ambani VIP Drop", 5000, 2000, "$6.99", false, "SPONSOR VIP +40% BONUS"),
        CoinPack("pack_4", "Mega Influencer Fleet", 15000, 7500, "$19.99", false, "BEST VALUE +50% BONUS")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextSecondary.copy(alpha = 0.4f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .testTag("refill_coins_sheet")
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
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = OlibiGold,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Refill Booster Coins",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Instant credit & exclusive DJ Ambani bonuses",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Promo Code input
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = promoCodeInput,
                            onValueChange = { promoCodeInput = it },
                            placeholder = { Text("Enter Promo Code (e.g. DJAMBANI)", fontSize = 12.sp, color = TextTertiary) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("promo_code_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OlibiPurple,
                                unfocusedBorderColor = DarkSurfaceHighlight,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (promoCodeInput.isNotBlank()) {
                                    val res = onRedeemVoucher(promoCodeInput)
                                    promoMessage = res
                                    if (res.first) promoCodeInput = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OlibiPurple),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("apply_promo_code_button")
                        ) {
                            Text("Apply", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    if (promoMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = promoMessage!!.second,
                            color = if (promoMessage!!.first) SuccessGreen else ErrorRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pack list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(packs) { pack ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = if (pack.isBestValue || pack.badge != null) 1.5.dp else 1.dp,
                                brush = if (pack.isBestValue) Brush.horizontalGradient(listOf(OlibiGold, OlibiCyanAccent))
                                else if (pack.badge != null) Brush.horizontalGradient(listOf(OlibiPurple, OlibiCyanAccent))
                                else Brush.horizontalGradient(listOf(DarkSurfaceHighlight, DarkSurfaceHighlight)),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                onBuyPack(pack.title, pack.coins, pack.bonusCoins, pack.price)
                                onDismiss()
                            }
                            .testTag("coin_pack_${pack.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (pack.badge != null) DarkSurfaceVariant else DarkSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                if (pack.badge != null) {
                                    Surface(
                                        color = OlibiGold.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(0.5.dp, OlibiGold)
                                    ) {
                                        Text(
                                            text = pack.badge,
                                            color = OlibiGold,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text(
                                    text = pack.title,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${String.format("%,d", pack.coins)} Coins",
                                        color = OlibiGold,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (pack.bonusCoins > 0) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "+${String.format("%,d", pack.bonusCoins)} BONUS",
                                            color = SuccessGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    onBuyPack(pack.title, pack.coins, pack.bonusCoins, pack.price)
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (pack.isBestValue || pack.badge != null) OlibiGold else OlibiPurple,
                                    contentColor = if (pack.isBestValue || pack.badge != null) DarkBackground else TextPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = pack.price,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
