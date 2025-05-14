package com.example.learnjapanese.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.net.URLEncoder

/**
 * Tiện ích để phát âm thanh từ vựng tiếng Nhật thông qua Google Translate API
 */
object TextToSpeechUtil {
    private var mediaPlayer: MediaPlayer? = null
    
    /**
     * Phát âm thanh tiếng Nhật
     * @param context Context của ứng dụng
     * @param text Văn bản tiếng Nhật cần phát âm
     */
    fun speak(context: Context, text: String) {
        try {
            if (text.isBlank()) return
            
            // Hủy phiên phát âm cũ nếu có
            stopSpeaking()
            
            // Mã hóa văn bản để sử dụng trong URL
            val encodedText = URLEncoder.encode(text, "UTF-8")
            val url = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=ja&q=$encodedText"
            
            Log.d("TextToSpeechUtil", "Preparing to play audio from URL: $url")
            
            // Chuẩn bị MediaPlayer mới
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                
                setDataSource(context, Uri.parse(url))
                setOnPreparedListener { mp -> 
                    Log.d("TextToSpeechUtil", "MediaPlayer prepared, starting playback")
                    mp.start() 
                }
                setOnCompletionListener { mp ->
                    Log.d("TextToSpeechUtil", "Playback completed")
                    mp.reset()
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("TextToSpeechUtil", "MediaPlayer error: what=$what, extra=$extra")
                    mp.reset()
                    true
                }
                
                prepareAsync()
            }
            
        } catch (e: Exception) {
            Log.e("TextToSpeechUtil", "Error playing audio", e)
            stopSpeaking()
        }
    }
    
    /**
     * Dừng phát âm thanh
     */
    fun stopSpeaking() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
                Log.d("TextToSpeechUtil", "MediaPlayer released")
            } catch (e: Exception) {
                Log.e("TextToSpeechUtil", "Error stopping MediaPlayer", e)
            }
        }
        mediaPlayer = null
    }
} 