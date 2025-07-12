package com.example.noteapp

import androidx.compose.ui.graphics.Color

enum class NoteColor(val color: Color) {
    WHITE(Color.White),
    YELLOW(Color(0xFFFFF59D)),
    BLUE(Color(0xFFBBDEFB)),
    GREEN(Color(0xFFC8E6C9)),
    PINK(Color(0xFFF8BBD9)),
    PURPLE(Color(0xFFE1BEE7))
}

data class Note(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val content: String,
    val category: String = "Общие",
    val color: NoteColor = NoteColor.WHITE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val reminderTime: Long? = null,
    val tags: List<String> = emptyList()
)

data class NotesStatistics(
    val totalNotes: Int,
    val pinnedNotes: Int,
    val archivedNotes: Int,
    val categoriesCount: Int,
    var averageWordsPerNote: Double
)