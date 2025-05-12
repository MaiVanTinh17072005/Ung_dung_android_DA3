import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangChuCaiScreen(
    viewModel: AlphabetViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val selectedType = viewModel.selectedType
    
    val lightGreen = Color(0xFFF1F8E9)
    val mediumGreen = Color(0xFF81C784)
    val darkGreen = Color(0xFF2E7D32)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bảng chữ cái Tiếng Nhật") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = lightGreen
                )
            )
        },
        containerColor = lightGreen
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.switchCharacterType(CharacterType.HIRAGANA) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == CharacterType.HIRAGANA) 
                            darkGreen else mediumGreen
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text("Hiragana", color = Color.White)
                }
                Button(
                    onClick = { viewModel.switchCharacterType(CharacterType.KATAKANA) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == CharacterType.KATAKANA) 
                            darkGreen else mediumGreen
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text("Katakana", color = Color.White)
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.characters) { char ->
                    CharacterCard(
                        character = char,
                        onClick = { viewModel.showCharacterDetail(it) }
                    )
                }
            }
        }
    }
    
    viewModel.selectedCharacter?.let { character ->
        CharacterDetailDialog(
            character = character,
            onDismiss = { viewModel.dismissCharacterDetail() }
        )
    }
}

@Composable
fun CharacterCard(
    character: JapaneseCharacter,
    onClick: (JapaneseCharacter) -> Unit
) {
    val mediaPlayer = remember { MediaPlayer() }
    
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(85.dp)
            .clickable { onClick(character) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFF66BB6A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = character.character,
                style = if (character.character.length > 1) 
                    MaterialTheme.typography.titleLarge
                else 
                    MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = character.romaji,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF1B5E20)
            )
            IconButton(
                onClick = {
                    mediaPlayer.apply {
                        reset()
                        setDataSource(character.audioUrl)
                        prepareAsync()
                        setOnPreparedListener { start() }
                    }
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Play pronunciation",
                    tint = Color(0xFF1B5E20)
                )
            }
        }
    }
}

@Composable
fun CharacterDetailDialog(
    character: JapaneseCharacter,
    onDismiss: () -> Unit
) {
    val mediaPlayer = remember { MediaPlayer() }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${character.character} - ${character.romaji}",
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(
                    onClick = {
                        mediaPlayer.apply {
                            reset()
                            setDataSource(character.audioUrl)
                            prepareAsync()
                            setOnPreparedListener { start() }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Play pronunciation"
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = character.strokeOrderUrl,
                    contentDescription = "Stroke order for ${character.character}",
                    modifier = Modifier.size(200.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
