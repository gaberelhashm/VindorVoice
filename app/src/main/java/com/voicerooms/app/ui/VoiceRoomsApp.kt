package com.voicerooms.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AppRole { USER, HOST, AGENT, ADMIN }
enum class AuthMethod { GOOGLE, FACEBOOK, PHONE }

data class AppUser(
    val id: String,
    val name: String,
    val email: String,
    val role: AppRole,
    val status: String = "نشط"
)

data class VoiceRoom(
    val id: String,
    val name: String,
    val description: String,
    val speakers: Int,
    val listeners: Int,
    val isLive: Boolean = true,
    val category: String = "عام"
)

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRoomsApp() {
    var selectedRole by remember { mutableStateOf(AppRole.HOST) }
    var authenticated by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val user = remember {
        AppUser(
            id = "uv-1001",
            name = "أحمد محمد",
            email = "ahmed@vindorvoice.app",
            role = selectedRole,
            status = "نشط"
        )
    }

    if (!authenticated) {
        AuthScreen(
            selectedRole = selectedRole,
            onRoleChanged = { selectedRole = it },
            onLogin = {
                authenticated = true
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "VindorVoice",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "إشعارات")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "بحث")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                val items = listOf(
                    "الغرف" to Icons.Filled.Home,
                    "المحادثات" to Icons.Filled.Chat,
                    "الإدارة" to Icons.Filled.AdminPanelSettings,
                    "الملف" to Icons.Filled.Person
                )

                items.forEachIndexed { index, (label, icon) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(if (selectedTab == index) icon else when (index) {
                                0 -> Icons.Outlined.Home
                                1 -> Icons.Outlined.Chat
                                2 -> Icons.Outlined.AdminPanelSettings
                                else -> Icons.Outlined.Person
                            }, contentDescription = label)
                        },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> RoomsScreen(user = user)
                1 -> ChatScreen()
                2 -> if (user.role == AppRole.ADMIN || user.role == AppRole.HOST) AdminScreen() else ProfileScreen(user)
                3 -> ProfileScreen(user)
            }
        }
    }
}

@Composable
fun AuthScreen(
    selectedRole: AppRole,
    onRoleChanged: (AppRole) -> Unit,
    onLogin: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(54.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "VindorVoice",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "أصواتك، أدوارك، مجتمعاتك",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "اختر نوع الحساب",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(AppRole.USER, AppRole.HOST, AppRole.AGENT, AppRole.ADMIN).forEach { role ->
                    val selected = selectedRole == role
                    FilterChip(
                        selected = selected,
                        onClick = { onRoleChanged(role) },
                        label = {
                            Text(
                                text = when (role) {
                                    AppRole.USER -> "مستخدم"
                                    AppRole.HOST -> "مضيف"
                                    AppRole.AGENT -> "وكيل"
                                    AppRole.ADMIN -> "مدير"
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            SocialLoginButton(
                label = "الدخول عبر Google",
                icon = Icons.Default.AccountCircle,
                onClick = onLogin
            )
            Spacer(modifier = Modifier.height(10.dp))
            SocialLoginButton(
                label = "الدخول عبر Facebook",
                icon = Icons.Default.Groups,
                onClick = onLogin
            )
            Spacer(modifier = Modifier.height(10.dp))
            SocialLoginButton(
                label = "الدخول برقم الهاتف",
                icon = Icons.Default.Phone,
                onClick = onLogin
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("تسجيل الدخول", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().height(54.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun RoomsScreen(user: AppUser) {
    val rooms = remember {
        listOf(
            VoiceRoom("1", "غرفة المذاكرة", "جلسة دراسة وهدوء", 4, 140, true, "تعليم"),
            VoiceRoom("2", "صوت الليل", "دردشة وموسيقى", 6, 220, true, "ترفيه"),
            VoiceRoom("3", "مهرجان الألعاب", "ألعاب ومنافسات", 7, 310, true, "ألعاب"),
            VoiceRoom("4", "منتدى المطورين", "برمجة وكود", 5, 180, true, "تقنية"),
            VoiceRoom("5", "جلسة الحكايات", "قصص ومشاركة", 3, 96, true, "ثقافة")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("مرحباً ${user.name}", fontWeight = FontWeight.Bold)
                    Text("الدور: ${when (user.role) { AppRole.USER -> "مستخدم"; AppRole.HOST -> "مضيف"; AppRole.AGENT -> "وكيل"; AppRole.ADMIN -> "مدير" }}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("إنشاء غرفة جديدة")
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "الغرف النشطة",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(rooms) { room ->
                RoomCard(room)
            }
        }
    }
}

@Composable
fun RoomCard(room: VoiceRoom) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = { }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(room.name, fontWeight = FontWeight.Bold)
                        Text(room.category, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }

                if (room.isLive) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF4ADE80).copy(alpha = 0.2f)) {
                        Text(
                            text = "مباشر",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color(0xFF4ADE80),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = room.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${room.speakers} متحدث", fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${room.listeners} مستمع", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ChatScreen() {
    val messages = remember {
        listOf(
            ChatMessage("1", "أحمد", "أهلاً بكم في الغرفة 👋", "10:30"),
            ChatMessage("2", "سارة", "من عنده فكرة للغرفة القادمة؟", "10:31"),
            ChatMessage("3", "محمد", "أنا جاهز للمشاركة", "10:32")
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("الدردشة العامة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(messages) { msg -> ChatBubble(msg) }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                placeholder = { Text("اكتب رسالة...") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(onClick = { }, modifier = Modifier.size(48.dp), containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Send, contentDescription = "إرسال")
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(message.sender.first().toString(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(message.sender, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(message.time, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(message.text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        }
    }
}

@Composable
fun AdminScreen() {
    val stats = listOf(
        "154" to "مستخدم",
        "22" to "مضيف",
        "08" to "وكيل",
        "95%" to "نسبة نشاط"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("لوحة الإدارة", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    stats.forEach { (value, label) ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Text("إدارة المستخدمين", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(listOf(
                "أحمد محمد" to "مضيف",
                "سارة أحمد" to "وكيل",
                "ليلى علي" to "مستخدم",
                "يوسف علي" to "مدير"
            )) { (name, role) ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text("الدور: $role", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        AssistChip(onClick = { }, label = { Text("تعديل") })
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(user: AppUser) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(user.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(user.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("الحالة: ${user.status}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatItem("12", "غرف")
            StatItem("48", "ساعات")
            StatItem("5", "أصدقاء")
        }

        Spacer(modifier = Modifier.height(32.dp))

        ProfileMenuItem(Icons.Default.Settings, "الإعدادات")
        ProfileMenuItem(Icons.Default.Star, "VIP")
        ProfileMenuItem(Icons.Default.Info, "المساعدة")
        ProfileMenuItem(Icons.Default.QrCode, "معرف الحساب")
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = { }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

