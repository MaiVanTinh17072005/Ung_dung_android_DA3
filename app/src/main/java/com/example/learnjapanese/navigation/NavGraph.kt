package com.example.learnjapanese.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learnjapanese.screens.DashboardScreen
import com.example.learnjapanese.screens.login.LoginScreen
import com.example.learnjapanese.screens.register.RegisterScreen
import com.example.learnjapanese.screens.welcome.WelcomeScreen
import com.example.learnjapanese.screens.forgotpassword.ForgotPasswordScreen
import com.example.learnjapanese.screens.changepassword.ChangePasswordScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onContinueClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Login.route) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSubmitOtp = {
                    // Navigate to change password screen
                    navController.navigate(Screen.ChangePassword.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = false }
                    }
                }
            )
        }
        
        composable(route = Screen.ChangePassword.route) {
            ChangePasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPasswordChanged = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToVocabulary = {
                    // Điều hướng đến màn hình Vocabulary
                },
                onNavigateToGrammar = {
                    // Điều hướng đến màn hình Grammar
                },
                onNavigateToAlphabet = {
                    // Điều hướng đến màn hình Alphabet
                },
                onNavigateToReading = {
                    // Điều hướng đến màn hình Reading
                },
                onNavigateToAccount = {
                    // Điều hướng đến màn hình Account
                },
                onNavigateToChat = {
                    // Điều hướng đến màn hình Chat
                }
            )
        }
    }
} 