package com.example.learnjapanese.screens.grammar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.GrammarItem
import com.example.learnjapanese.data.repository.GrammarRepository
import com.example.learnjapanese.utils.Resource
import com.example.learnjapanese.utils.removeAccent
import com.example.learnjapanese.utils.toUiModelList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình danh sách ngữ pháp
 */
@HiltViewModel
class GrammarViewModel @Inject constructor(
    private val grammarRepository: GrammarRepository
) : ViewModel() {

    // State của danh sách ngữ pháp
    private val _grammarItems = MutableStateFlow<Resource<List<GrammarItem>>>(Resource.Loading())
    val grammarItems: StateFlow<Resource<List<GrammarItem>>> = _grammarItems.asStateFlow()
    
    // Lưu trữ danh sách gốc để phục vụ tìm kiếm và lọc
    private var originalGrammarItems: List<GrammarItem> = emptyList()
    
    init {
        loadGrammarItems()
    }
    
    /**
     * Tải danh sách ngữ pháp từ API
     * @param level Cấp độ ngữ pháp cần lọc (null nếu không lọc)
     */
    fun loadGrammarItems(level: String? = null) {
        viewModelScope.launch {
            _grammarItems.value = Resource.Loading()
            
            try {
                // Gọi API lấy danh sách ngữ pháp với bộ lọc level
                val result = grammarRepository.getGrammarList(level = level)
                
                result.fold(
                    onSuccess = { grammarResponses ->
                        // Chuyển đổi từ model API sang model UI
                        val uiGrammarItems = grammarResponses.toUiModelList()
                        originalGrammarItems = uiGrammarItems
                        _grammarItems.value = Resource.Success(uiGrammarItems)
                    },
                    onFailure = { exception ->
                        _grammarItems.value = Resource.Error(exception.message ?: "Không thể tải danh sách ngữ pháp")
                    }
                )
            } catch (e: Exception) {
                _grammarItems.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Tìm kiếm ngữ pháp dựa trên từ khóa bằng API
     * @param keyword Từ khóa tìm kiếm
     */
    fun searchGrammarFromApi(keyword: String) {
        viewModelScope.launch {
            _grammarItems.value = Resource.Loading()
            
            try {
                val result = grammarRepository.searchGrammar(keyword)
                
                result.fold(
                    onSuccess = { grammarResponses ->
                        val uiGrammarItems = grammarResponses.toUiModelList()
                        _grammarItems.value = Resource.Success(uiGrammarItems)
                    },
                    onFailure = { exception ->
                        _grammarItems.value = Resource.Error(exception.message ?: "Không thể tìm kiếm ngữ pháp")
                    }
                )
            } catch (e: Exception) {
                _grammarItems.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Tìm kiếm ngữ pháp trong danh sách đã tải (tìm kiếm local)
     * @param query Từ khóa tìm kiếm
     */
    fun searchGrammarLocally(query: String) {
        if (query.isBlank()) {
            _grammarItems.value = Resource.Success(originalGrammarItems)
            return
        }
        
        val normalizedQuery = query.removeAccent()
        
        val filteredItems = originalGrammarItems.filter { item ->
            item.title.removeAccent().contains(normalizedQuery) || 
            item.summary.removeAccent().contains(normalizedQuery) ||
            item.explanation?.removeAccent()?.contains(normalizedQuery) == true
        }
        
        _grammarItems.value = Resource.Success(filteredItems)
    }
    
    /**
     * Lọc ngữ pháp theo cấp độ
     * @param level Cấp độ ngữ pháp
     */
    fun filterByLevel(level: String) {
        if (level == "Tất cả") {
            _grammarItems.value = Resource.Success(originalGrammarItems)
            return
        }
        
        val filteredItems = originalGrammarItems.filter { it.level == level }
        _grammarItems.value = Resource.Success(filteredItems)
    }
    
    /**
     * Lấy nhiều ngữ pháp theo danh sách ID (cho chức năng quiz)
     * @param grammarIds Danh sách ID ngữ pháp cần lấy
     * @return Flow trả về Resource<List<GrammarItem>>
     */
    fun getGrammarBatch(grammarIds: List<String>) {
        viewModelScope.launch {
            _grammarItems.value = Resource.Loading()
            
            try {
                val result = grammarRepository.getGrammarBatch(grammarIds)
                
                result.fold(
                    onSuccess = { grammarResponses ->
                        val uiGrammarItems = grammarResponses.toUiModelList()
                        _grammarItems.value = Resource.Success(uiGrammarItems)
                    },
                    onFailure = { exception ->
                        _grammarItems.value = Resource.Error(exception.message ?: "Không thể tải ngữ pháp theo ID")
                    }
                )
            } catch (e: Exception) {
                _grammarItems.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
} 