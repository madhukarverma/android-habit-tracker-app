package com.habbittracker.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habbittracker.app.domain.model.AttendanceStatus
import com.habbittracker.app.domain.model.Subject
import com.habbittracker.app.ui.theme.*

@Composable
fun SubjectCard(
    subject: Subject,
    todayStatus: AttendanceStatus?,
    onCardClick: () -> Unit,
    onMarkAttendance: (AttendanceStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Text(
                text = subject.icon,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            // Name and status
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDark,
                    textDecoration = if (todayStatus == AttendanceStatus.SKIPPED)
                        TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(4.dp))
                when (todayStatus) {
                    AttendanceStatus.PRESENT -> StatusBadge("✓ Done", StatusDone)
                    AttendanceStatus.SKIPPED -> StatusBadge("↩ Skip", StatusSkip)
                    AttendanceStatus.ABSENT -> StatusBadge("✗ Absent", StatusNew)
                    null -> StatusBadge("✦ New", StatusNew)
                }
            }

            // Attendance info
            Column(horizontalAlignment = Alignment.End) {
                if (todayStatus == null) {
                    // Quick action buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        QuickActionButton("✓", StatusDone) {
                            onMarkAttendance(AttendanceStatus.PRESENT)
                        }
                        QuickActionButton("✗", StatusNew) {
                            onMarkAttendance(AttendanceStatus.ABSENT)
                        }
                        QuickActionButton("↩", StatusSkip) {
                            onMarkAttendance(AttendanceStatus.SKIPPED)
                        }
                    }
                } else {
                    Text(
                        text = "${subject.attendedClasses}/${subject.totalClasses}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "classes",
                        fontSize = 11.sp,
                        color = TextDark.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(text: String, color: androidx.compose.ui.graphics.Color) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = color
    )
}

@Composable
private fun QuickActionButton(
    icon: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 16.sp, color = color)
    }
}
