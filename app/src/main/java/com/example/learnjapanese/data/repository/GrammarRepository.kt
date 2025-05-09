package com.example.learnjapanese.data.repository

import com.example.learnjapanese.data.api.GrammarApiService
import com.example.learnjapanese.data.model.GrammarBatchRequest
import com.example.learnjapanese.data.model.GrammarDetailResponse
import com.example.learnjapanese.data.model.QuizGenerationRequest
import com.example.learnjapanese.data.model.QuizResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository xử lý các thao tác liên quan đến ngữ pháp
 */
@Singleton
class GrammarRepository @Inject constructor(
    private val grammarApiService: GrammarApiService
) {
    /**
     * Lấy danh sách ngữ pháp với các bộ lọc
     * @param level Cấp độ ngữ pháp (N5, N4, N3, N2, N1)
     * @param category Danh mục ngữ pháp
     * @param limit Số lượng mục trên một trang
     * @param page Số trang
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getGrammarList(
        level: String? = null,
        category: String? = null,
        limit: Int? = null,
        page: Int? = null
    ): Result<List<GrammarDetailResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = grammarApiService.getGrammarList(
                    limit = limit,
                    page = page,
                    level = level,
                    category = category
                )
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            Result.success(it.data)
                        } else {
                            Result.failure(IOException(it.message))
                        }
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể tải danh sách ngữ pháp: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Lấy thông tin chi tiết của một ngữ pháp
     * @param grammarId ID của ngữ pháp cần lấy
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getGrammarDetail(grammarId: String): Result<GrammarDetailResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = grammarApiService.getGrammarDetail(grammarId)
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            Result.success(it.data)
                        } else {
                            Result.failure(IOException(it.message))
                        }
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể tải chi tiết ngữ pháp: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Lấy thông tin của nhiều ngữ pháp theo danh sách ID
     * @param grammarIds Danh sách ID ngữ pháp cần lấy
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getGrammarBatch(grammarIds: List<String>): Result<List<GrammarDetailResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val request = GrammarBatchRequest(ids = grammarIds)
                val response = grammarApiService.getGrammarBatch(request)
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            Result.success(it.data)
                        } else {
                            Result.failure(IOException(it.message))
                        }
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể tải danh sách ngữ pháp theo ID: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Tìm kiếm ngữ pháp theo từ khóa
     * @param keyword Từ khóa tìm kiếm
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun searchGrammar(keyword: String): Result<List<GrammarDetailResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = grammarApiService.searchGrammar(keyword)
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            Result.success(it.data)
                        } else {
                            Result.failure(IOException(it.message))
                        }
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể tìm kiếm ngữ pháp: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Tạo các câu hỏi ngữ pháp thông qua API AI
     * @param request Thông tin yêu cầu tạo câu hỏi
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu câu hỏi được tạo
     */
    suspend fun generateQuiz(request: QuizGenerationRequest): Result<QuizResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = grammarApiService.generateQuiz(request)
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            Result.success(it.data)
                        } else {
                            Result.failure(IOException(it.message))
                        }
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể tạo câu hỏi ngữ pháp: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 