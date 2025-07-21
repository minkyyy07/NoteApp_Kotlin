package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteapp.ui.theme.NoteAppTheme
import kotlin.math.roundToInt
import androidx.compose.material3.lightColorScheme
import com.example.noteapp.ui.theme.AppBackground
import com.example.noteapp.ui.theme.AppOnBackground
import com.example.noteapp.ui.theme.AppOnPrimary
import com.example.noteapp.ui.theme.AppPrimary
import com.example.noteapp.ui.theme.AppSecondary
import com.example.noteapp.ui.theme.AppSurface

val AppColorScheme = lightColorScheme(
    primary = AppPrimary,
    onPrimary = AppOnPrimary,
    secondary = AppSecondary,
    background = AppBackground,
    surface = AppSurface,
    onBackground = AppOnBackground,
    // можно добавить другие параметры по желанию
)

// Placeholder components - you'll need to implement these or use actual libraries
@Composable
fun CoilImage(
    imageModel: () -> String,
    imageOptions: ImageOptions,
    modifier: Modifier = Modifier
) {
    // Placeholder for actual Coil image implementation
    Box(modifier = modifier.background(Color.Gray))
}

data class ImageOptions(val contentScale: ContentScale)

@Composable
fun TextFieldView(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, style = textStyle) },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

class MainActivity : ComponentActivity() {
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAppTheme(
                darkTheme = false, // Можно сделать динамическим, если нужно
                dynamicColor = false // Используем свою палитру
            ) {
                NotesScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showArchive by remember { mutableStateOf(false) }
    var showStatistics by remember { mutableStateOf(false) }
    val searchQuery = viewModel.searchQuery.value
    val selectedCategory = viewModel.selectedCategory.value
    val notes = viewModel.notes
    val filteredNotes = notes.filter { note ->
        (!showArchive && !note.isArchived || showArchive && note.isArchived) &&
        (searchQuery.isEmpty() || note.title.contains(searchQuery, true) || note.content.contains(searchQuery, true)) &&
        (selectedCategory == "Все" || note.category == selectedCategory)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        // Top bar with archive/statistics buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (showArchive) "Архив" else "Мои заметки",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                IconButton(onClick = { showArchive = !showArchive }) {
                    Icon(Icons.Default.Archive, contentDescription = "Архив", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { showStatistics = true }) {
                    Icon(Icons.Default.BarChart, contentDescription = "Статистика", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        // Search and category filter
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        CategoryFilter(
            categories = viewModel.getAllCategories(),
            selectedCategory = selectedCategory,
            onCategorySelected = viewModel::updateSelectedCategory,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Dynamic note list
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!showArchive) {
                items(filteredNotes.filter { !it.isArchived }, key = { it.id }) { note ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        NoteItem(
                            note = note,
                            onDelete = { viewModel.deleteNote(note) },
                            onPin = { viewModel.togglePinNote(note.id) },
                            onArchive = { viewModel.archiveNote(note.id) },
                            onColorChange = { color -> viewModel.updateNoteColor(note.id, color) },
                            highlightPin = note.isPinned
                        )
                    }
                }
            } else {
                items(filteredNotes.filter { it.isArchived }, key = { it.id }) { note ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        ArchivedNoteItem(
                            note = note,
                            onUnarchive = { viewModel.unarchiveNote(note.id) },
                            onDelete = { viewModel.deleteNote(note) }
                        )
                    }
                }
            }
        }
        // Add note FAB
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить заметку")
            }
        }
    }
    if (showDialog) {
        AddNoteDialog(
            onDismiss = { showDialog = false },
            onSave = { title, content, category ->
                viewModel.addNote(title, content, category)
                showDialog = false
            },
            categories = viewModel.getAllCategories()
        )
    }
    if (showStatistics) {
        StatisticsDialog(
            statistics = viewModel.getNotesStatistics(),
            onDismiss = { showStatistics = false }
        )
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Поиск заметок", color = MaterialTheme.colorScheme.onSurface) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Поиск", tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Очистить", tint = MaterialTheme.colorScheme.error)
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            errorTextColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            errorLabelColor = MaterialTheme.colorScheme.error
        )
    )
}

