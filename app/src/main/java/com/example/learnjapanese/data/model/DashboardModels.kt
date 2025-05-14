package com.example.learnjapanese.data.model

/**
 * Lớp dữ liệu cho banner tính năng nổi bật trên màn hình Dashboard.
 *
 * @param title Tiêu đề của tính năng
 * @param description Mô tả ngắn gọn về tính năng
 * @param imageUrl Đường dẫn đến hình ảnh banner (có thể null)
 */
data class FeaturedBanner(
    val title: String,
    val description: String,
    val imageUrl: String? = null
)

/**
 * Lớp dữ liệu cho các tính năng học tập trên màn hình Dashboard.
 *
 * @param title Tên của tính năng học tập (ví dụ: "Từ vựng N5")
 * @param progress Phần trăm hoàn thành (từ 0 đến 1)
 */
data class LearningFeature(
    val title: String,
    val progress: Float
) 