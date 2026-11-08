package com.habbittracker.app.data.repository

import com.habbittracker.app.data.csv.CsvManager
import com.habbittracker.app.domain.model.Attendance
import com.habbittracker.app.domain.model.AttendanceStatus

class AttendanceRepository(private val csvManager: CsvManager) {

    companion object {
        private const val FILE_NAME = "attendance.csv"
        private const val HEADERS = "id,subjectId,date,status,timestamp"
    }

    fun getAllAttendance(): List<Attendance> {
        return csvManager.readLines(FILE_NAME).mapNotNull { line ->
            try {
                val parts = line.split(",")
                Attendance(
                    id = parts[0],
                    subjectId = parts[1],
                    date = parts[2],
                    status = AttendanceStatus.fromString(parts[3]),
                    timestamp = parts[4].toLong()
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getAttendanceForSubject(subjectId: String): List<Attendance> {
        return getAllAttendance().filter { it.subjectId == subjectId }
    }

    fun getAttendanceForDate(date: String): List<Attendance> {
        return getAllAttendance().filter { it.date == date }
    }

    fun getAttendanceForSubjectOnDate(subjectId: String, date: String): Attendance? {
        return getAllAttendance().find { it.subjectId == subjectId && it.date == date }
    }

    fun markAttendance(attendance: Attendance) {
        val allAttendance = getAllAttendance().toMutableList()
        val existingIndex = allAttendance.indexOfFirst {
            it.subjectId == attendance.subjectId && it.date == attendance.date
        }
        if (existingIndex >= 0) {
            allAttendance[existingIndex] = attendance
        } else {
            allAttendance.add(attendance)
        }
        saveAll(allAttendance)
    }

    fun deleteAttendanceForSubject(subjectId: String) {
        val allAttendance = getAllAttendance().filter { it.subjectId != subjectId }
        saveAll(allAttendance)
    }

    private fun saveAll(attendanceList: List<Attendance>) {
        val lines = attendanceList.map { a ->
            "${a.id},${a.subjectId},${a.date},${a.status.name},${a.timestamp}"
        }
        csvManager.writeLines(FILE_NAME, HEADERS, lines)
    }
}
