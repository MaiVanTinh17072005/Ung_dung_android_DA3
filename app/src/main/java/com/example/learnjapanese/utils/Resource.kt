package com.example.learnjapanese.utils

/**
 * Lớp tiện ích để quản lý trạng thái của dữ liệu khi tương tác với API
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
} 