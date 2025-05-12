import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AlphabetViewModel : ViewModel() {
    var selectedType by mutableStateOf(CharacterType.HIRAGANA)
        private set

    val characters: List<JapaneseCharacter>
        get() = if (selectedType == CharacterType.HIRAGANA) {
            AlphabetData.hiragana
        } else {
            AlphabetData.katakana
        }

    fun switchCharacterType(type: CharacterType) {
        selectedType = type
    }
}