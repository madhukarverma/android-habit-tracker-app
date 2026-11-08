package com.habbittracker.app.data.repository

import com.habbittracker.app.data.csv.CsvManager
import com.habbittracker.app.domain.model.Subject

class SubjectRepository(private val csvManager: CsvManager) {

    companion object {
        private const val FILE_NAME = "subjects.csv"
        private const val HEADERS = "id,name,icon,totalClasses,attendedClasses,skippedClasses,createdAt"
    }

    fun getAllSubjects(): List<Subject> {
        return csvManager.readLines(FILE_NAME).mapNotNull { line ->
            try {
                val parts = line.split(",")
                Subject(
                    id = parts[0],
                    name = parts[1],
                    icon = parts[2],
                    totalClasses = parts[3].toInt(),
                    attendedClasses = parts[4].toInt(),
                    skippedClasses = parts[5].toInt(),
                    createdAt = parts[6].toLong()
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    fun saveSubject(subject: Subject) {
        val subjects = getAllSubjects().toMutableList()
        val index = subjects.indexOfFirst { it.id == subject.id }
        if (index >= 0) {
            subjects[index] = subject
        } else {
            subjects.add(subject)
        }
        saveAll(subjects)
    }

    fun deleteSubject(subjectId: String) {
        val subjects = getAllSubjects().filter { it.id != subjectId }
        saveAll(subjects)
    }

    fun getSubject(subjectId: String): Subject? {
        return getAllSubjects().find { it.id == subjectId }
    }

    private fun saveAll(subjects: List<Subject>) {
        val lines = subjects.map { s ->
            "${s.id},${s.name},${s.icon},${s.totalClasses},${s.attendedClasses},${s.skippedClasses},${s.createdAt}"
        }
        csvManager.writeLines(FILE_NAME, HEADERS, lines)
    }
}
