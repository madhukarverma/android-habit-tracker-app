package com.habbittracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.habbittracker.app.data.csv.CsvManager
import com.habbittracker.app.data.repository.AttendanceRepository
import com.habbittracker.app.data.repository.SettingsRepository
import com.habbittracker.app.data.repository.SubjectRepository
import com.habbittracker.app.navigation.NavGraph
import com.habbittracker.app.ui.theme.CreamBackground
import com.habbittracker.app.ui.theme.HabitTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val csvManager = CsvManager(applicationContext)
        val subjectRepository = SubjectRepository(csvManager)
        val attendanceRepository = AttendanceRepository(csvManager)
        val settingsRepository = SettingsRepository(applicationContext)

        setContent {
            HabitTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CreamBackground
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        csvManager = csvManager,
                        subjectRepository = subjectRepository,
                        attendanceRepository = attendanceRepository,
                        settingsRepository = settingsRepository
                    )
                }
            }
        }
    }
}
