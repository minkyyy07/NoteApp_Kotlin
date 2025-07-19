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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteapp.ui.theme.NoteAppTheme
import kotlin.math.roundToInt

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
            NoteAppTheme {
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
    val textField1 = remember { mutableStateOf("") }

    // Убираем старый Scaffold с TopAppBar и FAB, используем только новый интерфейс
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xFFFFFFFF),
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = Color(0xFFD7BCE8),
                )
                .verticalScroll(rememberScrollState())
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 1.dp,bottom = 10.dp,)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0x00FFFFFF), Color(0x00FFFFFF), ),
                            start = Offset.Zero,
                            end = Offset(0F,Float.POSITIVE_INFINITY),
                        )
                    )
                    .padding(vertical = 11.dp,horizontal = 20.dp,)
            ){
                CoilImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/fUOlzGc9aM/edv8vwg7_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .width(40.dp)
                        .height(21.dp)
                )
                CoilImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/fUOlzGc9aM/hp77ldez_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .width(66.dp)
                        .height(11.dp)
                )
            }
            Text("Hi, Zain",
                color = Color(0xFF000000),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp,)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 26.dp,start = 20.dp,end = 20.dp,)
                    .fillMaxWidth()
            ){
                Text("Good Morning",
                    color = Color(0xFF000000),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 5.dp,)
                ){
                    Column(
                        modifier = Modifier
                            .padding(bottom = 5.dp,)
                            .border(
                                width = 2.dp,
                                color = Color(0xFF33363F),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .clip(shape = RoundedCornerShape(1.dp))
                            .width(6.dp)
                            .height(6.dp)
                    ){
                    }
                    Column(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = Color(0xFF33363F),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .clip(shape = RoundedCornerShape(1.dp))
                            .width(6.dp)
                            .height(6.dp)
                    ){
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Column(
                        modifier = Modifier
                            .padding(bottom = 5.dp,)
                            .border(
                                width = 2.dp,
                                color = Color(0xFF33363F),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .clip(shape = RoundedCornerShape(1.dp))
                            .width(6.dp)
                            .height(6.dp)
                    ){
                    }
                    Column(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = Color(0xFF33363F),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .clip(shape = RoundedCornerShape(1.dp))
                            .width(6.dp)
                            .height(6.dp)
                    ){
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 22.dp,start = 23.dp,end = 23.dp,)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFDE2FF),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .shadow(
                        elevation = 2.dp,
                        spotColor = Color(0x4D000000),
                    )
                    .padding(10.dp)
            ){
                CoilImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/fUOlzGc9aM/0q3fl6sg_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .padding(end = 1.dp,)
                        .width(29.dp)
                        .height(29.dp)
                )
                TextFieldView(
                    placeholder = "Search note",
                    value = textField1.value,
                    onValueChange = { newText -> textField1.value = newText },
                    textStyle = TextStyle(
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 19.dp,start = 20.dp,)
            ){
                Text("All Notes",
                    color = Color(0xFF5C5C5C),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 4.dp,)
                )
                CoilImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/fUOlzGc9aM/4ak8gejv_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .width(27.dp)
                        .height(27.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 19.dp,start = 26.dp,end = 26.dp,)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFDE2FF),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(top = 7.dp,bottom = 13.dp,start = 12.dp,)
            ){
                Column(
                ){
                    Text("Documents",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("10 May | Passport",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 19.dp,start = 23.dp,end = 23.dp,)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFDE2FF),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(top = 8.dp,bottom = 14.dp,start = 17.dp,)
            ){
                Column(
                ){
                    Text("Documents",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("12 May | Passport",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(horizontal = 1.dp,)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 19.dp,start = 24.dp,end = 24.dp,)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFDE2FF),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(top = 8.dp,bottom = 14.dp,start = 16.dp,)
            ){
                Column(
                ){
                    Text("Documents",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("10 May | Passport",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 19.dp,start = 24.dp,end = 24.dp,)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFDE2FF),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(top = 8.dp,bottom = 14.dp,start = 16.dp,)
            ){
                Column(
                ){
                    Text("Documents",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("11 May | Passport",
                        color = Color(0xFF000000),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(horizontal = 3.dp,)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 19.dp,start = 24.dp,end = 24.dp,)
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .padding(bottom = 19.dp,)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(15.dp))
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFFDE2FF),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(top = 8.dp,bottom = 14.dp,start = 16.dp,)
                        ){
                            Column(
                            ){
                                Text("Documents",
                                    color = Color(0xFF000000),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text("12 May | Passport",
                                    color = Color(0xFF000000),
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 1.dp,)
                                )
                            }
                        }
                    }
                    CoilImage(
                        imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/fUOlzGc9aM/58delw3i_expires_30_days.png"},
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        modifier = Modifier
                            .offset(x = -18.dp, y = 34.dp)
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 34.dp)
                            .width(50.dp)
                            .height(50.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFDE2FF),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(top = 8.dp,bottom = 14.dp,start = 16.dp,)
                ){
                    Column(
                    ){
                        Text("Documents",
                            color = Color(0xFF000000),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text("11 May | Passport",
                            color = Color(0xFF000000),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(horizontal = 3.dp,)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 18.dp,start = 26.dp,end = 26.dp,)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFDE2FF),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(top = 8.dp,bottom = 27.dp,)
                    ){
                        Text("Documents",
                            color = Color(0xFF000000),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 14.dp,)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .offset(x = -26.dp, y = 18.dp)
                        .align(Alignment.BottomStart)
                        .padding(bottom = 18.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF5D576B),
                        )
                        .padding(top = 3.dp,bottom = 4.dp,start = 117.dp,)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(end = 89.dp,)
                    ){
                        CoilImage(
                            imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/fUOlzGc9aM/c7wxs7pu_expires_30_days.png"},
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                            modifier = Modifier
                                .width(35.dp)
                                .height(35.dp)
                        )
                        Text("Notes",
                            color = Color(0xFF8884FF),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            CoilImage(
                                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/fUOlzGc9aM/366t01p0_expires_30_days.png"},
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp)
                            )
                            Text("To-do",
                                color = Color(0xFFFFFFFF),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }

    // Сохраняем диалоги из старого интерфейса
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
        label = { Text("Поиск заметок") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Поиск")
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Очистить")
                }
            }
        },
        modifier = modifier.fillMaxWidth()
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
                label = { Text(category) }
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
    onColorChange: (NoteColor) -> Unit
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
            .clip(MaterialTheme.shapes.large)
            .background(note.color.color)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.large)
            .shadow(4.dp, MaterialTheme.shapes.large)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX += dragAmount
                    },
                    onDragEnd = {
                        if (offsetX > 200) onPin()
                        else if (offsetX < -200) onArchive()
                        offsetX = 0f
                    }
                )
            }
            .clickable { showMenu = true },
        colors = CardDefaults.cardColors(containerColor = note.color.color)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
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
                    Icon(Icons.Default.Palette, contentDescription = "Цвет заметки")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onUnarchive) {
                    Text("Восстановить")
                }
                TextButton(onClick = onDelete) {
                    Text("Удалить")
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
        title = { Text("Новая заметка") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Заголовок") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Содержимое") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = { selectedCategory = it },
                    label = { Text("Категория") },
                    modifier = Modifier.fillMaxWidth()
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
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
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
        title = { Text("Статистика заметок") },
        text = {
            Column {
                Text("Всего заметок: ${statistics.totalNotes}")
                Text("Закрепленных: ${statistics.pinnedNotes}")
                Text("Архивированных: ${statistics.archivedNotes}")
                Text("Категорий: ${statistics.categoriesCount}")
                Text("Среднее слов в заметке: ${statistics.averageWordsPerNote.toInt()}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
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
                style = MaterialTheme.typography.headlineSmall
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
                Text("Готово")
            }
        }
    )
}
