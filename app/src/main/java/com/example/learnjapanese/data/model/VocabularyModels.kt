package com.example.learnjapanese.data.model

// Model cho chủ đề từ vựng
data class VocabularyTopic(
    val id: String,
    val name: String,
    val category: String,
    val totalWords: Int,
    val progress: Float,
    val isFavorite: Boolean = false,
    val words: List<VocabularyWord> = emptyList()
)

// Model cho từ vựng
data class VocabularyWord(
    val id: String,
    val word: String,
    val reading: String,
    val meaning: String,
    val exampleSentence: String? = null,
    val exampleSentenceTranslation: String? = null,
    val isLearned: Boolean = false
)

// Các loại câu hỏi quiz
enum class QuizQuestionType {
    JAPANESE_TO_MEANING,     // Cho từ tiếng Nhật, chọn nghĩa
    MEANING_TO_JAPANESE      // Cho nghĩa, chọn từ tiếng Nhật
}

// Model cho câu hỏi quiz
data class QuizQuestion(
    val questionWord: VocabularyWord,
    val answers: List<VocabularyWord>,
    val correctAnswerIndex: Int,
    val type: QuizQuestionType
)

// Dữ liệu mẫu
fun getSampleTopics(): List<VocabularyTopic> {
    val familyWords = listOf(
        VocabularyWord(
            "1",
            "家族",
            "かぞく",
            "Gia đình",
            "私の家族は5人です。",
            "Gia đình tôi có 5 người.",
            true
        ),
        VocabularyWord(
            "2",
            "両親",
            "りょうしん",
            "Bố mẹ, cha mẹ",
            "両親は田舎に住んでいます。",
            "Bố mẹ tôi sống ở quê.",
            true
        ),
        VocabularyWord(
            "3",
            "父",
            "ちち",
            "Bố (của mình)",
            "父は会社員です。",
            "Bố tôi là nhân viên công ty.",
            false
        ),
        VocabularyWord(
            "4",
            "母",
            "はは",
            "Mẹ (của mình)",
            "母は先生です。",
            "Mẹ tôi là giáo viên.",
            false
        ),
        VocabularyWord(
            "5",
            "兄",
            "あに",
            "Anh trai (của mình)",
            "兄は大学生です。",
            "Anh trai tôi là sinh viên đại học.",
            false
        )
    )
    
    val travelWords = listOf(
        VocabularyWord(
            "1",
            "旅行",
            "りょこう",
            "Du lịch, chuyến đi",
            "旅行が好きです。",
            "Tôi thích du lịch.",
            true
        ),
        VocabularyWord(
            "2",
            "観光",
            "かんこう",
            "Tham quan, thăm quan",
            "京都で観光しました。",
            "Tôi đã tham quan ở Kyoto.",
            false
        ),
        VocabularyWord(
            "3",
            "予約",
            "よやく",
            "Đặt trước, dự định",
            "ホテルを予約しました。",
            "Tôi đã đặt khách sạn.",
            false
        )
    )
    
    return listOf(
        VocabularyTopic("1", "Gia đình", "N5", 30, 0.8f, true, familyWords),
        VocabularyTopic("2", "Du lịch", "N5", 45, 0.5f, true, travelWords),
        VocabularyTopic("3", "Công việc", "N4", 50, 0.3f),
        VocabularyTopic("4", "Nhà hàng", "N5", 25, 0.9f),
        VocabularyTopic("5", "Trường học", "N5", 40, 0.6f),
        VocabularyTopic("6", "Giao thông", "N4", 35, 0.4f),
        VocabularyTopic("7", "Thời tiết", "N5", 20, 0.7f),
        VocabularyTopic("8", "Mua sắm", "N4", 30, 0.2f),
        VocabularyTopic("9", "Sức khỏe", "N4", 45, 0.1f),
        VocabularyTopic("10", "Thể thao", "N4", 28, 0.4f)
    )
}

// Lấy từ vựng từ id chủ đề
fun getSampleWords(topicId: String): List<VocabularyWord> {
    val topic = getSampleTopics().find { it.id == topicId }
    return topic?.words ?: emptyList()
}

// Hàm tạo câu hỏi quiz từ danh sách từ vựng
fun generateQuizQuestions(words: List<VocabularyWord>, count: Int): List<QuizQuestion> {
    if (words.size < 4) return emptyList() // Cần ít nhất 4 từ cho câu hỏi trắc nghiệm
    
    val questions = mutableListOf<QuizQuestion>()
    val availableWords = words.toMutableList()
    
    // Tạo các câu hỏi (tối đa count hoặc số từ có sẵn)
    repeat(minOf(count, availableWords.size)) {
        if (availableWords.isEmpty()) return@repeat
        
        // Chọn ngẫu nhiên loại câu hỏi
        val questionType = if (kotlin.random.Random.nextBoolean()) 
            QuizQuestionType.JAPANESE_TO_MEANING 
        else 
            QuizQuestionType.MEANING_TO_JAPANESE
        
        // Chọn ngẫu nhiên một từ làm đáp án đúng
        val correctIndex = kotlin.random.Random.nextInt(availableWords.size)
        val correctWord = availableWords.removeAt(correctIndex)
        
        // Tạo 3 lựa chọn sai
        val shuffledWords = words.filter { it.id != correctWord.id }.shuffled()
        val wrongOptions = shuffledWords.take(3)
        
        // Kết hợp đáp án đúng và sai
        val answers = mutableListOf<VocabularyWord>()
        answers.add(correctWord)
        answers.addAll(wrongOptions)
        
        // Xáo trộn đáp án
        answers.shuffle()
        
        // Tìm vị trí đáp án đúng sau khi xáo trộn
        val correctAnswerIndex = answers.indexOfFirst { it.id == correctWord.id }
        
        // Tạo câu hỏi
        questions.add(
            QuizQuestion(
                questionWord = correctWord,
                answers = answers,
                correctAnswerIndex = correctAnswerIndex,
                type = questionType
            )
        )
    }
    
    return questions
} 