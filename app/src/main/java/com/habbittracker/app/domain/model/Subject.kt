package com.habbittracker.app.domain.model

data class Subject(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val icon: String = "📚", // emoji icon
    val totalClasses: Int = 0,
    val attendedClasses: Int = 0,
    val skippedClasses: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    val attendancePercentage: Float
        get() = if (totalClasses > 0) (attendedClasses.toFloat() / totalClasses) * 100f else 0f

    val absentClasses: Int
        get() = totalClasses - attendedClasses - skippedClasses
}
