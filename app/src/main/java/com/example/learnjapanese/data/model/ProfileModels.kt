package com.example.learnjapanese.data.model

data class GetProfileRequest(
    val user_id: String
)

data class GetProfileResponse(
    val success: Boolean,
    val data: UserProfileData?,
    val message: String
)

data class UserProfileData(
    val email: String,
    val number_phone: String,
    val display_name: String,
    val profile_image_url: String? = null
)

data class SetProfileRequest(
    val userId: String,
    val email: String,
    val number_phone: String,
    val display_name: String
)

data class SetProfileResponse(
    val success: Boolean,
    val message: String
)

// Model cho API thay đổi avatar
data class UpdateAvatarRequest(
    val userId: String,
    val profile_image_url: String // Base64 encoded image data
)

data class UpdateAvatarResponse(
    val success: Boolean,
    val message: String,
    val profile_image_url: String? = null
)