package com.habbittracker.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.habbittracker.app.data.csv.CsvManager
import com.habbittracker.app.data.repository.AttendanceRepository
import com.habbittracker.app.data.repository.SettingsRepository
import com.habbittracker.app.data.repository.SubjectRepository
import com.habbittracker.app.ui.addsubject.AddSubjectScreen
import com.habbittracker.app.ui.detail.SubjectDetailScreen
import com.habbittracker.app.ui.home.HomeScreen
import com.habbittracker.app.ui.settings.SettingsScreen
import com.habbittracker.app.ui.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object AddSubject : Screen("add_subject")
    object SubjectDetail : Screen("subject_detail/{subjectId}") {
        fun createRoute(subjectId: String) = "subject_detail/$subjectId"
    }
    object Settings : Screen("settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    csvManager: CsvManager,
    subjectRepository: SubjectRepository,
    attendanceRepository: AttendanceRepository,
    settingsRepository: SettingsRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                subjectRepository = subjectRepository,
                attendanceRepository = attendanceRepository,
                settingsRepository = settingsRepository,
                onAddSubject = { navController.navigate(Screen.AddSubject.route) },
                onSubjectClick = { subjectId ->
                    navController.navigate(Screen.SubjectDetail.createRoute(subjectId))
                },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.AddSubject.route) {
            AddSubjectScreen(
                subjectRepository = subjectRepository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SubjectDetail.route,
            arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: return@composable
            SubjectDetailScreen(
                subjectId = subjectId,
                subjectRepository = subjectRepository,
                attendanceRepository = attendanceRepository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                settingsRepository = settingsRepository,
                csvManager = csvManager,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
