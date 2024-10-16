package com.devjsg.cj_logistics_future_technology.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.presentation.auth.SignUpScreen
import com.devjsg.cj_logistics_future_technology.presentation.auth.TermsScreen
import com.devjsg.cj_logistics_future_technology.presentation.detail.DetailMemberScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.AdminHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.ContestScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.worker.WorkerHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.login.LoginScreen
import com.devjsg.cj_logistics_future_technology.presentation.map.MapsScreen
import com.devjsg.cj_logistics_future_technology.presentation.splash.SplashScreen
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MyHeartRateViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    biometricPromptHelper: BiometricPromptHelper,
    myHeartRateViewModel: MyHeartRateViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController = navController) }
        composable("terms") { TermsScreen(navController = navController) }
        composable("sign_up") { SignUpScreen(navController = navController) }
        composable("login") {
            LoginScreen(
                navController = navController,
                biometricPromptHelper = biometricPromptHelper
            )
        }
        composable("admin_home") { AdminHomeScreen(navController = navController) }
        composable("contest_home") { ContestScreen(navController = navController) }
        composable("worker_home") {
            WorkerHomeScreen(
                navController = navController,
                myHeartRateViewModel = myHeartRateViewModel
            )
        }
        composable(
            route = "maps/{employeeName}/{latitude}/{longitude}/{age}/{phone}/{createdAt}",
            arguments = listOf(
                navArgument("employeeName") { type = NavType.StringType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType },
                navArgument("age") { type = NavType.IntType },
                navArgument("phone") { type = NavType.StringType },
                navArgument("createdAt") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble() ?: 0.0
            val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble() ?: 0.0
            val age = backStackEntry.arguments?.getInt("age") ?: 0
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val employeeName = backStackEntry.arguments?.getString("employeeName") ?: ""
            val createdAt = backStackEntry.arguments?.getString("createdAt") ?: ""
            MapsScreen(
                employeeName = employeeName,
                latitude = latitude,
                longitude = longitude,
                age = age,
                phone = phone,
                createdAt = createdAt
            )
        }
        composable(
            route = "detail_member/{memberId}",
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            DetailMemberScreen(navController= navController,memberId = memberId)
        }
    }
}