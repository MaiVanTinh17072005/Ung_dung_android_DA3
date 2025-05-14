package com.example.learnjapanese.utils

import com.example.learnjapanese.data.model.GrammarExample
import com.example.learnjapanese.data.model.GrammarQuizQuestion
import com.example.learnjapanese.data.model.QuizExampleResponse
import com.example.learnjapanese.data.model.QuizQuestionResponse

/**
 * Chuyển đổi từ QuizQuestionResponse (model API) sang GrammarQuizQuestion (model UI)
 */
fun QuizQuestionResponse.toUiModel(): GrammarQuizQuestion {
    return GrammarQuizQuestion(
        questionId = this.questionId,
        questionText = this.questionText,
        answers = this.options,
        correctAnswerIndex = this.correctAnswerIndex,
        explanation = this.explanation,
        grammarPattern = this.grammarPattern
    )
}

/**
 * Chuyển đổi từ QuizExampleResponse (model API) sang GrammarExample (model UI)
 */
fun QuizExampleResponse.toUiModel(): GrammarExample {
    return GrammarExample(
        japanese = this.japanese,
        reading = this.reading,
        meaning = this.meaning
    )
}

/**
 * Chuyển đổi danh sách QuizQuestionResponse thành danh sách GrammarQuizQuestion
 */
fun List<QuizQuestionResponse>.toUiModelList(): List<GrammarQuizQuestion> {
    return this.map { it.toUiModel() }
} 