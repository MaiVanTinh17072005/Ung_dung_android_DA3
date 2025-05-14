package com.example.learnjapanese.ui.components

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin

/**
 * Composable để hiển thị nội dung Markdown
 * 
 * @param markdown Nội dung markdown cần hiển thị
 * @param modifier Modifier để áp dụng cho component
 * @param color Màu chữ
 * @param textAlign Căn chỉnh văn bản
 * @param style Style của văn bản
 * @param linkColor Màu của liên kết, mặc định là màu primary
 */
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
    linkColor: Color = MaterialTheme.colorScheme.primary
) {
    val context = LocalContext.current
    
    // Cấu hình Markwon với các tùy chỉnh cần thiết
    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(ImagesPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(StrikethroughPlugin.create())
            .build()
    }
    
    AndroidView(
        factory = { ctx ->
            TextView(ctx).apply {
                // Thiết lập các thuộc tính cơ bản
                setTextColor(color.toArgb())
                textAlign?.let { align ->
                    this.textAlignment = when (align) {
                        TextAlign.Center -> TextView.TEXT_ALIGNMENT_CENTER
                        TextAlign.End -> TextView.TEXT_ALIGNMENT_TEXT_END
                        TextAlign.Start -> TextView.TEXT_ALIGNMENT_TEXT_START
                        else -> TextView.TEXT_ALIGNMENT_TEXT_START
                    }
                }
                
                // Áp dụng TextStyle từ Compose
                this.textSize = style.fontSize.value
                this.setLineSpacing(style.lineHeight.value, 1f)
                
                // Chuyển đổi FontWeight sang android.graphics.Typeface
                this.typeface = android.graphics.Typeface.create(
                    "sans-serif",
                    when {
                        style.fontWeight?.weight ?: 400 >= 700 -> android.graphics.Typeface.BOLD
                        else -> android.graphics.Typeface.NORMAL
                    }
                )
                
                // Cho phép liên kết có thể nhấn vào
                this.linksClickable = true
                
                // Thiết lập padding để cải thiện trải nghiệm đọc
                this.setPadding(0, 0, 0, 0)
                
                // Làm cho links có màu của primary color
                this.setLinkTextColor(linkColor.toArgb())
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RectangleShape),
        update = { textView ->
            // Cập nhật nội dung markdown
            markwon.setMarkdown(textView, markdown)
        }
    )
} 