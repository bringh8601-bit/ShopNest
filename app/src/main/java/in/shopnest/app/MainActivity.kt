package in.shopnest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF101B35)
private val NavyLight = Color(0xFF1C2B4D)
private val Gold = Color(0xFFD8B15A)
private val Cream = Color(0xFFF7F5F0)
private val Ink = Color(0xFF182238)
private val Muted = Color(0xFF778096)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ShopNestApp() }
    }
}

private data class ShopProduct(val name: String, val price: String, val tag: String, val emoji: String)

@Composable
fun ShopNestApp() {
    val categories = listOf("सभी", "मोबाइल", "फैशन", "जूते", "ग्रोसरी", "इलेक्ट्रॉनिक्स", "होम", "ब्यूटी", "खिलौने")
    val products = listOf(
        ShopProduct("Premium Wireless Headphones", "₹1,499", "बेस्टसेलर", "🎧"),
        ShopProduct("Classic Everyday Sneakers", "₹1,199", "नई पसंद", "👟"),
        ShopProduct("Smart Fitness Watch", "₹2,499", "टॉप डील", "⌚"),
        ShopProduct("Elegant Home Decor Set", "₹899", "लोकप्रिय", "🏡")
    )
    var selectedCategory by remember { mutableStateOf("सभी") }
    var query by remember { mutableStateOf("") }
    var cartCount by remember { mutableIntStateOf(0) }
    var selectedTab by remember { mutableIntStateOf(0) }

    MaterialTheme(colorScheme = lightColorScheme(primary = Navy, secondary = Gold, background = Cream, surface = Color.White)) {
        Scaffold(
            containerColor = Cream,
            bottomBar = {
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("होम") })
                    NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1 }, icon = { Icon(Icons.Default.Category, null) }, label = { Text("कैटेगरी") })
                    NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2 }, icon = { Icon(Icons.Default.FavoriteBorder, null) }, label = { Text("पसंदीदा") })
                    NavigationBarItem(selected = selectedTab == 3, onClick = { selectedTab = 3 }, icon = { Icon(Icons.Default.PersonOutline, null) }, label = { Text("अकाउंट") })
                }
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(bottom = 20.dp)) {
                item {
                    Column(Modifier.background(Navy).padding(horizontal = 18.dp, vertical = 18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(Gold), contentAlignment = Alignment.Center) {
                                Text("S", color = Navy, fontSize = 27.sp, fontWeight = FontWeight.Black)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text("ShopNest", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                                Text("हर खरीदारी, खास अंदाज़ में", color = Gold, fontSize = 11.sp)
                            }
                            BadgedBox(badge = { if (cartCount > 0) Badge { Text(cartCount.toString()) } }) {
                                Icon(Icons.Default.ShoppingBag, "Cart", tint = Color.White, modifier = Modifier.size(27.dp))
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                        OutlinedTextField(
                            value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("आप क्या ढूँढ रहे हैं?", color = Muted) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = Navy) },
                            trailingIcon = { Icon(Icons.Default.Tune, null, tint = Navy) },
                            singleLine = true, shape = RoundedCornerShape(15.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = Gold, unfocusedBorderColor = Color.White)
                        )
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("WELCOME TO SHOPNEST", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                                Text("प्रीमियम डील्स का नया घर", color = Navy, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                                Text("आपकी पसंद, एक ही जगह।", color = Muted, fontSize = 13.sp)
                            }
                            Text("✦", color = Gold, fontSize = 48.sp)
                        }
                        Spacer(Modifier.height(14.dp))
                        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(NavyLight).padding(20.dp)) {
                            Column {
                                Text("SHOPNEST PRIME", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                                Text("बड़ी बचत।\nबेहतरीन पसंद।", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 30.sp)
                                Spacer(Modifier.height(8.dp))
                                Text("चुनिंदा प्रोडक्ट्स पर शानदार ऑफर्स", color = Color.White.copy(alpha = .8f), fontSize = 12.sp)
                                Spacer(Modifier.height(14.dp))
                                Text("EXPLORE NOW  →", color = Navy, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clip(RoundedCornerShape(30.dp)).background(Gold).clickable { selectedTab = 1 }.padding(horizontal = 16.dp, vertical = 10.dp))
                            }
                        }
                    }
                }
                item {
                    Column(Modifier.padding(top = 4.dp)) {
                        SectionTitle("शॉप बाय कैटेगरी", "सभी देखें")
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            items(categories) { category ->
                                val active = selectedCategory == category
                                Text(category, color = if (active) Color.White else Navy,
                                    modifier = Modifier.clip(RoundedCornerShape(30.dp)).background(if (active) Navy else Color.White)
                                        .clickable { selectedCategory = category }.padding(horizontal = 16.dp, vertical = 10.dp),
                                    fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(18.dp)); SectionTitle("आज की खास पसंद", "और देखें") }
                items(products.filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }) { product ->
                    ProductRow(product = product, onAdd = { cartCount++ })
                }
                item {
                    Text("SHOPNEST • आपका भरोसेमंद शॉपिंग साथी", color = Muted, fontSize = 10.sp,
                        modifier = Modifier.fillMaxWidth().padding(18.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, action: String) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(action, color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProductRow(product: ShopProduct, onAdd: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clip(RoundedCornerShape(18.dp)).background(Color.White).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(82.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFF1EFE9)), contentAlignment = Alignment.Center) {
            Text(product.emoji, fontSize = 36.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(product.tag, color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(product.name, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text(product.price, color = Navy, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
        IconButton(onClick = onAdd, modifier = Modifier.clip(CircleShape).background(Navy)) {
            Icon(Icons.Default.Add, "Add to cart", tint = Gold)
        }
    }
}
