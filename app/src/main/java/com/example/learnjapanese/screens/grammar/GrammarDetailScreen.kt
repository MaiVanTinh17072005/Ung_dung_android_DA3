package com.example.learnjapanese.screens.grammar

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.View
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.learnjapanese.data.model.GrammarExample
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.utils.Resource
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrammarDetailScreen(
    grammarId: String,
    onBack: () -> Unit = {},
    onStartQuiz: (String) -> Unit = {},
    viewModel: GrammarDetailViewModel = hiltViewModel()
) {
    val grammarDetailResource by viewModel.grammarDetail.collectAsState()
    
    // Nếu đang ở chế độ toàn màn hình, chỉ hiển thị video toàn màn hình
    if (viewModel.isFullscreen) {
        grammarDetailResource.let { resource ->
            if (resource is Resource.Success) {
                resource.data?.videoUrl?.let { videoUrl ->
                    FullscreenVideoDialog(
                        videoUrl = videoUrl,
                        onDismiss = { viewModel.toggleFullscreen() }
                    )
                }
            }
        }
        return
    }
    
    // Hiển thị giao diện thông thường nếu không ở chế độ toàn màn hình
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (val resource = grammarDetailResource) {
                        is Resource.Success -> {
                            Text(
                                text = resource.data?.title ?: "Chi tiết ngữ pháp",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        else -> {
                            Text(
                                text = "Chi tiết ngữ pháp",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onStartQuiz(grammarId) }) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "Kiểm tra"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { innerPadding ->
        when (val resource = grammarDetailResource) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }
            
            is Resource.Success -> {
                val grammar = resource.data
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Video section
                    grammar?.videoUrl?.let { videoUrl ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                // Title trước
                                Text(
                                    text = "Bài giảng video",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Text(
                                    text = "Xem video để hiểu rõ hơn về ngữ pháp này",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Video Player trong Box riêng biệt với kích thước cố định
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Trình phát video không toàn màn hình
                                    VideoPlayerWithControls(
                                        url = videoUrl,
                                        onFullscreenToggle = { viewModel.toggleFullscreen() }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Grammar info
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Level badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = grammar?.level ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = grammar?.summary ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Divider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Hiển thị dạng ngữ pháp (title)
                            grammar?.title?.let { title ->
                                Text(
                                    text = "Mẫu câu",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Divider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            
                            // Hiển thị tóm tắt (summary)
                            grammar?.summary?.let { summary ->
                                Text(
                                    text = "Ý nghĩa",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = summary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Divider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            
                            // Explanation (giải thích)
                            grammar?.explanation?.let { explanation ->
                                if (explanation.isNotBlank()) {
                                    Text(
                                        text = "Giải thích",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = explanation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                        }
                    }
                    
                    // Examples
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Ví dụ",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Hiển thị ví dụ từ model
                            grammar?.examples?.let { examples ->
                                examples.forEachIndexed { index, example ->
                                    ExampleCard(example = example)
                                    
                                    if (index < examples.size - 1) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(80.dp)) // Cho bottom navigation
                }
            }
            
            is Resource.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Không thể tải chi tiết ngữ pháp",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = resource.message ?: "Đã xảy ra lỗi không xác định",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { viewModel.loadGrammarDetail() }) {
                            Text("Thử lại")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExampleCard(example: GrammarExample) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Japanese text
            Text(
                text = example.japanese,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            // Reading
            if (example.reading != null) {
                Text(
                    text = example.reading,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Divider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            // Meaning
            Text(
                text = example.meaning,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FullscreenVideoDialog(
    videoUrl: String,
    onDismiss: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    
    // Ẩn hoàn toàn thanh trạng thái và thanh điều hướng
    DisposableEffect(Unit) {
        activity?.let { act ->
            val window = act.window
            
            // Ẩn hoàn toàn thanh trạng thái và thanh điều hướng
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowCompat.getInsetsController(window, window.decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            
            // Đặt cờ toàn màn hình
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            
            // Cài đặt hướng ngang
            act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        
        onDispose {
            activity?.let { act ->
                val window = act.window
                
                // Khôi phục lại trạng thái UI bình thường
                WindowCompat.setDecorFitsSystemWindows(window, true)
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.show(WindowInsetsCompat.Type.systemBars())
                controller.show(WindowInsetsCompat.Type.statusBars())
                controller.show(WindowInsetsCompat.Type.navigationBars())
                
                // Xóa cờ toàn màn hình
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                
                // Trở lại hướng mặc định
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
    
    // Sử dụng Box thay vì Dialog để tràn màn hình hoàn toàn
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        VideoPlayerWithControls(
            url = videoUrl,
            isFullscreen = true,
            onFullscreenToggle = { onDismiss() }
        )
        
        // Xử lý nút back
        BackHandler {
            onDismiss()
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerWithControls(
    url: String,
    isFullscreen: Boolean = false,
    onFullscreenToggle: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    
    // Tạo và cấu hình ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                }
            })
        }
    }
    
    // Giải phóng ExoPlayer khi không cần thiết nữa
    DisposableEffect(key1 = Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    Box(
        modifier = if (isFullscreen) {
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        } else {
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f/9f)
        }
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                    setBackgroundColor(android.graphics.Color.BLACK)
                    
                    // Thay đổi cách hiển thị video cho toàn màn hình
                    if (isFullscreen) {
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    } else {
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }
                    
                    // Tùy chỉnh điều khiển dựa trên chế độ
                    if (isFullscreen) {
                        controllerShowTimeoutMs = 2500
                        controllerHideOnTouch = true
                        setShowNextButton(false)
                        setShowPreviousButton(false)
                    } else {
                        controllerShowTimeoutMs = 3000
                        controllerHideOnTouch = true
                    }
                    
                    setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                    
                    // Cấu hình layout
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { playerView ->
                playerView.player = exoPlayer
                
                // Cập nhật cấu hình khi chuyển đổi chế độ
                if (isFullscreen) {
                    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    playerView.controllerShowTimeoutMs = 2500
                } else {
                    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    playerView.controllerShowTimeoutMs = 3000
                }
                
                playerView.showController()
            }
        )
        
        // Nút toàn màn hình
        IconButton(
            onClick = { onFullscreenToggle(!isFullscreen) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(40.dp)
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(
                imageVector = if (isFullscreen) Icons.Default.CloseFullscreen else Icons.Default.Fullscreen,
                contentDescription = if (isFullscreen) "Thoát toàn màn hình" else "Toàn màn hình",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GrammarDetailScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GrammarDetailScreen(grammarId = "g1")
        }
    }
} 