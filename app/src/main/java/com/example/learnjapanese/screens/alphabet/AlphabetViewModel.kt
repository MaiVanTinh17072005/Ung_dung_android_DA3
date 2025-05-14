package com.example.learnjapanese.screens.alphabet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.learnjapanese.data.AlphabetData
import com.example.learnjapanese.data.CharacterType
import com.example.learnjapanese.data.JapaneseCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlphabetViewModel @Inject constructor() : ViewModel() {
    var selectedType by mutableStateOf(CharacterType.HIRAGANA)
        private set
        
    var selectedCharacter by mutableStateOf<JapaneseCharacter?>(null)
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
    
    fun showCharacterDetail(character: JapaneseCharacter) {
        selectedCharacter = character
    }
    
    fun dismissCharacterDetail() {
        selectedCharacter = null
    }
}