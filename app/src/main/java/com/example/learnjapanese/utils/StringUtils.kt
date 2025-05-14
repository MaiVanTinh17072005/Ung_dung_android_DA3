package com.example.learnjapanese.utils

import java.text.Normalizer
import java.util.regex.Pattern

/**
 * Chuyển đổi chuỗi có dấu thành không dấu
 * Ví dụ: "Ẩm thực" -> "am thuc"
 */
fun String.removeAccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(temp).replaceAll("").lowercase()
} 