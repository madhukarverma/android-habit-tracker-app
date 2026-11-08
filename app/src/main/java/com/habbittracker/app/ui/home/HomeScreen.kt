package com.habbittracker.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habbittracker.app.data.repository.AttendanceRepository
import com.habbittracker.app.data.repository.SettingsRepository
import com.habbittracker.app.data.repository.SubjectRepository
import com.habbittracker.app.domain.model.Attendance
import com.habbittracker.app.domain.model.AttendanceStatus
import com.habbittracker.app.domain.model.Subject
import com.habbittracker.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    subjectRepository: SubjectRepository,
    attendanceRepository: AttendanceRepository,
    settingsRepository: SettingsRepository,
    onAddSubject: () -> Unit,
    onSubjectClick: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var subjects by remember { mutableStateOf(subjectRepository.getAllSubjects()) }
    var attendanceMap by remember { mutableStateOf<Map<String, AttendanceStatus?>>(emptyMap()) }
    val userName = remember { settingsRepository.getUserName() }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Refresh data
    fun refreshData() {
        subjects = subjectRepository.getAllSubjects()
        val dateStr = selectedDate.format(dateFormatter)
        val todayAttendance = attendanceRepository.getAttendanceForDate(dateStr)
        attendanceMap = subjects.associate { subject ->
            subject.id to todayAttendance.find { it.subjectId == subject.id }?.status
        }
    }

    LaunchedEffect(selectedDate) {
        refreshData()
    }

    val motivationalTips = listOf(
        "START YOUR DAY WITH WATER. ONE GLASS WILL ENERGIZE YOU!",
        "CONSISTENCY IS KEY. SHOW UP EVERY DAY!",
        "SMALL STEPS LEAD TO BIG CHANGES!",
        "YOU'RE DOING GREAT. KEEP IT UP!",
        "ATTENDANCE MATTERS. BE PRESENT!"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        // Greeting
        Text(
            text = "Hey, $userName!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(start = 16.dp, top = 48.dp, bottom = 8.dp)
        )

        // Calendar Strip
        CalendarStrip(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        // Motivational tip
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(LightGreen.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = motivationalTips.random(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                lineHeight = 18.sp
            )
        }

        // Subject list
        if (subjects.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📚", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No subjects yet!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDark.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Tap + New Subject to get started",
                        fontSize = 14.sp,
                        color = TextDark.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(subjects) { subject ->
                    SubjectCard(
                        subject = subject,
                        todayStatus = attendanceMap[subject.id],
                        onCardClick = { onSubjectClick(subject.id) },
                        onMarkAttendance = { status ->
                            val dateStr = selectedDate.format(dateFormatter)
                            val attendance = Attendance(
                                subjectId = subject.id,
                                date = dateStr,
                                status = status
                            )
                            attendanceRepository.markAttendance(attendance)

                            // Update subject counts
                            val updatedSubject = when (status) {
                                AttendanceStatus.PRESENT -> subject.copy(
                                    totalClasses = subject.totalClasses + 1,
                                    attendedClasses = subject.attendedClasses + 1
                                )
                                AttendanceStatus.ABSENT -> subject.copy(
                                    totalClasses = subject.totalClasses + 1
                                )
                                AttendanceStatus.SKIPPED -> subject.copy(
                                    totalClasses = subject.totalClasses + 1,
                                    skippedClasses = subject.skippedClasses + 1
                                )
                            }
                            subjectRepository.saveSubject(updatedSubject)
                            refreshData()
                        }
                    )
                }
            }
        }

        // Bottom Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBackground, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // New Subject button
            Box(
                modifier = Modifier
                    .background(GreenPrimary, RoundedCornerShape(24.dp))
                    .clickable { onAddSubject() }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "+ New subject",
                    color = TextLight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Settings button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(TextLight.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .clickable { onSettingsClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⚙", fontSize = 20.sp)
                }
            }
        }
    }
}
