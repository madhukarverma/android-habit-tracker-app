# 🌱 Habit Tracker & Attendance Android App (v0.1)

A modular, clean Android attendance and habit tracking application built with **Kotlin** and **Jetpack Compose**. Designed with the cheerful, minimalist Moe character theme inspired by Figma visual designs.

---

## 🎨 Theme & Color Palette

- **Dark Background / Accents:** `#232323`
- **Primary Green:** `#5DA65C`
- **Cream Background:** `#F8FAED`
- **Soft Green Accent:** `#A8D5A2`
- **New / Absent Badge:** `#E85D3A`
- **Skip Badge:** `#7BB8D6`

---

## 📱 Features

1. **Splash Screen**: Bold branding with animated transition to the home screen.
2. **Interactive Calendar Strip**: Horizontal scrolling date picker with today indicator and day-of-week labels.
3. **Subject Management (CRUD)**:
   - Add new subjects with emoji icon selection (📚, 📐, 🧪, 💻, 🎨, etc.).
   - View subject cards with attendance statistics (`attended / total classes`).
   - Delete subjects with cascading removal of attendance records.
4. **Quick Attendance Marking**:
   - Mark **Present** (`✓ Done`), **Absent** (`✗`), or **Skipped** (`↩`) with a single tap.
   - Interactive status styling reflecting marked status for each date.
5. **Subject Detail Screen**:
   - Detailed statistics: Total classes, Present count, Absent count, Skipped count.
   - Attendance percentage gauge (green for ≥ 75%, warning for < 75%).
   - Chronological attendance history log.
   - Helpful tips section.
6. **CSV Data Persistence**:
   - Stores all data locally in standard CSV format (`subjects.csv` and `attendance.csv`) in internal app storage.
   - Easy parsing and zero dependency on complex database drivers.
7. **Settings & CSV Data Export**:
   - Customizable user name ("Hey, [Name]!").
   - Instant export of all subject and attendance CSV data through Android system share sheet.

---

## 🏗️ Architecture

Following clean, modular architecture:
- `data/`: CSV file management (`CsvManager`), repository implementations (`SubjectRepository`, `AttendanceRepository`, `SettingsRepository`).
- `domain/`: Pure Kotlin data models (`Subject`, `Attendance`, `AttendanceStatus`).
- `ui/`: Jetpack Compose screens and design tokens (`theme/`, `splash/`, `home/`, `detail/`, `addsubject/`, `settings/`).
- `navigation/`: Jetpack Navigation graph (`NavGraph.kt`).

---

## 🛠️ Build Requirements

- **Android SDK**: Compile SDK 34, Min SDK 26
- **JDK**: Java 17 (Microsoft OpenJDK 17 LTS)
- **Gradle**: 8.2+
- **Build Tool**: Android Gradle Plugin 8.2.0, Kotlin 1.9.20, Jetpack Compose Compiler 1.5.5
