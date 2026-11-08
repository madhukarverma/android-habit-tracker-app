package com.habbittracker.app.ui.detail

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
import com.habbittracker.app.data.repository.SubjectRepository
import com.habbittracker.app.domain.model.Attendance
import com.habbittracker.app.domain.model.AttendanceStatus
import com.habbittracker.app.domain.model.Subject
import com.habbittracker.app.ui.theme.*

@Composable
fun SubjectDetailScreen(
    subjectId: String,
    subjectRepository: SubjectRepository,
    attendanceRepository: AttendanceRepository,
    onBack: () -> Unit
) {
    var subject by remember { mutableStateOf(subjectRepository.getSubject(subjectId)) }
    var attendanceHistory by remember {
        mutableStateOf(attendanceRepository.getAttendanceForSubject(subjectId))
    }

    if (subject == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Subject not found")
        }
        return
    }

    val s = subject!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
            Text(text = s.icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = s.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "✕",
                fontSize = 20.sp,
                color = StatusNew,
                modifier = Modifier
                    .clickable {
                        subjectRepository.deleteSubject(subjectId)
                        attendanceRepository.deleteAttendanceForSubject(subjectId)
                        onBack()
                    }
                    .padding(8.dp)
            )
        }

        // Stats Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                label = "Total",
                value = s.totalClasses.toString(),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Present",
                value = s.attendedClasses.toString(),
                color = StatusDone,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Absent",
                value = s.absentClasses.toString(),
                color = StatusNew,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Skipped",
                value = s.skippedClasses.toString(),
                color = StatusSkip,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Attendance percentage
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(GreenPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Attendance",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${String.format("%.1f", s.attendancePercentage)}%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (s.attendancePercentage >= 75f) StatusDone else StatusNew
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Attendance History header
        Text(
            text = "Attendance History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // History list
        if (attendanceHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No attendance records yet",
                    color = TextDark.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(attendanceHistory.sortedByDescending { it.date }) { attendance ->
                    AttendanceHistoryItem(attendance)
                }
            }
        }

        // Helpful Tips
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Helpful tips",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🎯 Aim for at least 75% attendance",
                fontSize = 14.sp,
                color = TextDark.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "📅 Mark attendance daily for accurate tracking",
                fontSize = 14.sp,
                color = TextDark.copy(alpha = 0.7f)
            )
        }

        // Finish Button
        Button(
            onClick = { onBack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
        ) {
            Text(
                text = "Finish",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color = TextDark,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextDark.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun AttendanceHistoryItem(attendance: Attendance) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = attendance.date,
            fontSize = 14.sp,
            color = TextDark
        )
        val (statusText, statusColor) = when (attendance.status) {
            AttendanceStatus.PRESENT -> "✓ Present" to StatusDone
            AttendanceStatus.ABSENT -> "✗ Absent" to StatusNew
            AttendanceStatus.SKIPPED -> "↩ Skipped" to StatusSkip
        }
        Text(
            text = statusText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = statusColor
        )
    }
}
