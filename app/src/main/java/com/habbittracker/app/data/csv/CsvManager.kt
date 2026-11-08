package com.habbittracker.app.data.csv

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class CsvManager(private val context: Context) {

    private fun getFile(fileName: String): File {
        val dir = File(context.filesDir, "data")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, fileName)
    }

    fun writeLines(fileName: String, headers: String, lines: List<String>) {
        val file = getFile(fileName)
        FileWriter(file, false).use { writer ->
            writer.write(headers + "\n")
            lines.forEach { line ->
                writer.write(line + "\n")
            }
        }
    }

    fun appendLine(fileName: String, headers: String, line: String) {
        val file = getFile(fileName)
        val exists = file.exists() && file.length() > 0
        FileWriter(file, true).use { writer ->
            if (!exists) {
                writer.write(headers + "\n")
            }
            writer.write(line + "\n")
        }
    }

    fun readLines(fileName: String): List<String> {
        val file = getFile(fileName)
        if (!file.exists()) return emptyList()
        val lines = mutableListOf<String>()
        BufferedReader(FileReader(file)).use { reader ->
            var first = true
            reader.forEachLine { line ->
                if (first) { first = false; return@forEachLine } // skip header
                if (line.isNotBlank()) lines.add(line)
            }
        }
        return lines
    }

    fun getExportFile(fileName: String): File = getFile(fileName)

    fun clearFile(fileName: String) {
        val file = getFile(fileName)
        if (file.exists()) file.delete()
    }
}