@Composable
fun CategoryFilter(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category, color = MaterialTheme.colorScheme.onSurface) },
                leadingIcon = if (category == selectedCategory) {
                    {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                } else null,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp)),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onDelete: () -> Unit,
    onPin: () -> Unit,
    onArchive: () -> Unit,
    onColorChange: (NoteColor) -> Unit,
    highlightPin: Boolean
) {
    var showMenu by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    val density = LocalDensity.current

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(300),
        label = "swipe_animation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .graphicsLayer(
                    scaleX = if (highlightPin) 1.03f else 1f,
                    scaleY = if (highlightPin) 1.03f else 1f
                )
                .border(
                    width = if (highlightPin) 3.dp else 2.dp,
                    color = if (highlightPin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(
                defaultElevation = when {
                    highlightPin -> 18.dp
                    offsetX != 0f -> 14.dp
                    else -> 10.dp
                }
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Меню", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatDate(note.createdAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { showColorPicker = true }) {
                        Icon(Icons.Default.Palette, contentDescription = "Цвет заметки", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
                    }
                    if (highlightPin) {
                        Icon(Icons.Default.PushPin, contentDescription = "Закреплено", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
    if (showColorPicker) {
        ColorPickerDialog(
            currentColor = note.color,
            onColorSelected = {
                onColorChange(it)
                showColorPicker = false
            },
            onDismiss = { showColorPicker = false }
        )
    }
}

@Composable
fun ArchivedNoteItem(
    note: Note,
    onUnarchive: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onUnarchive) {
                        Text("Восстановить", color = MaterialTheme.colorScheme.primary)
                    }
                    TextButton(onClick = onDelete) {
                        Text("Удалить", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit,
    categories: List<String>
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Общие") }
    var showCategoryDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая заметка", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Заголовок", color = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorTextColor = MaterialTheme.colorScheme.error,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Содержимое", color = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorTextColor = MaterialTheme.colorScheme.error,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = { selectedCategory = it },
                    label = { Text("Категория", color = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorTextColor = MaterialTheme.colorScheme.error,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, content, selectedCategory)
                    }
                }
            ) {
                Text("Сохранить", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

@Composable
fun StatisticsDialog(
    statistics: NotesStatistics,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Статистика заметок", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column {
                Text("Всего заметок: ${statistics.totalNotes}", color = MaterialTheme.colorScheme.onSurface)
                Text("Закрепленных: ${statistics.pinnedNotes}", color = MaterialTheme.colorScheme.onSurface)
                Text("Архивированных: ${statistics.archivedNotes}", color = MaterialTheme.colorScheme.onSurface)
                Text("Категорий: ${statistics.categoriesCount}", color = MaterialTheme.colorScheme.onSurface)
                Text("Среднее слов в заметке: ${statistics.averageWordsPerNote.toInt()}", color = MaterialTheme.colorScheme.onSurface)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

// Функция для форматирования даты
fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val minutes = diff / (1000 * 60)
    val hours = diff / (1000 * 60 * 60)
    val days = diff / (1000 * 60 * 60 * 24)

    return when {
        minutes < 1 -> "Только что"
        minutes < 60 -> "${minutes}м назад"
        hours < 24 -> "${hours}ч назад"
        days < 7 -> "${days}д назад"
        else -> {
            val date = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
            date.format(java.util.Date(timestamp))
        }
    }
}

@Composable
fun ColorPickerDialog(
    currentColor: NoteColor,
    onColorSelected: (NoteColor) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Выберите цвет",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(NoteColor.values()) { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(color.color)
                            .border(
                                width = if (color == currentColor) 3.dp else 1.dp,
                                color = if (color == currentColor)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(color) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (color == currentColor) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Выбрано",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Готово", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}
