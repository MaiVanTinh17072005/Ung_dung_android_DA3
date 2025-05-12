package com.example.learnjapanese.screens.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tạm thời bỏ qua HiltViewModel để có thể khởi tạo trực tiếp
class ReadingViewModel : ViewModel() {
    // Danh sách bài đọc
    private val _readingList = MutableStateFlow<List<Reading>>(emptyList())
    val readingList: StateFlow<List<Reading>> = _readingList.asStateFlow()
    
    // Chi tiết bài đọc hiện tại
    private val _currentReading = MutableStateFlow<ReadingDetail?>(null)
    val currentReading: StateFlow<ReadingDetail?> = _currentReading.asStateFlow()
    
    init {
        // Giả lập việc tải danh sách bài đọc
        fetchReadingList()
    }
    
    // Lấy danh sách bài đọc
    private fun fetchReadingList() {
        viewModelScope.launch {
            // Giả lập delay mạng
            delay(1000)
            _readingList.value = getSampleReadingList()
        }
    }
    
    // Tải chi tiết bài đọc
    fun loadReadingDetail(readingId: String) {
        viewModelScope.launch {
            // Reset trạng thái hiện tại và giả lập delay mạng
            _currentReading.value = null
            delay(1000)
            _currentReading.value = getSampleReadingDetail(readingId)
        }
    }
    
    // Dữ liệu mẫu cho danh sách bài đọc
    private fun getSampleReadingList(): List<Reading> {
        return listOf(
            Reading(
                id = "1",
                title = "東京での一日",
                summary = "Một ngày ở Tokyo - Câu chuyện về một ngày khám phá các địa điểm nổi tiếng ở thủ đô Nhật Bản",
                level = "N4",
                isBookmarked = true,
                imageUrl = "https://images.unsplash.com/photo-1536098561742-ca998e48cbcc?q=80&w=2036&auto=format&fit=crop"
            ),
            Reading(
                id = "2",
                title = "日本の四季",
                summary = "Bốn mùa ở Nhật Bản - Đặc điểm và vẻ đẹp riêng biệt của từng mùa trong năm",
                level = "N5",
                isBookmarked = false,
                imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?q=80&w=1470&auto=format&fit=crop"
            ),
            Reading(
                id = "3",
                title = "和食の魅力",
                summary = "Sức hấp dẫn của ẩm thực Nhật Bản - Tìm hiểu về các món ăn truyền thống và triết lý đằng sau chúng",
                level = "N4",
                isBookmarked = false,
                imageUrl = "https://images.unsplash.com/photo-1580822184713-fc5400e7fe10?q=80&w=1374&auto=format&fit=crop"
            ),
            Reading(
                id = "4",
                title = "富士山への旅",
                summary = "Hành trình đến núi Phú Sĩ - Trải nghiệm leo núi và khám phá vẻ đẹp của biểu tượng quốc gia Nhật Bản",
                level = "N3",
                isBookmarked = true,
                imageUrl = "https://images.unsplash.com/photo-1578271887552-5ac3a72752bc?q=80&w=1469&auto=format&fit=crop"
            ),
            Reading(
                id = "5",
                title = "京都の古い寺院",
                summary = "Những ngôi đền cổ ở Kyoto - Khám phá lịch sử và kiến trúc độc đáo của các di sản văn hóa",
                level = "N3",
                isBookmarked = false,
                imageUrl = "https://images.unsplash.com/photo-1493780474015-ba834fd0ce2f?q=80&w=1442&auto=format&fit=crop"
            ),
            Reading(
                id = "6",
                title = "日本のポップカルチャー",
                summary = "Văn hóa đại chúng Nhật Bản - Tìm hiểu về anime, manga và các xu hướng văn hóa hiện đại",
                level = "N3",
                isBookmarked = false,
                imageUrl = "https://images.unsplash.com/photo-1542051841857-5f90071e7989?q=80&w=1470&auto=format&fit=crop"
            ),
            Reading(
                id = "7",
                title = "日本の教育システム",
                summary = "Hệ thống giáo dục Nhật Bản - Cấu trúc và đặc điểm của nền giáo dục từ tiểu học đến đại học",
                level = "N2",
                isBookmarked = false,
                imageUrl = null
            )
        )
    }
    
