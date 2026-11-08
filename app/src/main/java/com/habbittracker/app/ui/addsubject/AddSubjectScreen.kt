package com.habbittracker.app.ui.addsubject

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habbittracker.app.data.repository.SubjectRepository
import com.habbittracker.app.domain.model.Subject
import com.habbittracker.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectScreen(
    subjectRepository: SubjectRepository,
    onBack: () -> Unit
) {
    var subjectName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("📚") }

    val icons = listOf(
        "📚", "📐", "🧪", "💻", "🎨", "🎵",
        "📊", "🌍", "🔬", "📝", "🧮", "🏃",
        "📖", "🗣️", "⚗️", "🖥️", "✏️", "🎭"
    )

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
                text = "Add Subject",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Subject Name
        OutlinedTextField(
            value = subjectName,
            onValueChange = { subjectName = it },
            label = { Text("Subject Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                cursorColor = GreenPrimary,
                focusedLabelColor = GreenPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Icon Selection
        Text(
            text = "Choose an icon",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.height(160.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(icons) { icon ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (icon == selectedIcon) GreenPrimary.copy(alpha = 0.2f)
                            else CardBackground,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedIcon = icon },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Add Button
        Button(
            onClick = {
                if (subjectName.isNotBlank()) {
                    val subject = Subject(
                        name = subjectName.trim(),
                        icon = selectedIcon
                    )
                    subjectRepository.saveSubject(subject)
                    onBack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            enabled = subjectName.isNotBlank()
        ) {
            Text(
                text = "Add Subject",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
