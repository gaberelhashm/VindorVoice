package com.voicerooms.app.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF7C3AED)
private val PurpleDark = Color(0xFF3B0F65)
private val Gold = Color(0xFFFFC857)
private val Green = Color(0xFF45D483)
private val Blue = Color(0xFF63A4FF)
private val Rose = Color(0xFFFF7BBF)

enum class AppRole { USER, HOST, AGENT, ADMIN }

data class AppUser(val id: String, val name: String, val email: String, val role: AppRole, val status: String = "نشط")
data class VoiceRoom(val id: String, val name: String, val description: String, val speakers: Int, val listeners: Int, val category: String, val color: Color)
data class ChatMessage(val id: String, val sender: String, val text: String, val time: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRoomsApp() {
    var loggedIn by remember { mutableStateOf(false) }
    var tab by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf("الكل") }
    val user = remember { AppUser("uv-1001", "أحمد محمد", "ahmed@vindorvoice.app", AppRole.HOST) }

    if (!loggedIn) {
        AuthScreen(onLogin = { loggedIn = true })
        return
    }

    val labels = listOf("الرئيسية", "الرسائل", "النشاط", "حسابي")
    val icons = listOf(Icons.Default.Home, Icons.Default.Chat, Icons.Default.Notifications, Icons.Default.Person)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("VindorVoice", fontWeight = FontWeight.ExtraBold)
                        Text("مجتمعك الصوتي", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, "بحث") }
                    IconButton(onClick = {}) { Icon(Icons.Default.NotificationsNone, "الإشعارات") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                labels.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = { Icon(icons[index], label) },
                        label = { Text(label, fontSize = 10.sp) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (tab) {
                0 -> HomeScreen(user, selectedCategory, { selectedCategory = it })
                1 -> ChatScreen()
                2 -> ActivityScreen()
                else -> ProfileScreen(user)
            }
        }
    }
}

@Composable
private fun AuthScreen(onLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF12091F),
                        Color(0xFF2B0F47),
                        Color(0xFF6E2AD1)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = .12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Mic, null, Modifier.size(58.dp), tint = Color.White)
            }

            Spacer(Modifier.height(20.dp))
            Text("VindorVoice", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
            Text("صوتك يصل إلى مجتمعك", color = Color.White.copy(alpha = .8f), fontSize = 16.sp)

            Spacer(Modifier.height(32.dp))
            AuthButton("الدخول بحساب Google", Icons.Default.AccountCircle, onLogin)
            Spacer(Modifier.height(12.dp))
            AuthButton("الدخول بحساب Facebook", Icons.Default.Groups, onLogin)
            Spacer(Modifier.height(12.dp))
            AuthButton("الدخول برقم الهاتف", Icons.Default.Phone, onLogin)

            Spacer(Modifier.height(22.dp))
            Text(
                "بتسجيل الدخول أنت توافق على شروط الاستخدام وسياسة الخصوصية",
                color = Color.White.copy(alpha = .66f),
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 18.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun AuthButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF24112F)
        )
    ) {
        Icon(icon, null)
        Spacer(Modifier.width(10.dp))
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun HomeScreen(user: AppUser, selectedCategory: String, onCategoryChange: (String) -> Unit) {
    val categories = listOf("الكل", "شعبي", "موسيقى", "ألعاب", "تعلم", "أصدقاء")
    val rooms = remember {
        listOf(
            VoiceRoom("1", "سهرة الأصدقاء", "موسيقى وضحك ودردشة مفتوحة", 8, 1240, "شعبي", Color(0xFF9B5DE5)),
            VoiceRoom("2", "ليالي عربية", "أجمل الأغاني والطلبات", 6, 860, "موسيقى", Color(0xFFF15BB5)),
            VoiceRoom("3", "Arena Games", "تحديات وألعاب جماعية", 10, 642, "ألعاب", Color(0xFF00BBF9)),
            VoiceRoom("4", "تعلم الإنجليزية", "تحدث وتعلم مع أصدقاء جدد", 4, 318, "تعلم", Color(0xFF00F5D4)),
            VoiceRoom("5", "لمة الأصحاب", "تعرف على أشخاص جدد", 5, 207, "أصدقاء", Color(0xFFFF9F1C))
        )
    }

    val shown = if (selectedCategory == "الكل") rooms else rooms.filter { it.category == selectedCategory }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { WelcomeCard(user) }
        item { QuickActions() }
        item {
            Text("اكتشف الغرف", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategoryChange(category) },
                        label = { Text(category) }
                    )
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("مباشر الآن", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("عرض الكل", color = Purple, fontSize = 13.sp)
            }
        }
        items(shown) { room -> RoomCard(room) }
    }
}

