package com.habbittracker.app.ui.settings

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.habbittracker.app.data.csv.CsvManager
import com.habbittracker.app.data.repository.SettingsRepository
import com.habbittracker.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    csvManager: CsvManager,
    onBack: () -> Unit
) {
    var userName by remember { mutableStateOf(settingsRepository.getUserName()) }
    var showNameDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Name setting
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    editName = userName
                    showNameDialog = true
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Name",
                        fontSize = 14.sp,
                        color = TextDark.copy(alpha = 0.6f)
                    )
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                }
                Text(text = "✏️", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Export CSV
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val subjectsFile = csvManager.getExportFile("subjects.csv")
                    val attendanceFile = csvManager.getExportFile("attendance.csv")
                    val shareText = buildString {
                        appendLine("=== SUBJECTS ===")
                        if (subjectsFile.exists()) append(subjectsFile.readText())
                        appendLine("\n=== ATTENDANCE ===")
                        if (attendanceFile.exists()) append(attendanceFile.readText())
                    }
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Export CSV Data"))
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Export Data",
                        fontSize = 14.sp,
                        color = TextDark.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Share CSV data",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                }
                Text(text = "📤", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // About
        Text(
            text = "Habit Tracker v0.1",
            fontSize = 14.sp,
            color = TextDark.copy(alpha = 0.4f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Attendance tracking made simple",
            fontSize = 12.sp,
            color = TextDark.copy(alpha = 0.3f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    // Name edit dialog
    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Change Name") },
            text = {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Your name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        cursorColor = GreenPrimary
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (editName.isNotBlank()) {
                        settingsRepository.setUserName(editName.trim())
                        userName = editName.trim()
                        showNameDialog = false
                    }
                }) {
                    Text("Save", color = GreenPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
