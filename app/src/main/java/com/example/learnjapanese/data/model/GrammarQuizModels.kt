package com.example.learnjapanese.data.model

/**
 * Model câu hỏi quiz ngữ pháp
 */
data class GrammarQuizQuestion(
    val questionId: String,
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val grammarPattern: String
)

/**
 * Model kết quả quiz ngữ pháp
 */
data class GrammarQuizResult(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val percentageCorrect: Float,
    val grammarIds: List<String>
) 