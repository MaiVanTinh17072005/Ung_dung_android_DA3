package com.example.learnjapanese.screens.grammar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.GrammarItem
import com.example.learnjapanese.data.repository.GrammarRepository
import com.example.learnjapanese.utils.Resource
import com.example.learnjapanese.utils.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình chi tiết ngữ pháp
 */
@HiltViewModel
class GrammarDetailViewModel @Inject constructor(
    private val grammarRepository: GrammarRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Lấy ID ngữ pháp từ navigation arguments
    private val grammarId: String = checkNotNull(savedStateHandle["grammarId"])
    
    // State của chi tiết ngữ pháp
    private val _grammarDetail = MutableStateFlow<Resource<GrammarItem>>(Resource.Loading())
    val grammarDetail: StateFlow<Resource<GrammarItem>> = _grammarDetail.asStateFlow()
    
    init {
        loadGrammarDetail()
    }
    
    /**
     * Tải thông tin chi tiết của ngữ pháp
     */
    fun loadGrammarDetail() {
        viewModelScope.launch {
            _grammarDetail.value = Resource.Loading()
            
            try {
                val result = grammarRepository.getGrammarDetail(grammarId)
                
                result.fold(
                    onSuccess = { grammarResponse ->
                        // Chuyển đổi từ model API sang model UI
                        val uiGrammarItem = grammarResponse.toUiModel()
                        _grammarDetail.value = Resource.Success(uiGrammarItem)
                    },
                    onFailure = { exception ->
                        _grammarDetail.value = Resource.Error(exception.message ?: "Không thể tải chi tiết ngữ pháp")
                    }
                )
            } catch (e: Exception) {
                _grammarDetail.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Đánh dấu ngữ pháp này đã học (sẽ cần triển khai API riêng)
     */
    fun markAsLearned() {
        // Trong tương lai có thể gọi API để đánh dấu đã học
        // Tạm thời chỉ cập nhật UI
        val currentDetail = (_grammarDetail.value as? Resource.Success)?.data ?: return
        _grammarDetail.value = Resource.Success(currentDetail.copy(isLearned = true))
    }
} 