    // Dữ liệu mẫu cho chi tiết bài đọc
    private fun getSampleReadingDetail(readingId: String): ReadingDetail {
        return when (readingId) {
            "1" -> ReadingDetail(
                id = "1",
                title = "東京での一日",
                summary = "Một ngày ở Tokyo - Câu chuyện về một ngày khám phá các địa điểm nổi tiếng ở thủ đô Nhật Bản",
                level = "N4",
                wordCount = 320,
                content = "私は先週の日曜日に東京を観光しました。朝早く起きて、まず浅草寺に行きました。そこには多くの観光客がいました。雷門の大きな赤い提灯を見て、とても感動しました。次に、スカイツリーに登りました。東京の街が一望できました。とても美しい景色でした。お昼には、浅草で有名な天ぷらを食べました。美味しかったです。午後は秋葉原に行きました。多くの電気店やアニメショップがありました。その後、渋谷に行って、有名な交差点を見ました。とても多くの人が一度に道路を渡るのを見て、驚きました。最後に新宿で夕食を食べました。一日中歩き回ったので、とても疲れましたが、東京での一日は本当に楽しかったです。",
                sentences = listOf(
                    Sentence(
                        id = "1_1",
                        text = "私は先週の日曜日に東京を観光しました。",
                        translation = "Tôi đã đi tham quan Tokyo vào chủ nhật tuần trước."
                    ),
                    Sentence(
                        id = "1_2",
                        text = "朝早く起きて、まず浅草寺に行きました。",
                        translation = "Tôi dậy sớm và đi đến chùa Asakusa trước tiên."
                    ),
                    Sentence(
                        id = "1_3",
                        text = "そこには多くの観光客がいました。",
                        translation = "Ở đó có rất nhiều du khách."
                    ),
                    Sentence(
                        id = "1_4",
                        text = "雷門の大きな赤い提灯を見て、とても感動しました。",
                        translation = "Tôi đã rất xúc động khi nhìn thấy chiếc đèn lồng đỏ lớn ở cổng Kaminarimon."
                    ),
                    Sentence(
                        id = "1_5",
                        text = "次に、スカイツリーに登りました。",
                        translation = "Tiếp theo, tôi đã leo lên Tokyo Skytree."
                    ),
                    Sentence(
                        id = "1_6",
                        text = "東京の街が一望できました。",
                        translation = "Tôi có thể nhìn toàn cảnh thành phố Tokyo."
                    ),
                    Sentence(
                        id = "1_7",
                        text = "とても美しい景色でした。",
                        translation = "Đó là một khung cảnh rất đẹp."
                    ),
                    Sentence(
                        id = "1_8",
                        text = "お昼には、浅草で有名な天ぷらを食べました。",
                        translation = "Vào buổi trưa, tôi đã ăn tempura nổi tiếng ở Asakusa."
                    ),
                    Sentence(
                        id = "1_9",
                        text = "美味しかったです。",
                        translation = "Nó rất ngon."
                    ),
                    Sentence(
                        id = "1_10",
                        text = "午後は秋葉原に行きました。",
                        translation = "Buổi chiều tôi đi đến Akihabara."
                    ),
                    Sentence(
                        id = "1_11",
                        text = "多くの電気店やアニメショップがありました。",
                        translation = "Có nhiều cửa hàng điện tử và cửa hàng anime."
                    ),
                    Sentence(
                        id = "1_12",
                        text = "その後、渋谷に行って、有名な交差点を見ました。",
                        translation = "Sau đó, tôi đi đến Shibuya và xem giao lộ nổi tiếng."
                    ),
                    Sentence(
                        id = "1_13",
                        text = "とても多くの人が一度に道路を渡るのを見て、驚きました。",
                        translation = "Tôi ngạc nhiên khi thấy rất nhiều người cùng băng qua đường một lúc."
                    ),
                    Sentence(
                        id = "1_14",
                        text = "最後に新宿で夕食を食べました。",
                        translation = "Cuối cùng, tôi ăn tối ở Shinjuku."
                    ),
                    Sentence(
                        id = "1_15",
                        text = "一日中歩き回ったので、とても疲れましたが、東京での一日は本当に楽しかったです。",
                        translation = "Tôi rất mệt vì đi bộ cả ngày, nhưng một ngày ở Tokyo thực sự rất vui."
                    )
                )
            )
            "2" -> ReadingDetail(
                id = "2",
                title = "日本の四季",
                summary = "Bốn mùa ở Nhật Bản - Đặc điểm và vẻ đẹp riêng biệt của từng mùa trong năm",
                level = "N5",
                wordCount = 280,
                content = "日本には四季があります。春、夏、秋、冬です。春は三月から五月までです。桜が咲きます。人々は花見をします。夏は六月から八月までです。とても暑いです。多くの祭りがあります。花火大会も人気があります。秋は九月から十一月までです。紅葉がとてもきれいです。空が青くて、食べ物がおいしいです。冬は十二月から二月までです。北海道や東北では雪がたくさん降ります。スキーやスノーボードを楽しむ人も多いです。日本の四季はそれぞれ特徴があり、美しいです。",
                sentences = listOf(
                    Sentence(
                        id = "2_1",
                        text = "日本には四季があります。",
                        translation = "Nhật Bản có bốn mùa."
                    ),
                    Sentence(
                        id = "2_2",
                        text = "春、夏、秋、冬です。",
                        translation = "Đó là xuân, hạ, thu, đông."
                    ),
                    Sentence(
                        id = "2_3",
                        text = "春は三月から五月までです。",
                        translation = "Mùa xuân là từ tháng 3 đến tháng 5."
                    ),
                    Sentence(
                        id = "2_4",
                        text = "桜が咲きます。",
                        translation = "Hoa anh đào nở."
                    ),
                    Sentence(
                        id = "2_5",
                        text = "人々は花見をします。",
                        translation = "Mọi người ngắm hoa."
                    ),
                    Sentence(
                        id = "2_6",
                        text = "夏は六月から八月までです。",
                        translation = "Mùa hè là từ tháng 6 đến tháng 8."
                    ),
                    Sentence(
                        id = "2_7",
                        text = "とても暑いです。",
                        translation = "Trời rất nóng."
                    ),
                    Sentence(
                        id = "2_8",
                        text = "多くの祭りがあります。",
                        translation = "Có nhiều lễ hội."
                    ),
                    Sentence(
                        id = "2_9",
                        text = "花火大会も人気があります。",
                        translation = "Lễ hội pháo hoa cũng rất phổ biến."
                    ),
                    Sentence(
                        id = "2_10",
                        text = "秋は九月から十一月までです。",
                        translation = "Mùa thu là từ tháng 9 đến tháng 11."
                    ),
                    Sentence(
                        id = "2_11",
                        text = "紅葉がとてもきれいです。",
                        translation = "Lá đỏ rất đẹp."
                    ),
                    Sentence(
                        id = "2_12",
                        text = "空が青くて、食べ物がおいしいです。",
                        translation = "Bầu trời xanh và thức ăn ngon."
                    ),
                    Sentence(
                        id = "2_13",
                        text = "冬は十二月から二月までです。",
                        translation = "Mùa đông là từ tháng 12 đến tháng 2."
                    ),
                    Sentence(
                        id = "2_14",
                        text = "北海道や東北では雪がたくさん降ります。",
                        translation = "Tuyết rơi nhiều ở Hokkaido và Tohoku."
                    ),
                    Sentence(
                        id = "2_15",
                        text = "スキーやスノーボードを楽しむ人も多いです。",
                        translation = "Nhiều người thích trượt tuyết và trượt ván tuyết."
                    ),
                    Sentence(
                        id = "2_16",
                        text = "日本の四季はそれぞれ特徴があり、美しいです。",
                        translation = "Bốn mùa ở Nhật Bản đều có đặc điểm riêng và đều đẹp."
                    )
                )
            )
            else -> ReadingDetail(
                id = "3",
                title = "和食の魅力",
                summary = "Sức hấp dẫn của ẩm thực Nhật Bản - Tìm hiểu về các món ăn truyền thống và triết lý đằng sau chúng",
                level = "N4",
                wordCount = 350,
                content = "和食は日本の伝統的な食事です。2013年にユネスコの無形文化遺産に登録されました。和食の基本は「一汁三菜」です。これは、ご飯と味噌汁と三つのおかずという意味です。和食の特徴は、季節の食材を使うことです。春には山菜や筍、夏には鱧や鮎、秋には松茸や栗、冬には河豚や蟹などが人気です。また、見た目も大切で、季節や料理に合わせた器を使います。食材の形や色を生かした盛り付けも美しいです。和食は味だけでなく、五感全てで楽しむ食文化なのです。",
                sentences = listOf(
                    Sentence(
                        id = "3_1",
                        text = "和食は日本の伝統的な食事です。",
                        translation = "Washoku là ẩm thực truyền thống của Nhật Bản."
                    ),
                    Sentence(
                        id = "3_2",
                        text = "2013年にユネスコの無形文化遺産に登録されました。",
                        translation = "Nó đã được đăng ký là di sản văn hóa phi vật thể của UNESCO vào năm 2013."
                    ),
                    Sentence(
                        id = "3_3",
                        text = "和食の基本は「一汁三菜」です。",
                        translation = "Nền tảng của ẩm thực Nhật là \"một canh ba món\"."
                    ),
                    Sentence(
                        id = "3_4",
                        text = "これは、ご飯と味噌汁と三つのおかずという意味です。",
                        translation = "Điều này có nghĩa là cơm, súp miso và ba món ăn kèm."
                    ),
                    Sentence(
                        id = "3_5",
                        text = "和食の特徴は、季節の食材を使うことです。",
                        translation = "Đặc điểm của ẩm thực Nhật là sử dụng nguyên liệu theo mùa."
                    ),
                    Sentence(
                        id = "3_6",
                        text = "春には山菜や筍、夏には鱧や鮎、秋には松茸や栗、冬には河豚や蟹などが人気です。",
                        translation = "Rau rừng và măng vào mùa xuân, cá chình và cá ayu vào mùa hè, nấm matsutake và hạt dẻ vào mùa thu, cá nóc và cua vào mùa đông đều rất phổ biến."
                    ),
                    Sentence(
                        id = "3_7",
                        text = "また、見た目も大切で、季節や料理に合わせた器を使います。",
                        translation = "Ngoài ra, hình thức cũng quan trọng, và họ sử dụng bát đĩa phù hợp với mùa và món ăn."
                    ),
                    Sentence(
                        id = "3_8",
                        text = "食材の形や色を生かした盛り付けも美しいです。",
                        translation = "Cách trình bày tận dụng hình dạng và màu sắc của nguyên liệu cũng rất đẹp."
                    ),
                    Sentence(
                        id = "3_9",
                        text = "和食は味だけでなく、五感全てで楽しむ食文化なのです。",
                        translation = "Ẩm thực Nhật không chỉ là hương vị, mà là văn hóa ẩm thực được thưởng thức bằng tất cả năm giác quan."
                    )
                )
            )
        }
    }
}

// Model cho danh sách bài đọc
data class Reading(
    val id: String,
    val title: String,
    val summary: String,
    val level: String,
    val isBookmarked: Boolean = false,
    val imageUrl: String? = null
)

// Model cho chi tiết bài đọc
data class ReadingDetail(
    val id: String,
    val title: String,
    val summary: String,
    val level: String,
    val wordCount: Int,
    val content: String,
    val sentences: List<Sentence>
)

// Model cho từng câu trong bài đọc
data class Sentence(
    val id: String,
    val text: String,
    val translation: String
) 