@Composable
private fun WelcomeCard(user: AppUser) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Purple),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("أهلاً ${user.name} 👋", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(6.dp))
                Text("اكتشف أشخاصاً واهتمامات جديدة اليوم", color = Color.White.copy(alpha = .8f), fontSize = 13.sp)
                Spacer(Modifier.height(14.dp))
                AssistChip(
                    onClick = {},
                    label = { Text("مضيف نشط") },
                    leadingIcon = { Icon(Icons.Default.Verified, null) }
                )
            }
            Box(
                Modifier.size(78.dp).clip(CircleShape).background(Color.White.copy(alpha = .16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Mic, null, Modifier.size(42.dp), tint = Color.White)
            }
        }
    }
}

@Composable
private fun QuickActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ActionCard("إنشاء غرفة", Icons.Default.Add, Modifier.weight(1f))
        ActionCard("دعوة أصدقاء", Icons.Default.PersonAdd, Modifier.weight(1f))
        ActionCard("المتجر", Icons.Default.ShoppingBag, Modifier.weight(1f))
    }
}

@Composable
private fun ActionCard(text: String, icon: ImageVector, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = Purple)
            Spacer(Modifier.height(6.dp))
            Text(text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun RoomCard(room: VoiceRoom) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
        onClick = {}
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(54.dp).clip(RoundedCornerShape(16.dp)).background(room.color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Mic, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(room.name, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    Text(room.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Green.copy(alpha = .16f)
                ) {
                    Text("مباشر", Modifier.padding(horizontal = 8.dp, vertical = 5.dp), color = Green, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(minOf(room.speakers, 5)) { index ->
                    Box(
                        Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(room.color.copy(alpha = .7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${index + 1}", color = Color.White, fontSize = 10.sp)
                    }
                    if (index < minOf(room.speakers, 5) - 1) {
                        Spacer(Modifier.width(2.dp))
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text("${room.speakers} متحدث", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(14.dp))
                Icon(Icons.Default.Visibility, null, Modifier.size(15.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(4.dp))
                Text("${room.listeners}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, null, tint = Purple)
            }
        }
    }
}

@Composable
private fun ChatScreen() {
    val messages = remember {
        listOf(
            ChatMessage("1", "سارة", "أهلاً بالجميع 👋", "10:31"),
            ChatMessage("2", "محمد", "مين داخل غرفة السهرة؟", "10:32"),
            ChatMessage("3", "ليلى", "أنا موجودة الآن", "10:33")
        )
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("الرسائل", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        Text("تواصل مع أصدقائك ومجتمعك", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        Spacer(Modifier.height(18.dp))

        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { MessageRow(it) }
        }

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("اكتب رسالة...") },
            shape = RoundedCornerShape(22.dp),
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Send, "إرسال")
                }
            }
        )
    }
}

@Composable
private fun MessageRow(message: ChatMessage) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(46.dp).clip(CircleShape).background(Purple.copy(alpha = .2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(message.sender.take(1), color = Purple, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row {
                Text(message.sender, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text(message.time, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(message.text, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ActivityScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("النشاط والإشعارات", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(18.dp))
        listOf(
            "حصلت على هدية جديدة 🎁",
            "تمت دعوتك إلى غرفة سهرة الأصدقاء",
            "لديك متابع جديد",
            "الغرفة التي تتابعها بدأت الآن"
        ).forEach { item ->
            Card(
                Modifier.fillMaxWidth().padding(bottom = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, null, tint = Purple)
                    Spacer(Modifier.width(12.dp))
                    Text(item)
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(user: AppUser) {
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(14.dp))
        Box(
            Modifier.size(104.dp).clip(CircleShape).background(
                Brush.linearGradient(listOf(Purple, Color(0xFFB86BFF)))
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, Modifier.size(58.dp), tint = Color.White)
        }

        Spacer(Modifier.height(12.dp))
        Text(user.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        Text("ID: ${user.id}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Spacer(Modifier.height(20.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Stat("12", "الغرف")
            Stat("486", "المتابعون")
            Stat("4.8K", "الإعجابات")
        }

        Spacer(Modifier.height(24.dp))
        listOf(
            Icons.Default.Settings to "الإعدادات",
            Icons.Default.Star to "VIP والهدايا",
            Icons.Default.QrCode to "مشاركة معرفي",
            Icons.Default.Help to "المساعدة"
        ).forEach { (icon, text) ->
            ProfileItem(icon, text)
        }
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Purple, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProfileItem(icon: ImageVector, text: String) {
    Card(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = {}
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Purple)
            Spacer(Modifier.width(14.dp))
            Text(text, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
