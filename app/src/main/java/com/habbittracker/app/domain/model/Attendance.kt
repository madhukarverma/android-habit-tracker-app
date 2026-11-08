package com.habbittracker.app.domain.model

data class Attendance(
    val id: String = java.util.UUID.randomUUID().toString(),
    val subjectId: String,
    val date: String, // ISO format YYYY-MM-DD
    val status: AttendanceStatus,
    val timestamp: Long = System.currentTimeMillis()
)

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    SKIPPED;

    companion object {
        fun fromString(value: String): AttendanceStatus {
            return when (value.uppercase()) {
                "PRESENT" -> PRESENT
                "ABSENT" -> ABSENT
                "SKIPPED" -> SKIPPED
                else -> ABSENT
            }
        }
    }
}
