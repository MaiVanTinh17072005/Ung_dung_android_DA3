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
import androidx.compose.ui.text.style.TextDecoration
import com.example.learnjapanese.ui.theme.xanh_la_1
import com.example.learnjapanese.ui.theme.xanh_la_2
import com.google.accompanist.systemuicontroller.rememberSystemUiController
// Add this import at the top
import androidx.compose.ui.text.font.FontStyle
// Add these imports at the top
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon

@Composable
fun WelcomeScreen(
    onContinueClick: () -> Unit
) {
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
                    modifier = Modifier.padding(top = 0.dp)
                ) {

                    Text(
                        text = "Ứng dụng học tiếng nhật chất lượng",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Italic,
                        color = xanh_la_2
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    
                    Text(
                        text = "LearnJapanese",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.25f),
                                offset = Offset(2f, 2f),
                                blurRadius = 2f
                            )
                        ),
                        textAlign = TextAlign.Center,
                        color = xanh_la_1,
                        letterSpacing = 1.sp
                    )
                }
                
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(bottom = 20.dp)
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
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Next",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}