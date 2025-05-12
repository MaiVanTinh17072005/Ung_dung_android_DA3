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
    
    val lightGreen = Color(0xFFF1F8E9)  // Much lighter green background (almost white)
    val mediumGreen = Color(0xFF81C784)  // Medium green for buttons
    val darkGreen = Color(0xFF2E7D32)    // Dark green for selected state
    
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
            // Tab switching buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectedType = CharacterType.HIRAGANA },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == CharacterType.HIRAGANA) 
                            darkGreen
                        else 
                            mediumGreen
                    ),
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                ) {
                    Text("Hiragana", color = Color.White)
                }
                Button(
                    onClick = { selectedType = CharacterType.KATAKANA },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == CharacterType.KATAKANA) 
                            darkGreen
                        else 
                            mediumGreen
                    ),
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                ) {
                    Text("Katakana", color = Color.White)
                }
            }

            // Character grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(12.dp),
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
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
                color = Color(0xFF1B5E20)  // Dark green for romaji
            )
        }
    }
}
