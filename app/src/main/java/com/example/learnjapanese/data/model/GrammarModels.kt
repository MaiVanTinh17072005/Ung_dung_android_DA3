package com.example.learnjapanese.data.model

// Model cho mục ngữ pháp
data class GrammarItem(
    val id: String,
    val title: String,
    val level: String,
    val summary: String,
    val explanation: String,
    val examples: List<GrammarExample>,
    val videoUrl: String? = null,
    val isLearned: Boolean = false
)

// Model cho ví dụ câu của ngữ pháp
data class GrammarExample(
    val japanese: String,
    val reading: String? = null,
    val meaning: String
)

// Enum cho cấp độ JLPT
enum class JlptLevel {
    N5, N4, N3, N2, N1
}

// Các loại câu hỏi kiểm tra ngữ pháp
enum class GrammarQuestionType {
    MULTIPLE_CHOICE,     // Chọn câu đúng
    COMPLETE_SENTENCE,   // Hoàn thành câu
    REORDER              // Sắp xếp lại câu
}

// Model cho câu hỏi kiểm tra ngữ pháp
data class GrammarQuestion(
    val id: String,
    val grammarId: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val type: GrammarQuestionType,
    val explanation: String
)

// Dữ liệu mẫu cho ngữ pháp
fun getSampleGrammarItems(): List<GrammarItem> {
    return listOf(
        GrammarItem(
            id = "g1",
            title = "〜は〜です",
            level = "N5",
            summary = "A là B (Câu khẳng định đơn giản)",
            explanation = "Cấu trúc cơ bản trong tiếng Nhật để nói 'A là B'. Đây là hình thức lịch sự (polite form) được sử dụng để giới thiệu hoặc xác nhận về danh tính, nghề nghiệp, quốc tịch, v.v. của một người hoặc vật.\n\n" +
                    "Cách sử dụng:\n" +
                    "- Danh từ + は + danh từ + です\n" +
                    "- Chủ ngữ được đánh dấu bằng trợ từ は (wa), và vị ngữ kết thúc bằng です (desu).",
            examples = listOf(
                GrammarExample(
                    japanese = "私は学生です。",
                    reading = "わたしはがくせいです。",
                    meaning = "Tôi là học sinh/sinh viên."
                ),
                GrammarExample(
                    japanese = "これは本です。",
                    reading = "これはほんです。",
                    meaning = "Đây là sách."
                ),
                GrammarExample(
                    japanese = "田中さんは日本人です。",
                    reading = "たなかさんはにほんじんです。",
                    meaning = "Anh/chị Tanaka là người Nhật."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example1",
            isLearned = true
        ),
        GrammarItem(
            id = "g2",
            title = "〜は〜ではありません",
            level = "N5",
            summary = "A không phải là B (Câu phủ định đơn giản)",
            explanation = "Đây là hình thức phủ định của 'Aは Bです'. Sử dụng để diễn đạt một điều gì đó không phải là một điều khác.\n\n" +
                    "Cách sử dụng:\n" +
                    "- Danh từ + は + danh từ + ではありません\n" +
                    "- Trong văn nói, 'じゃありません' thường được sử dụng thay cho 'ではありません'.",
            examples = listOf(
                GrammarExample(
                    japanese = "私は教師ではありません。",
                    reading = "わたしはきょうしではありません。",
                    meaning = "Tôi không phải là giáo viên."
                ),
                GrammarExample(
                    japanese = "これはかばんじゃありません。",
                    reading = "これはかばんじゃありません。",
                    meaning = "Đây không phải là cặp/túi."
                ),
                GrammarExample(
                    japanese = "山田さんは会社員ではありません。",
                    reading = "やまださんはかいしゃいんではありません。",
                    meaning = "Anh/chị Yamada không phải là nhân viên công ty."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example2"
        ),
        GrammarItem(
            id = "g3",
            title = "〜が〜",
            level = "N5",
            summary = "Trợ từ chỉ chủ ngữ, đánh dấu thông tin mới",
            explanation = "Trợ từ が được sử dụng để đánh dấu chủ ngữ trong câu, đặc biệt khi giới thiệu thông tin mới hoặc trong câu hỏi. Nó cũng được sử dụng để nhấn mạnh chủ ngữ.\n\n" +
                    "Cách sử dụng:\n" +
                    "- Danh từ + が + động từ/tính từ\n" +
                    "- Khác với は, trợ từ が thường nhấn mạnh danh từ đứng trước nó.",
            examples = listOf(
                GrammarExample(
                    japanese = "誰が来ましたか？",
                    reading = "だれがきましたか？",
                    meaning = "Ai đã đến?"
                ),
                GrammarExample(
                    japanese = "田中さんが来ました。",
                    reading = "たなかさんがきました。",
                    meaning = "Anh/chị Tanaka đã đến."
                ),
                GrammarExample(
                    japanese = "水が飲みたいです。",
                    reading = "みずがのみたいです。",
                    meaning = "Tôi muốn uống nước."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example3"
        ),
        GrammarItem(
            id = "g4",
            title = "〜を〜",
            level = "N5",
            summary = "Trợ từ chỉ tân ngữ/đối tượng của hành động",
            explanation = "Trợ từ を (đọc là 'o') được sử dụng để chỉ đối tượng trực tiếp của hành động, tức là người hoặc vật nhận tác động của động từ.\n\n" +
                    "Cách sử dụng:\n" +
                    "- Danh từ + を + động từ ngoại động (他動詞)\n" +
                    "- Được sử dụng với các động từ như 食べる (ăn), 飲む (uống), 見る (xem), 読む (đọc), v.v.",
            examples = listOf(
                GrammarExample(
                    japanese = "私はパンを食べます。",
                    reading = "わたしはパンをたべます。",
                    meaning = "Tôi ăn bánh mì."
                ),
                GrammarExample(
                    japanese = "彼は新聞を読みます。",
                    reading = "かれはしんぶんをよみます。",
                    meaning = "Anh ấy đọc báo."
                ),
                GrammarExample(
                    japanese = "私達は映画を見ました。",
                    reading = "わたしたちはえいがをみました。",
                    meaning = "Chúng tôi đã xem phim."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example4"
        ),
        GrammarItem(
            id = "g5",
            title = "〜に〜",
            level = "N5",
            summary = "Trợ từ chỉ nơi chốn, thời gian, người nhận, v.v.",
            explanation = "Trợ từ に có nhiều cách sử dụng, chủ yếu để chỉ:\n" +
                    "1. Địa điểm đến (nơi ai/cái gì đi đến)\n" +
                    "2. Thời gian xảy ra hành động\n" +
                    "3. Người nhận của hành động (người được tặng, v.v.)\n" +
                    "4. Mục đích của hành động\n\n" +
                    "Cách sử dụng:\n" +
                    "- Địa điểm + に + động từ di chuyển\n" +
                    "- Thời gian + に + động từ\n" +
                    "- Người + に + động từ cho/tặng",
            examples = listOf(
                GrammarExample(
                    japanese = "私は学校に行きます。",
                    reading = "わたしはがっこうにいきます。",
                    meaning = "Tôi đi đến trường."
                ),
                GrammarExample(
                    japanese = "7時に起きます。",
                    reading = "しちじにおきます。",
                    meaning = "Tôi thức dậy lúc 7 giờ."
                ),
                GrammarExample(
                    japanese = "友達にプレゼントをあげました。",
                    reading = "ともだちにプレゼントをあげました。",
                    meaning = "Tôi đã tặng quà cho bạn."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example5"
        ),
        GrammarItem(
            id = "g6",
            title = "〜て form (て形)",
            level = "N5",
            summary = "Dạng liên kết giữa các động từ, yêu cầu, v.v.",
            explanation = "て形 (te-kei) là một trong những dạng chia động từ quan trọng nhất trong tiếng Nhật. Nó có nhiều chức năng:\n" +
                    "1. Liên kết các hành động theo trình tự\n" +
                    "2. Yêu cầu ai đó làm gì (〜てください)\n" +
                    "3. Chỉ trạng thái liên tục (〜ている)\n" +
                    "4. Chỉ cách thức, phương tiện (bằng cách...)\n\n" +
                    "Cách chia động từ sang dạng て:\n" +
                    "- Nhóm 1 (う-động từ): Thay う -> って, く -> いて, ぐ -> いで, す -> して, v.v.\n" +
                    "- Nhóm 2 (る-động từ): Bỏ る, thêm て\n" +
                    "- Nhóm 3 (不規則動詞): する -> して, 来る -> 来て",
            examples = listOf(
                GrammarExample(
                    japanese = "食べて寝ます。",
                    reading = "たべてねます。",
                    meaning = "Tôi ăn rồi đi ngủ."
                ),
                GrammarExample(
                    japanese = "水を飲んでください。",
                    reading = "みずをのんでください。",
                    meaning = "Làm ơn uống nước."
                ),
                GrammarExample(
                    japanese = "彼は立っています。",
                    reading = "かれはたっています。",
                    meaning = "Anh ấy đang đứng."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example6"
        ),
        GrammarItem(
            id = "g7",
            title = "〜ない form (否定形)",
            level = "N5",
            summary = "Dạng phủ định của động từ",
            explanation = "Dạng phủ định được sử dụng để diễn đạt không làm gì hoặc không xảy ra điều gì. Cách chia:\n\n" +
                    "1. Động từ nhóm 1 (う-động từ): Thay う → あない\n" +
                    "   ある → ない (ngoại lệ)\n" +
                    "2. Động từ nhóm 2 (る-động từ): Bỏ る, thêm ない\n" +
                    "3. Động từ nhóm 3 (不規則動詞): する → しない, 来る → 来ない",
            examples = listOf(
                GrammarExample(
                    japanese = "私は肉を食べません。",
                    reading = "わたしはにくをたべません。",
                    meaning = "Tôi không ăn thịt."
                ),
                GrammarExample(
                    japanese = "彼は日本語を話さない。",
                    reading = "かれはにほんごをはなさない。",
                    meaning = "Anh ấy không nói tiếng Nhật."
                ),
                GrammarExample(
                    japanese = "明日は来ません。",
                    reading = "あしたはきません。",
                    meaning = "Ngày mai tôi không đến."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example7"
        ),
        GrammarItem(
            id = "g8",
            title = "〜た form (過去形)",
            level = "N5",
            summary = "Dạng quá khứ của động từ",
            explanation = "Dạng quá khứ (た形) được sử dụng để diễn tả hành động đã xảy ra trong quá khứ. Cách chia:\n\n" +
                    "1. Từ dạng て形:\n" +
                    "   - て → た\n" +
                    "   - で → だ\n" +
                    "2. Ví dụ:\n" +
                    "   - 食べる (taberu) → 食べて (tabete) → 食べた (tabeta)\n" +
                    "   - 飲む (nomu) → 飲んで (nonde) → 飲んだ (nonda)",
            examples = listOf(
                GrammarExample(
                    japanese = "私は昨日、映画を見ました。",
                    reading = "わたしはきのう、えいがをみました。",
                    meaning = "Hôm qua tôi đã xem phim."
                ),
                GrammarExample(
                    japanese = "彼女は日本に行きました。",
                    reading = "かのじょはにほんにいきました。",
                    meaning = "Cô ấy đã đi Nhật Bản."
                ),
                GrammarExample(
                    japanese = "私達はパーティーで踊りました。",
                    reading = "わたしたちはパーティーでおどりました。",
                    meaning = "Chúng tôi đã nhảy múa ở bữa tiệc."
                )
            ),
            videoUrl = "https://www.youtube.com/watch?v=example8"
        )
    )
}

// Lấy câu hỏi mẫu cho ngữ pháp
fun getSampleGrammarQuestions(): List<GrammarQuestion> {
    return listOf(
        GrammarQuestion(
            id = "q1",
            grammarId = "g1",
            questionText = "Chọn câu đúng với ngữ pháp 'Aは Bです':",
            options = listOf(
                "私が学生です。",
                "私は学生です。",
                "私を学生です。",
                "私に学生です。"
            ),
            correctAnswerIndex = 1,
            type = GrammarQuestionType.MULTIPLE_CHOICE,
            explanation = "Ngữ pháp 'Aは Bです' sử dụng trợ từ は để đánh dấu chủ ngữ, không phải が, を hay に."
        ),
        GrammarQuestion(
            id = "q2",
            grammarId = "g2",
            questionText = "Hoàn thành câu: 'これは本_______。' (Đây không phải là sách.)",
            options = listOf(
                "ではありません",
                "じゃないです",
                "ではないです",
                "Tất cả đều đúng"
            ),
            correctAnswerIndex = 3,
            type = GrammarQuestionType.COMPLETE_SENTENCE,
            explanation = "Để phủ định 'これは本です', bạn có thể dùng một trong các cách: 'ではありません', 'じゃないです', hoặc 'ではないです'."
        ),
        GrammarQuestion(
            id = "q3",
            grammarId = "g3",
            questionText = "Khi nào nên dùng が thay vì は?",
            options = listOf(
                "Khi đánh dấu chủ đề của câu",
                "Khi giới thiệu thông tin mới hoặc trả lời câu hỏi",
                "Khi kết thúc câu",
                "Khi nói về quá khứ"
            ),
            correctAnswerIndex = 1,
            type = GrammarQuestionType.MULTIPLE_CHOICE,
            explanation = "が thường được sử dụng khi giới thiệu thông tin mới, trả lời câu hỏi, hoặc nhấn mạnh người/vật thực hiện hành động."
        ),
        GrammarQuestion(
            id = "q4",
            grammarId = "g4",
            questionText = "Sắp xếp từ dưới đây thành một câu có ý nghĩa: '食べます - りんごを - 私は'",
            options = listOf(
                "私はりんごを食べます。",
                "りんごを私は食べます。",
                "食べます私はりんごを。",
                "私はを食べますりんご。"
            ),
            correctAnswerIndex = 0,
            type = GrammarQuestionType.REORDER,
            explanation = "Thứ tự cơ bản trong tiếng Nhật là 'Chủ ngữ + Tân ngữ + Động từ', vì vậy câu đúng là '私は(watashi wa) りんごを(ringo o) 食べます(tabemasu)'."
        ),
        GrammarQuestion(
            id = "q5",
            grammarId = "g5",
            questionText = "Chọn câu sử dụng trợ từ に đúng cách:",
            options = listOf(
                "私は学校で行きます。",
                "7時は起きます。",
                "私は東京に住んでいます。",
                "友達をプレゼントをあげました。"
            ),
            correctAnswerIndex = 2,
            type = GrammarQuestionType.MULTIPLE_CHOICE,
            explanation = "に được sử dụng với động từ 住む (sống ở) để chỉ nơi ai đó sống."
        )
    )
}

// Lấy ngữ pháp từ id
fun getGrammarById(id: String): GrammarItem? {
    return getSampleGrammarItems().find { it.id == id }
}

// Lấy câu hỏi liên quan đến ngữ pháp
fun getQuestionsByGrammarId(grammarId: String): List<GrammarQuestion> {
    return getSampleGrammarQuestions().filter { it.grammarId == grammarId }
} 