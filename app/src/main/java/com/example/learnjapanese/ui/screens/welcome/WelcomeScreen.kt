package com.example.learnjapanese.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import com.example.learnjapanese.R
import com.example.learnjapanese.ui.theme.MauChinh
import androidx.compose.ui.text.TextStyle
import com.example.learnjapanese.ui.theme.xanh_la_1
import com.example.learnjapanese.ui.theme.xanh_la_2
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.foundation.border
import androidx.compose.animation.*
import kotlinx.coroutines.delay
import com.example.learnjapanese.ui.theme.Cam

@Composable
fun WelcomeScreen(
    onContinueClick: () -> Unit
) {
    var firstTextCharCount by remember { mutableStateOf(0) }
    var secondTextCharCount by remember { mutableStateOf(0) }
    var buttonVisible by remember { mutableStateOf(false) }

    val firstText = "Ứng dụng học tiếng nhật chất lượng"
    val secondText = "LearnJapanese"
    val threeText = "Chiến đấu nào!!!"
    var thirdTextCharCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        firstText.forEachIndexed { index, _ ->
            delay(30)
            firstTextCharCount = index + 2
        }
        secondText.forEachIndexed { index, _ ->
            delay(55)
            secondTextCharCount = index + 1
        }
        // Add animation for third text
        threeText.forEachIndexed { index, _ ->
            delay(40)
            thirdTextCharCount = index + 1
        }
        buttonVisible = true
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.hinh_nen_4),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.45f))
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(0.87f)
                .padding(16.dp)
                .clip(RoundedCornerShape(40.dp))
                .border(
                    width = 1.2.dp,
                    color = xanh_la_1.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(40.dp)
                )
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hinh_nen_4),
                contentDescription = "Inner Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.2f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(
                        text = firstText.take(firstTextCharCount),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Italic,
                        color = xanh_la_2
                    )
                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = secondText.take(secondTextCharCount),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                offset = Offset(2f, 2f),
                                blurRadius = 2f
                            )
                        ),
                        textAlign = TextAlign.Center,
                        color = xanh_la_1,
                        letterSpacing = 1.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = threeText.take(thirdTextCharCount),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        color = Cam,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    
                    AnimatedVisibility(
                        visible = buttonVisible,
                        enter = fadeIn() + slideInVertically(
                            initialOffsetY = { 40 }
                        )
                    ) {
                        Button(
                            onClick = onContinueClick,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(bottom = 15.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MauChinh)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    text = "Tiếp tục",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White 
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Filled.ArrowForward,
                                    contentDescription = "Next",
                                    modifier = Modifier.size(26.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}