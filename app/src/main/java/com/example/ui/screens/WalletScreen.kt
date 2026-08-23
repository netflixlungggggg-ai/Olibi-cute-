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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CoinTransaction
import com.example.data.model.UserAccount
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    userAccount: UserAccount,
    transactions: List<CoinTransaction>,
    onOpenRefillSheet: () -> Unit,
    onRedeemVoucher: (String) -> Pair<Boolean, String>
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var promoCodeInput by remember { mutableStateOf("") }
    var promoResult by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

    val filteredTransactions = remember(transactions, selectedFilter) {
        when (selectedFilter) {
            "EARNED" -> transactions.filter { it.isEarned }
            "SPENT" -> transactions.filter { !it.isEarned }
            else -> transactions
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("wallet_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Text(
                text = "Booster Wallet",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Hero Balance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, Brush.horizontalGradient(listOf(OlibiGold, OlibiPurple)), RoundedCornerShape(22.dp))
                    .testTag("wallet_hero_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(DarkSurfaceVariant, DarkBackground, OlibiPurpleDark)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL BALANCE",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = OlibiGold.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, OlibiGold)
                            ) {
                                Text(
                                    text = userAccount.vipTier,
                                    color = OlibiGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Gold Coin",
                                tint = OlibiGold,
                                modifier = Modifier.size(34.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = String.format("%,d", userAccount.coins),
                                color = TextPrimary,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Coins",
                                color = OlibiGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onOpenRefillSheet,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("refill_pack_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = OlibiGold, contentColor = DarkBackground),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Refill Coins", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Voucher / Promo Code Box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceHighlight, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Redeem DJ Ambani Promo Code",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = promoCodeInput,
                            onValueChange = { promoCodeInput = it },
                            placeholder = { Text("Code (e.g. DJAMBANI)", fontSize = 12.sp, color = TextTertiary) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("wallet_promo_input"),
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
                                    promoResult = res
                                    if (res.first) promoCodeInput = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OlibiPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Redeem", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    if (promoResult != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = promoResult!!.second,
                            color = if (promoResult!!.first) SuccessGreen else ErrorRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Transaction History Header & Filter Tabs
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transaction History",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("ALL", "EARNED", "SPENT").forEach { filter ->
                            val isSelected = selectedFilter == filter
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) OlibiPurple.copy(alpha = 0.3f) else DarkSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    0.5.dp,
                                    if (isSelected) OlibiPurpleLight else Color.Transparent
                                ),
                                modifier = Modifier.clickable { selectedFilter = filter }
                            ) {
                                Text(
                                    text = filter,
                                    color = if (isSelected) OlibiCyanAccent else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Transactions list
        if (filteredTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No transactions found in this category",
                        color = TextTertiary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(filteredTransactions) { tx ->
                val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
                val timeStr = dateFormat.format(Date(tx.timestamp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkSurfaceHighlight, RoundedCornerShape(14.dp))
                        .testTag("tx_${tx.id}")
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
                                color = if (tx.isEarned) SuccessGreen.copy(alpha = 0.15f) else ErrorRed.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (tx.isEarned) SuccessGreen else ErrorRed
                                )
                            ) {
                                Icon(
                                    imageVector = if (tx.isEarned) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                    contentDescription = if (tx.isEarned) "Earned" else "Spent",
                                    tint = if (tx.isEarned) SuccessGreen else ErrorRed,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = tx.title,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$timeStr • ${tx.description}",
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        Text(
                            text = if (tx.isEarned) "+${tx.amount}" else "-${tx.amount}",
                            color = if (tx.isEarned) SuccessGreen else ErrorRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
