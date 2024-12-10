package com.bccapstone.duitonlen.ui.screen


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bccapstone.duitonlen.ui.theme.DuitOnlenTheme
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview

class HomePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DuitOnlenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EWalletScreen()
                }
            }
        }
    }
}

@Composable
fun EWalletScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        // TopBar
                        Box(
                            modifier = Modifier

                        ) {
                            TopBar()
                        }

                        // BalanceCard yang overlap dengan TopBar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 75.dp)
                        ) {
                            BalanceCard()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .offset(y = (-30).dp)  // Nilai negatif untuk efek overlap ke atas
                    ) {
                        Column {
                            MenuButtons()
                            PaymentSection()
                            FinancialRecords()
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }

            // Bottom Navigation dengan fixed posisition di bawah
            BottomNavigation()
        }
    }
}

@Composable
fun TopBar() {
    // Red curved background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                color = Color.Red,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hi,",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "User",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White
            )
        }
    }
}


@Composable
fun BalanceCard() {
    var isVisible by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Saldo",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isVisible) "Rp13.500" else "Rp●●●●●",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { isVisible = !isVisible }
                ) {
                    Icon(
                        imageVector = if (isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = "Toggle visibility",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
        }
    }
}

@Composable
fun MenuButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(65.dp)
                .background(
                    color = Color(0xFFFF9046),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(27.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = text,
            fontSize = 15.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MenuButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MenuButton(
            icon = Icons.Default.Add,
            text = "Top Up",
            onClick = { /* Handle click */ }
        )
        MenuButton(
            icon = Icons.Default.Download,
            text = "Tarik",
            onClick = { /* Handle click */ }
        )
        MenuButton(
            icon = Icons.Default.SwapHoriz,
            text = "Kirim",
            onClick = { /* Handle click */ }
        )
        MenuButton(
            icon = Icons.Default.CropFree,
            text = "Scan",
            onClick = { /* Handle click */ }
        )
    }
}


@Composable
fun PaymentSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pembayaran",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "lihat semua",
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = Color(0xFFFF9046),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color.White,
                        modifier = Modifier.size(27.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gratis s.d 25rb Coin",
                        fontSize = 14.sp,
                        color = Color.Black

                    )
                    Box(
                        modifier = Modifier
                            .height(25.dp)
                            .width(80.dp)
                            .background(
                                color = Color(0xFFFF9046),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Claim",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(
                color = Color.Gray,
                thickness = 0.5.dp
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShapeIcon(
                    icon = Icons.Default.Phone,
                    text = "Phone"
                ) { /* Handle click */ }
                ShapeIcon(
                    icon = Icons.Default.Tv,
                    text = "TV"
                ) { /* Handle click */ }
                ShapeIcon(
                    icon = Icons.Default.Wifi,
                    text = "Wifi"
                ) { /* Handle click */ }
                ShapeIcon(
                    icon = Icons.Default.Wifi,
                    text = "Wifi"
                ) { /* Handle click */ }
                ShapeIcon(
                    icon = Icons.Default.MoreHoriz,
                    text = "More"
                ) { /* Handle click */ }
            }
        }
    }
}

@Composable
fun ShapeIcon(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(55.dp)
            .background(
                color = Color(0xFFFF9046),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(27.dp)
        )
    }
}


@Composable
fun FinancialRecords() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Catatan Keuangan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "sembunyikan",
                        fontSize = 12.sp,
                    )
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Toggle visibility",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                text = "1 Nov 2024 - 30 Nov 2024",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .height(80.dp)
                        .width(162.dp)
                        .background(
                            color = Color(0xFFFF9046),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Pemasukan",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Rp4.109.543",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .height(80.dp)
                        .width(162.dp)
                        .background(
                            color = Color(0xFFFF9046),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Pengeluaran",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Rp3.663.285",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            }
            Row (
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                ) {
//                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selisih",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Rp446.258",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Text(
                    text = "lihat semua",
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
fun BottomNavigation() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomNavItem(
                    text = "Beranda",
                    icon = Icons.Default.Home,
                    isSelected = true,
                    tint = Color.Red
                )
                BottomNavItem(
                    text = "Mutasi",
                    icon = Icons.Default.Description,
                    isSelected = false,
                    tint = Color.Gray
                )
                // Spacer untuk ikon Scan di tengah
                Spacer(modifier = Modifier.width(48.dp))
                BottomNavItem(
                    text = "Transaksi",
                    icon = Icons.Default.Description,
                    isSelected = false,
                    tint = Color.Gray
                )
                BottomNavItem(
                    text = "Akun",
                    icon = Icons.Default.Person,
                    isSelected = false,
                    tint = Color.Gray
                )
            }
        }

        // Ikon Scan yang overlap dengan BottomNavigation
        Card(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-8).dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Red),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CropFree,
                    contentDescription = "Scan",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    tint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            color = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CobaPreview() {
    DuitOnlenTheme {
        EWalletScreen()
    }
}