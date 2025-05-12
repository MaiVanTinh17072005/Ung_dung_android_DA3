import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangChuCaiScreen(
    onBack: () -> Unit = {}
) {
    var selectedType by remember { mutableStateOf(CharacterType.HIRAGANA) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bảng chữ cái Nhật") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab switching buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectedType = CharacterType.HIRAGANA },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == CharacterType.HIRAGANA) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("Hiragana")
                }
                Button(
                    onClick = { selectedType = CharacterType.KATAKANA },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == CharacterType.KATAKANA) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("Katakana")
                }
            }

            // Character grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(if (selectedType == CharacterType.HIRAGANA) 
                    AlphabetData.hiragana 
                else 
                    AlphabetData.katakana
                ) { char ->
                    CharacterCard(character = char)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: JapaneseCharacter) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(85.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = character.romaji,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
