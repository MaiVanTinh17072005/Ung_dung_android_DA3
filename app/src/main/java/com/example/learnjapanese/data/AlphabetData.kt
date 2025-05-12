data class JapaneseCharacter(
    val character: String,
    val romaji: String,
    val type: CharacterType
)

enum class CharacterType {
    HIRAGANA, KATAKANA
}

object AlphabetData {
    val hiragana = listOf(
        // Nguyên âm
        JapaneseCharacter("あ", "a", CharacterType.HIRAGANA),
        JapaneseCharacter("い", "i", CharacterType.HIRAGANA),
        JapaneseCharacter("う", "u", CharacterType.HIRAGANA),
        JapaneseCharacter("え", "e", CharacterType.HIRAGANA),
        JapaneseCharacter("お", "o", CharacterType.HIRAGANA),
        
        // K-series
        JapaneseCharacter("か", "ka", CharacterType.HIRAGANA),
        JapaneseCharacter("き", "ki", CharacterType.HIRAGANA),
        JapaneseCharacter("く", "ku", CharacterType.HIRAGANA),
        JapaneseCharacter("け", "ke", CharacterType.HIRAGANA),
        JapaneseCharacter("こ", "ko", CharacterType.HIRAGANA),
        
        // S-series
        JapaneseCharacter("さ", "sa", CharacterType.HIRAGANA),
        JapaneseCharacter("し", "shi", CharacterType.HIRAGANA),
        JapaneseCharacter("す", "su", CharacterType.HIRAGANA),
        JapaneseCharacter("せ", "se", CharacterType.HIRAGANA),
        JapaneseCharacter("そ", "so", CharacterType.HIRAGANA),
        
        // T-series
        JapaneseCharacter("た", "ta", CharacterType.HIRAGANA),
        JapaneseCharacter("ち", "chi", CharacterType.HIRAGANA),
        JapaneseCharacter("つ", "tsu", CharacterType.HIRAGANA),
        JapaneseCharacter("て", "te", CharacterType.HIRAGANA),
        JapaneseCharacter("と", "to", CharacterType.HIRAGANA),
        
        // N-series
        JapaneseCharacter("な", "na", CharacterType.HIRAGANA),
        JapaneseCharacter("に", "ni", CharacterType.HIRAGANA),
        JapaneseCharacter("ぬ", "nu", CharacterType.HIRAGANA),
        JapaneseCharacter("ね", "ne", CharacterType.HIRAGANA),
        JapaneseCharacter("の", "no", CharacterType.HIRAGANA),
        
        // H-series
        JapaneseCharacter("は", "ha", CharacterType.HIRAGANA),
        JapaneseCharacter("ひ", "hi", CharacterType.HIRAGANA),
        JapaneseCharacter("ふ", "fu", CharacterType.HIRAGANA),
        JapaneseCharacter("へ", "he", CharacterType.HIRAGANA),
        JapaneseCharacter("ほ", "ho", CharacterType.HIRAGANA),
        
        // M-series
        JapaneseCharacter("ま", "ma", CharacterType.HIRAGANA),
        JapaneseCharacter("み", "mi", CharacterType.HIRAGANA),
        JapaneseCharacter("む", "mu", CharacterType.HIRAGANA),
        JapaneseCharacter("め", "me", CharacterType.HIRAGANA),
        JapaneseCharacter("も", "mo", CharacterType.HIRAGANA),
        
        // Y-series
        JapaneseCharacter("や", "ya", CharacterType.HIRAGANA),
        JapaneseCharacter("ゆ", "yu", CharacterType.HIRAGANA),
        JapaneseCharacter("よ", "yo", CharacterType.HIRAGANA),
        
        // R-series
        JapaneseCharacter("ら", "ra", CharacterType.HIRAGANA),
        JapaneseCharacter("り", "ri", CharacterType.HIRAGANA),
        JapaneseCharacter("る", "ru", CharacterType.HIRAGANA),
        JapaneseCharacter("れ", "re", CharacterType.HIRAGANA),
        JapaneseCharacter("ろ", "ro", CharacterType.HIRAGANA),
        
        // W-series
        JapaneseCharacter("わ", "wa", CharacterType.HIRAGANA),
        JapaneseCharacter("を", "wo", CharacterType.HIRAGANA),
        
        // N
        JapaneseCharacter("ん", "n", CharacterType.HIRAGANA),
        
        // Dakuten và Handakuten
        JapaneseCharacter("が", "ga", CharacterType.HIRAGANA),
        JapaneseCharacter("ぎ", "gi", CharacterType.HIRAGANA),
        JapaneseCharacter("ぐ", "gu", CharacterType.HIRAGANA),
        JapaneseCharacter("げ", "ge", CharacterType.HIRAGANA),
        JapaneseCharacter("ご", "go", CharacterType.HIRAGANA),
        
        JapaneseCharacter("ざ", "za", CharacterType.HIRAGANA),
        JapaneseCharacter("じ", "ji", CharacterType.HIRAGANA),
        JapaneseCharacter("ず", "zu", CharacterType.HIRAGANA),
        JapaneseCharacter("ぜ", "ze", CharacterType.HIRAGANA),
        JapaneseCharacter("ぞ", "zo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("だ", "da", CharacterType.HIRAGANA),
        JapaneseCharacter("ぢ", "ji", CharacterType.HIRAGANA),
        JapaneseCharacter("づ", "zu", CharacterType.HIRAGANA),
        JapaneseCharacter("で", "de", CharacterType.HIRAGANA),
        JapaneseCharacter("ど", "do", CharacterType.HIRAGANA),
        
        JapaneseCharacter("ば", "ba", CharacterType.HIRAGANA),
        JapaneseCharacter("び", "bi", CharacterType.HIRAGANA),
        JapaneseCharacter("ぶ", "bu", CharacterType.HIRAGANA),
        JapaneseCharacter("べ", "be", CharacterType.HIRAGANA),
        JapaneseCharacter("ぼ", "bo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("ぱ", "pa", CharacterType.HIRAGANA),
        JapaneseCharacter("ぴ", "pi", CharacterType.HIRAGANA),
        JapaneseCharacter("ぷ", "pu", CharacterType.HIRAGANA),
        JapaneseCharacter("ぺ", "pe", CharacterType.HIRAGANA),
        JapaneseCharacter("ぽ", "po", CharacterType.HIRAGANA),
        
        // Kết hợp với や、ゆ、よ
        JapaneseCharacter("きゃ", "kya", CharacterType.HIRAGANA),
        JapaneseCharacter("きゅ", "kyu", CharacterType.HIRAGANA),
        JapaneseCharacter("きょ", "kyo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("しゃ", "sha", CharacterType.HIRAGANA),
        JapaneseCharacter("しゅ", "shu", CharacterType.HIRAGANA),
        JapaneseCharacter("しょ", "sho", CharacterType.HIRAGANA),
        
        JapaneseCharacter("ちゃ", "cha", CharacterType.HIRAGANA),
        JapaneseCharacter("ちゅ", "chu", CharacterType.HIRAGANA),
        JapaneseCharacter("ちょ", "cho", CharacterType.HIRAGANA),
        
        JapaneseCharacter("にゃ", "nya", CharacterType.HIRAGANA),
        JapaneseCharacter("にゅ", "nyu", CharacterType.HIRAGANA),
        JapaneseCharacter("にょ", "nyo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("ひゃ", "hya", CharacterType.HIRAGANA),
        JapaneseCharacter("ひゅ", "hyu", CharacterType.HIRAGANA),
        JapaneseCharacter("ひょ", "hyo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("みゃ", "mya", CharacterType.HIRAGANA),
        JapaneseCharacter("みゅ", "myu", CharacterType.HIRAGANA),
        JapaneseCharacter("みょ", "myo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("りゃ", "rya", CharacterType.HIRAGANA),
        JapaneseCharacter("りゅ", "ryu", CharacterType.HIRAGANA),
        JapaneseCharacter("りょ", "ryo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("ぎゃ", "gya", CharacterType.HIRAGANA),
        JapaneseCharacter("ぎゅ", "gyu", CharacterType.HIRAGANA),
        JapaneseCharacter("ぎょ", "gyo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("じゃ", "ja", CharacterType.HIRAGANA),
        JapaneseCharacter("じゅ", "ju", CharacterType.HIRAGANA),
        JapaneseCharacter("じょ", "jo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("びゃ", "bya", CharacterType.HIRAGANA),
        JapaneseCharacter("びゅ", "byu", CharacterType.HIRAGANA),
        JapaneseCharacter("びょ", "byo", CharacterType.HIRAGANA),
        
        JapaneseCharacter("ぴゃ", "pya", CharacterType.HIRAGANA),
        JapaneseCharacter("ぴゅ", "pyu", CharacterType.HIRAGANA),
        JapaneseCharacter("ぴょ", "pyo", CharacterType.HIRAGANA)
    )

    val katakana = listOf(
        // Nguyên âm
        JapaneseCharacter("ア", "a", CharacterType.KATAKANA),
        JapaneseCharacter("イ", "i", CharacterType.KATAKANA),
        JapaneseCharacter("ウ", "u", CharacterType.KATAKANA),
        JapaneseCharacter("エ", "e", CharacterType.KATAKANA),
        JapaneseCharacter("オ", "o", CharacterType.KATAKANA),
        
        // K-series
        JapaneseCharacter("カ", "ka", CharacterType.KATAKANA),
        JapaneseCharacter("キ", "ki", CharacterType.KATAKANA),
        JapaneseCharacter("ク", "ku", CharacterType.KATAKANA),
        JapaneseCharacter("ケ", "ke", CharacterType.KATAKANA),
        JapaneseCharacter("コ", "ko", CharacterType.KATAKANA),
        
        // S-series
        JapaneseCharacter("サ", "sa", CharacterType.KATAKANA),
        JapaneseCharacter("シ", "shi", CharacterType.KATAKANA),
        JapaneseCharacter("ス", "su", CharacterType.KATAKANA),
        JapaneseCharacter("セ", "se", CharacterType.KATAKANA),
        JapaneseCharacter("ソ", "so", CharacterType.KATAKANA),
        
        // T-series
        JapaneseCharacter("タ", "ta", CharacterType.KATAKANA),
        JapaneseCharacter("チ", "chi", CharacterType.KATAKANA),
        JapaneseCharacter("ツ", "tsu", CharacterType.KATAKANA),
        JapaneseCharacter("テ", "te", CharacterType.KATAKANA),
        JapaneseCharacter("ト", "to", CharacterType.KATAKANA),
        
        // N-series
        JapaneseCharacter("ナ", "na", CharacterType.KATAKANA),
        JapaneseCharacter("ニ", "ni", CharacterType.KATAKANA),
        JapaneseCharacter("ヌ", "nu", CharacterType.KATAKANA),
        JapaneseCharacter("ネ", "ne", CharacterType.KATAKANA),
        JapaneseCharacter("ノ", "no", CharacterType.KATAKANA),
        
        // H-series
        JapaneseCharacter("ハ", "ha", CharacterType.KATAKANA),
        JapaneseCharacter("ヒ", "hi", CharacterType.KATAKANA),
        JapaneseCharacter("フ", "fu", CharacterType.KATAKANA),
        JapaneseCharacter("ヘ", "he", CharacterType.KATAKANA),
        JapaneseCharacter("ホ", "ho", CharacterType.KATAKANA),
        
        // M-series
        JapaneseCharacter("マ", "ma", CharacterType.KATAKANA),
        JapaneseCharacter("ミ", "mi", CharacterType.KATAKANA),
        JapaneseCharacter("ム", "mu", CharacterType.KATAKANA),
        JapaneseCharacter("メ", "me", CharacterType.KATAKANA),
        JapaneseCharacter("モ", "mo", CharacterType.KATAKANA),
        
        // Y-series
        JapaneseCharacter("ヤ", "ya", CharacterType.KATAKANA),
        JapaneseCharacter("ユ", "yu", CharacterType.KATAKANA),
        JapaneseCharacter("ヨ", "yo", CharacterType.KATAKANA),
        
        // R-series
        JapaneseCharacter("ラ", "ra", CharacterType.KATAKANA),
        JapaneseCharacter("リ", "ri", CharacterType.KATAKANA),
        JapaneseCharacter("ル", "ru", CharacterType.KATAKANA),
        JapaneseCharacter("レ", "re", CharacterType.KATAKANA),
        JapaneseCharacter("ロ", "ro", CharacterType.KATAKANA),
        
        // W-series
        JapaneseCharacter("ワ", "wa", CharacterType.KATAKANA),
        JapaneseCharacter("ヲ", "wo", CharacterType.KATAKANA),
        
        // N
        JapaneseCharacter("ン", "n", CharacterType.KATAKANA),
        
        // Dakuten và Handakuten
        JapaneseCharacter("ガ", "ga", CharacterType.KATAKANA),
        JapaneseCharacter("ギ", "gi", CharacterType.KATAKANA),
        JapaneseCharacter("グ", "gu", CharacterType.KATAKANA),
        JapaneseCharacter("ゲ", "ge", CharacterType.KATAKANA),
        JapaneseCharacter("ゴ", "go", CharacterType.KATAKANA),
        
        JapaneseCharacter("ザ", "za", CharacterType.KATAKANA),
        JapaneseCharacter("ジ", "ji", CharacterType.KATAKANA),
        JapaneseCharacter("ズ", "zu", CharacterType.KATAKANA),
        JapaneseCharacter("ゼ", "ze", CharacterType.KATAKANA),
        JapaneseCharacter("ゾ", "zo", CharacterType.KATAKANA),
        
        JapaneseCharacter("ダ", "da", CharacterType.KATAKANA),
        JapaneseCharacter("ヂ", "ji", CharacterType.KATAKANA),
        JapaneseCharacter("ヅ", "zu", CharacterType.KATAKANA),
        JapaneseCharacter("デ", "de", CharacterType.KATAKANA),
        JapaneseCharacter("ド", "do", CharacterType.KATAKANA),
        
        JapaneseCharacter("バ", "ba", CharacterType.KATAKANA),
        JapaneseCharacter("ビ", "bi", CharacterType.KATAKANA),
        JapaneseCharacter("ブ", "bu", CharacterType.KATAKANA),
        JapaneseCharacter("ベ", "be", CharacterType.KATAKANA),
        JapaneseCharacter("ボ", "bo", CharacterType.KATAKANA),
        
        JapaneseCharacter("パ", "pa", CharacterType.KATAKANA),
        JapaneseCharacter("ピ", "pi", CharacterType.KATAKANA),
        JapaneseCharacter("プ", "pu", CharacterType.KATAKANA),
        JapaneseCharacter("ペ", "pe", CharacterType.KATAKANA),
        JapaneseCharacter("ポ", "po", CharacterType.KATAKANA),
        
        // Kết hợp với ヤ、ユ、ヨ
        JapaneseCharacter("キャ", "kya", CharacterType.KATAKANA),
        JapaneseCharacter("キュ", "kyu", CharacterType.KATAKANA),
        JapaneseCharacter("キョ", "kyo", CharacterType.KATAKANA),
        
        JapaneseCharacter("シャ", "sha", CharacterType.KATAKANA),
        JapaneseCharacter("シュ", "shu", CharacterType.KATAKANA),
        JapaneseCharacter("ショ", "sho", CharacterType.KATAKANA),
        
        JapaneseCharacter("チャ", "cha", CharacterType.KATAKANA),
        JapaneseCharacter("チュ", "chu", CharacterType.KATAKANA),
        JapaneseCharacter("チョ", "cho", CharacterType.KATAKANA),
        
        JapaneseCharacter("ニャ", "nya", CharacterType.KATAKANA),
        JapaneseCharacter("ニュ", "nyu", CharacterType.KATAKANA),
        JapaneseCharacter("ニョ", "nyo", CharacterType.KATAKANA),
        
        JapaneseCharacter("ヒャ", "hya", CharacterType.KATAKANA),
        JapaneseCharacter("ヒュ", "hyu", CharacterType.KATAKANA),
        JapaneseCharacter("ヒョ", "hyo", CharacterType.KATAKANA),
        
        JapaneseCharacter("ミャ", "mya", CharacterType.KATAKANA),
        JapaneseCharacter("ミュ", "myu", CharacterType.KATAKANA),
        JapaneseCharacter("ミョ", "myo", CharacterType.KATAKANA),
        
        JapaneseCharacter("リャ", "rya", CharacterType.KATAKANA),
        JapaneseCharacter("リュ", "ryu", CharacterType.KATAKANA),
        JapaneseCharacter("リョ", "ryo", CharacterType.KATAKANA),
        
        JapaneseCharacter("ギャ", "gya", CharacterType.KATAKANA),
        JapaneseCharacter("ギュ", "gyu", CharacterType.KATAKANA),
        JapaneseCharacter("ギョ", "gyo", CharacterType.KATAKANA),
        
        JapaneseCharacter("ジャ", "ja", CharacterType.KATAKANA),
        JapaneseCharacter("ジュ", "ju", CharacterType.KATAKANA),
        JapaneseCharacter("ジョ", "jo", CharacterType.KATAKANA),
        
        JapaneseCharacter("ビャ", "bya", CharacterType.KATAKANA),
        JapaneseCharacter("ビュ", "byu", CharacterType.KATAKANA),
        JapaneseCharacter("ビョ", "byo", CharacterType.KATAKANA),
        
        JapaneseCharacter("ピャ", "pya", CharacterType.KATAKANA),
        JapaneseCharacter("ピュ", "pyu", CharacterType.KATAKANA),
        JapaneseCharacter("ピョ", "pyo", CharacterType.KATAKANA)
    )
}