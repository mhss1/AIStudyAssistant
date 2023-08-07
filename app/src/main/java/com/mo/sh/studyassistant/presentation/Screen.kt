package com.mo.sh.studyassistant.presentation

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Tutor: Screen("tutor_screen?imageUri={imageUri}&text={text}")
    object Summarize: Screen("summarize_screen?pdfUri={pdfUri}")
    object Writer: Screen("writer_screen")
    object Questions: Screen("questions_screen")
    object Settings: Screen("settings_screen")
}