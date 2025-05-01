package com.example.learnjapanese.data.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

// Enum cho trạng thái cuộc gọi
enum class CallState {
    RINGING,    // Đang đổ chuông
    CONNECTING, // Đang kết nối
    CONNECTED,  // Đã kết nối
    ENDED       // Đã kết thúc
}

// Model cho một liên hệ/bạn bè
data class Contact(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val avatar: String? = null,
    val phoneNumber: String? = null,
    val username: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: String? = null,
    val isFavorite: Boolean = false,
    val color: Color? = null
)

// Model cho một cuộc gọi
data class CallSession(
    val id: String = UUID.randomUUID().toString(),
    val contact: Contact,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val state: CallState = CallState.RINGING,
    val duration: Long? = null,
    val isVideoEnabled: Boolean = false,
    val isMicrophoneEnabled: Boolean = true,
    val isSpeakerEnabled: Boolean = false,
    val isIncoming: Boolean = false
)

// Nhóm danh bạ
data class ContactGroup(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val contacts: List<Contact> = emptyList()
)

// Danh sách liên hệ mẫu
fun getSampleContacts(): List<Contact> {
    return listOf(
        Contact(
            id = "c1",
            name = "Tanaka Yuki",
            username = "tanaka_sensei",
            isOnline = true,
            isFavorite = true,
            lastSeen = "Đang hoạt động"
        ),
        Contact(
            id = "c2",
            name = "Sato Airi",
            username = "airi_sato",
            isOnline = false,
            lastSeen = "1 giờ trước"
        ),
        Contact(
            id = "c3",
            name = "Yamada Kenji",
            username = "kenji_yamada",
            isOnline = true,
            isFavorite = true,
            lastSeen = "Đang hoạt động"
        ),
        Contact(
            id = "c4",
            name = "Nakamura Hana",
            username = "hana_chan",
            isOnline = false,
            lastSeen = "2 giờ trước"
        ),
        Contact(
            id = "c5",
            name = "Suzuki Takeshi",
            username = "suzuki_takeshi",
            isOnline = true,
            lastSeen = "Đang hoạt động"
        ),
        Contact(
            id = "c6",
            name = "Watanabe Emi",
            username = "emi_watanabe",
            isOnline = false,
            lastSeen = "Hôm qua"
        ),
        Contact(
            id = "c7",
            name = "Kobayashi Ryo",
            username = "ryo_koba",
            isOnline = false,
            lastSeen = "5 giờ trước"
        ),
        Contact(
            id = "c8",
            name = "Ito Yumi",
            username = "yumi_ito",
            isOnline = true,
            lastSeen = "Đang hoạt động"
        ),
        Contact(
            id = "c9",
            name = "Kato Takuya",
            username = "takuya_kato",
            isOnline = false,
            lastSeen = "2 ngày trước"
        ),
        Contact(
            id = "c10",
            name = "Saito Mei",
            username = "mei_saito",
            isOnline = false,
            lastSeen = "1 tuần trước",
            isFavorite = true
        )
    )
}

// Lấy danh bạ theo ID
fun getContactById(id: String): Contact? {
    return getSampleContacts().find { it.id == id }
}

// Lấy danh sách cuộc gọi gần đây
fun getRecentCalls(): List<CallSession> {
    val contacts = getSampleContacts()
    return listOf(
        CallSession(
            id = "call1",
            contact = contacts[0],
            startTime = System.currentTimeMillis() - 3600000, // 1 giờ trước
            endTime = System.currentTimeMillis() - 3550000,
            state = CallState.ENDED,
            duration = 600000, // 10 phút
            isIncoming = false
        ),
        CallSession(
            id = "call2",
            contact = contacts[2],
            startTime = System.currentTimeMillis() - 86400000, // 1 ngày trước
            endTime = System.currentTimeMillis() - 86390000,
            state = CallState.ENDED,
            duration = 300000, // 5 phút
            isIncoming = true
        ),
        CallSession(
            id = "call3",
            contact = contacts[5],
            startTime = System.currentTimeMillis() - 172800000, // 2 ngày trước
            endTime = System.currentTimeMillis() - 172790000,
            state = CallState.ENDED,
            duration = 120000, // 2 phút
            isIncoming = false
        ),
        CallSession(
            id = "call4",
            contact = contacts[7],
            startTime = System.currentTimeMillis() - 259200000, // 3 ngày trước
            endTime = System.currentTimeMillis() - 259195000,
            state = CallState.ENDED,
            duration = 180000, // 3 phút
            isIncoming = true
        ),
        CallSession(
            id = "call5",
            contact = contacts[9],
            startTime = System.currentTimeMillis() - 345600000, // 4 ngày trước
            endTime = System.currentTimeMillis() - 345590000,
            state = CallState.ENDED,
            duration = 240000, // 4 phút
            isIncoming = false
        )
    )
}

// Lấy danh sách nhóm liên hệ
fun getContactGroups(): List<ContactGroup> {
    val allContacts = getSampleContacts()
    val favoriteContacts = allContacts.filter { it.isFavorite }
    val onlineContacts = allContacts.filter { it.isOnline }
    
    return listOf(
        ContactGroup(
            id = "g1",
            name = "Yêu thích",
            contacts = favoriteContacts
        ),
        ContactGroup(
            id = "g2",
            name = "Đang hoạt động",
            contacts = onlineContacts
        ),
        ContactGroup(
            id = "g3",
            name = "Tất cả liên hệ",
            contacts = allContacts
        )
    )
} 