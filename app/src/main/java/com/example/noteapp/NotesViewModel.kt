package com.example.noteapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Date

enum class SortOrder {
    DATE_NEWEST,
    DATE_OLDEST,
    TITLE_A_Z,
    TITLE_Z_A
}

class NotesViewModel : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> = _notes

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _sortOrder = mutableStateOf(SortOrder.DATE_NEWEST)
    val sortOrder: State<SortOrder> = _sortOrder

    private val _selectedCategory = mutableStateOf("Все")
    val selectedCategory: State<String> = _selectedCategory

    fun addNote(title: String, content: String = "", category: String = "Общие") {
        _notes.add(Note(title = title, content = content, category = category))
    }

    fun deleteNote(note: Note) {
        _notes.remove(note)
    }

    fun updateNote(noteId: Long, newTitle: String, newContent: String) {
        val index = _notes.indexOfFirst { it.id == noteId }
        if (index != -1) {
            _notes[index] = _notes[index].copy(
                title = newTitle,
                content = newContent,
                updatedAt = System.currentTimeMillis()
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    val filteredNotes: List<Note>
        get() {
            var filtered = if (_selectedCategory.value == "Все") {
                _notes.filter { !it.isArchived }
            } else {
                _notes.filter { it.category == _selectedCategory.value && !it.isArchived }
            }

            if (_searchQuery.value.isNotBlank()) {
                filtered = filtered.filter { note ->
                    note.title.contains(_searchQuery.value, ignoreCase = true) ||
                    note.content.contains(_searchQuery.value, ignoreCase = true)
                }
            }

            return when (_sortOrder.value) {
                SortOrder.DATE_NEWEST -> filtered.sortedByDescending { it.updatedAt }
                SortOrder.DATE_OLDEST -> filtered.sortedBy { it.updatedAt }
                SortOrder.TITLE_A_Z -> filtered.sortedBy { it.title }
                SortOrder.TITLE_Z_A -> filtered.sortedByDescending { it.title }
            }
        }

    fun togglePinNote(noteId: Long) {
        val index = _notes.indexOfFirst { it.id == noteId }
        if (index != -1) {
            _notes[index] = _notes[index].copy(isPinned = !_notes[index].isPinned)
        }
    }

    val pinnedNotes: List<Note>
        get() = _notes.filter { it.isPinned && !it.isArchived }

    fun archiveNote(noteId: Long) {
        val index = _notes.indexOfFirst { it.id == noteId }
        if (index != -1) {
            _notes[index] = _notes[index].copy(isArchived = false)
        }
    }
    val archivedNotes: List<Note>
        get() = _notes.filter { it.isArchived }

    // Color
    fun updateNoteColor(noteId: Long, color: NoteColor) {
        val index = _notes.indexOfFirst { it.id == noteId }
        if (index != -1) {
            _notes[index] = _notes[index].copy(color = color)
        }
    }

    // Category
    fun getUniqueCategories(): List<String> {
        return _notes.map { it.category }.distinct().sorted()
    }

    fun getAllCategories(): List<String> {
        return listOf("Все") + getUniqueCategories()
    }

    // Stats
    fun getNotesStatistics(): NotesStatistics {
        return NotesStatistics(
            totalNotes = _notes.filter { !it.isArchived }.size,
            pinnedNotes = _notes.count { it.isPinned && !it.isArchived },
            archivedNotes = _notes.count { it.isArchived },
            categoriesCount = getUniqueCategories().size,
            averageWordsPerNote = if (_notes.isNotEmpty()) {
                _notes.filter { !it.isArchived }.map { it.content.split(" ").size }.average()
            } else 0.0
        )
    }

    // Export
    fun exportNotesToText(): String {
        return _notes.filter { !it.isArchived }.joinToString("\n") { note ->
            "=== ${note.title} ===\n${note.content}\n[${Date(note.createdAt)}]\nКатегория: ${note.category}"
        }
    }
}