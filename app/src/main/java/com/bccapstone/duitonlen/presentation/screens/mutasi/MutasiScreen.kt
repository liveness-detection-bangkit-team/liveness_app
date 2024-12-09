package com.bccapstone.duitonlen.presentation.screens.mutasi

import android.R.attr.top
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bccapstone.duitonlen.presentation.theme.DuitOnlenTheme

class MutasiScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DuitOnlenTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),

                ) {
                    MutasiScreenContainer()
                }
            }
        }
    }
}

@Composable
fun MutasiScreenContainer() {
    Scaffold(
        bottomBar = {
            BottomNavigation(selectedItem = "Mutasi")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues) // Gunakan padding dari Scaffold
        ) {
            TopBar()
            Spacer(modifier = Modifier.height(20.dp))
            DateLabel("14 Nov 2024")
            Spacer(modifier = Modifier.height(10.dp))
            TransactionBox(
                title = "Top Up Dana 08xxxxxxxx04",
                amount = "- Rp26.700,00",
                status = "Berhasil",
                time = "21:29:26 WIB"
            )
            TransactionBox(
                title = "Transfer Dari Budi",
                amount = "+ Rp120.000,00",
                status = "Berhasil",
                time = "19:22:01 WIB"
            )
            Spacer(modifier = Modifier.height(10.dp))
            DateLabel("13 Nov 2024")
            Spacer(modifier = Modifier.height(10.dp))
            TransactionBox(
                title = "Pembayan Qris ",
                amount = "- Rp15.190,00",
                status = "Berhasil",
                time = "17:02:12 WIB"
            )
            TransactionBox(
                title = "Transfer Ke Budi",
                amount = "- Rp1.000.000,00",
                status = "Berhasil",
                time = "12:15:35 WIB"
            )
            TransactionBox(
                title = "Biaya SMS Notifikasi Sejumlah " +
                        "5 Notifikasi",
                amount = "- Rp3.750,00",
                status = "Berhasil",
                time = "01:00:00 WIB"
            )
        }
    }
}


@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .zIndex(1f)
            .background(
                Color.Red,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Mutasi",
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
fun DateLabel(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(vertical = 16.dp, horizontal = 16.dp,)
    ) {
        Text(
            text = date,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}


@Composable
fun TransactionBox(
    title: String,
    amount: String,
    status: String,
    time: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = amount,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (amount.startsWith("+")) Color(0xFF1F8F4F) else Color(0xFFD32F2F) // Hijau/merah
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = status,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = time,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BottomNavigation(selectedItem: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp) // Memberikan ruang untuk scan button yang menonjol ke atas
    ) {
        // Card navigation yang berada di bawah (z-index lebih rendah)
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
        // Scan button yang berada di atas (z-index lebih tinggi)
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
            color = tint,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal // Bold jika dipilih
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMutasiScreen() {
    DuitOnlenTheme {
        MutasiScreenContainer()
    }
}