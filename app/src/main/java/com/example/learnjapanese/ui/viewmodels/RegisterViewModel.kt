package com.example.learnjapanese.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    var fullName by mutableStateOf("")
        private set
    
    var email by mutableStateOf("")
        private set
    
    var phone by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set
    
    var confirmPassword by mutableStateOf("")
        private set

    var fullNameError by mutableStateOf<String?>(null)
        private set
    
    var emailError by mutableStateOf<String?>(null)
        private set
    
    var phoneError by mutableStateOf<String?>(null)
        private set
    
    var passwordError by mutableStateOf<String?>(null)
        private set
    
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPhone(phone: String): Boolean {
        val phonePattern = "^0[0-9]{9}$"
        return phone.matches(phonePattern.toRegex())
    }

    private fun isValidFullName(name: String): Boolean {
        // Chỉ cho phép chữ cái, dấu cách và dấu tiếng Việt
        val namePattern = "^[\\p{L}\\s]+$"
        return name.matches(namePattern.toRegex())
    }

    private fun validatePasswordStrength(password: String): String? {
        if (password.isEmpty()) {
            return "Vui lòng nhập mật khẩu"
        }
        
        val errors = mutableListOf<String>()
        
        if (password.length < 8) {
            errors.add("ít nhất 8 ký tự")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("chữ hoa")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("chữ thường")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("số")
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            errors.add("ký tự đặc biệt")
        }
        
        return if (errors.isEmpty()) null else "Mật khẩu phải có ${errors.joinToString(", ")}"
    }
    
    fun updateFullName(newFullName: String) {
        fullName = newFullName
        fullNameError = when {
            newFullName.isEmpty() -> "Vui lòng nhập họ tên"
            newFullName.length < 8 -> "Họ tên phải có ít nhất 8 ký tự"
            !isValidFullName(newFullName) -> "Họ tên không được chứa ký tự đặc biệt hoặc số"
            else -> null
        }
    }
    
    fun updateEmail(newEmail: String) {
        email = newEmail
        emailError = if (newEmail.isEmpty()) {
            "Vui lòng nhập địa chỉ email"
        } else if (!isValidEmail(newEmail)) {
            "Vui lòng nhập địa chỉ email hợp lệ. Ví dụ: tinh@gmail.com"
        } else null
    }
    
    fun updatePhone(newPhone: String) {
        // Giới hạn độ dài số điện thoại là 10 số
        if (newPhone.length <= 10) {
            phone = newPhone
            phoneError = when {
                newPhone.isEmpty() -> "Vui lòng nhập số điện thoại"
                !newPhone.startsWith("0") -> "Số điện thoại phải bắt đầu bằng số 0"
                !isValidPhone(newPhone) -> "Số điện thoại phải có 10 chữ số"
                else -> null
            }
        }
    }
    
    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = validatePasswordStrength(newPassword)
    }
    
    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        confirmPasswordError = if (newConfirmPassword.isEmpty()) {
            "Vui lòng xác nhận mật khẩu"
        } else if (newConfirmPassword != password) {
            "Mật khẩu xác nhận không khớp"
        } else null
    }

    fun validateInputs(): Boolean {
        updateFullName(fullName)
        updateEmail(email)
        updatePhone(phone)
        updatePassword(password)
        updateConfirmPassword(confirmPassword)
        
        return fullNameError == null && 
               emailError == null && 
               phoneError == null && 
               passwordError == null && 
               confirmPasswordError == null
    }
    
    fun register() {
        if (validateInputs()) {
            // Thực hiện logic đăng ký ở đây
            // Thông thường sẽ gọi đến repository
        }
    }
}