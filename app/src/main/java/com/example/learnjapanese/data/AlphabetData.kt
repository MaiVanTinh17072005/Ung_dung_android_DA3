data class JapaneseCharacter(
    val character: String,
    val romaji: String,
    val type: CharacterType,
    val strokeOrderUrl: String = ""
)

enum class CharacterType {
    HIRAGANA, KATAKANA
}

object AlphabetData {
    private const val BASE_URL = "https://www3.nhk.or.jp/nhkworld/lesson/assets/images/letters/detail"
    
    val hiragana = listOf(
        // Nguyên âm
        JapaneseCharacter("あ", "a", CharacterType.HIRAGANA, "$BASE_URL/hira/a.png"),
        JapaneseCharacter("い", "i", CharacterType.HIRAGANA, "$BASE_URL/hira/i.png"),
        JapaneseCharacter("う", "u", CharacterType.HIRAGANA, "$BASE_URL/hira/u.png"),
        JapaneseCharacter("え", "e", CharacterType.HIRAGANA, "$BASE_URL/hira/e.png"),
        JapaneseCharacter("お", "o", CharacterType.HIRAGANA, "$BASE_URL/hira/o.png"),
        
        // K-series
        JapaneseCharacter("か", "ka", CharacterType.HIRAGANA, "$BASE_URL/hira/ka.png"),
        JapaneseCharacter("き", "ki", CharacterType.HIRAGANA, "$BASE_URL/hira/ki.png"),
        JapaneseCharacter("く", "ku", CharacterType.HIRAGANA, "$BASE_URL/hira/ku.png"),
        JapaneseCharacter("け", "ke", CharacterType.HIRAGANA, "$BASE_URL/hira/ke.png"),
        JapaneseCharacter("こ", "ko", CharacterType.HIRAGANA, "$BASE_URL/hira/ko.png"),
        
        // S-series
        JapaneseCharacter("さ", "sa", CharacterType.HIRAGANA, "$BASE_URL/hira/sa.png"),
        JapaneseCharacter("し", "shi", CharacterType.HIRAGANA, "$BASE_URL/hira/shi.png"),
        JapaneseCharacter("す", "su", CharacterType.HIRAGANA, "$BASE_URL/hira/su.png"),
        JapaneseCharacter("せ", "se", CharacterType.HIRAGANA, "$BASE_URL/hira/se.png"),
        JapaneseCharacter("そ", "so", CharacterType.HIRAGANA, "$BASE_URL/hira/so.png"),
        
        // T-series
        JapaneseCharacter("た", "ta", CharacterType.HIRAGANA, "$BASE_URL/hira/ta.png"),
        JapaneseCharacter("ち", "chi", CharacterType.HIRAGANA, "$BASE_URL/hira/chi.png"),
        JapaneseCharacter("つ", "tsu", CharacterType.HIRAGANA, "$BASE_URL/hira/tsu.png"),
        JapaneseCharacter("て", "te", CharacterType.HIRAGANA, "$BASE_URL/hira/te.png"),
        JapaneseCharacter("と", "to", CharacterType.HIRAGANA, "$BASE_URL/hira/to.png"),
        
        // N-series
        JapaneseCharacter("な", "na", CharacterType.HIRAGANA, "$BASE_URL/hira/na.png"),
        JapaneseCharacter("に", "ni", CharacterType.HIRAGANA, "$BASE_URL/hira/ni.png"),
        JapaneseCharacter("ぬ", "nu", CharacterType.HIRAGANA, "$BASE_URL/hira/nu.png"),
        JapaneseCharacter("ね", "ne", CharacterType.HIRAGANA, "$BASE_URL/hira/ne.png"),
        JapaneseCharacter("の", "no", CharacterType.HIRAGANA, "$BASE_URL/hira/no.png"),
        
        // H-series
        JapaneseCharacter("は", "ha", CharacterType.HIRAGANA, "$BASE_URL/hira/ha.png"),
        JapaneseCharacter("ひ", "hi", CharacterType.HIRAGANA, "$BASE_URL/hira/hi.png"),
        JapaneseCharacter("ふ", "fu", CharacterType.HIRAGANA, "$BASE_URL/hira/fu.png"),
        JapaneseCharacter("へ", "he", CharacterType.HIRAGANA, "$BASE_URL/hira/he.png"),
        JapaneseCharacter("ほ", "ho", CharacterType.HIRAGANA, "$BASE_URL/hira/ho.png"),
        
        // M-series
        JapaneseCharacter("ま", "ma", CharacterType.HIRAGANA, "$BASE_URL/hira/ma.png"),
        JapaneseCharacter("み", "mi", CharacterType.HIRAGANA, "$BASE_URL/hira/mi.png"),
        JapaneseCharacter("む", "mu", CharacterType.HIRAGANA, "$BASE_URL/hira/mu.png"),
        JapaneseCharacter("め", "me", CharacterType.HIRAGANA, "$BASE_URL/hira/me.png"),
        JapaneseCharacter("も", "mo", CharacterType.HIRAGANA, "$BASE_URL/hira/mo.png"),
        
        // Y-series
        JapaneseCharacter("や", "ya", CharacterType.HIRAGANA, "$BASE_URL/hira/ya.png"),
        JapaneseCharacter("ゆ", "yu", CharacterType.HIRAGANA, "$BASE_URL/hira/yu.png"),
        JapaneseCharacter("よ", "yo", CharacterType.HIRAGANA, "$BASE_URL/hira/yo.png"),
        
        // R-series
        JapaneseCharacter("ら", "ra", CharacterType.HIRAGANA, "$BASE_URL/hira/ra.png"),
        JapaneseCharacter("り", "ri", CharacterType.HIRAGANA, "$BASE_URL/hira/ri.png"),
        JapaneseCharacter("る", "ru", CharacterType.HIRAGANA, "$BASE_URL/hira/ru.png"),
        JapaneseCharacter("れ", "re", CharacterType.HIRAGANA, "$BASE_URL/hira/re.png"),
        JapaneseCharacter("ろ", "ro", CharacterType.HIRAGANA, "$BASE_URL/hira/ro.png"),
        
        // W-series
        JapaneseCharacter("わ", "wa", CharacterType.HIRAGANA, "$BASE_URL/hira/wa.png"),
        JapaneseCharacter("を", "wo", CharacterType.HIRAGANA, "$BASE_URL/hira/wo.png"),
        
        // N
        JapaneseCharacter("ん", "n", CharacterType.HIRAGANA, "$BASE_URL/hira/n.png"),
        
        // Dakuten (voiced sounds)
        JapaneseCharacter("が", "ga", CharacterType.HIRAGANA, "$BASE_URL/hira/ga.png"),
        JapaneseCharacter("ぎ", "gi", CharacterType.HIRAGANA, "$BASE_URL/hira/gi.png"),
        JapaneseCharacter("ぐ", "gu", CharacterType.HIRAGANA, "$BASE_URL/hira/gu.png"),
        JapaneseCharacter("げ", "ge", CharacterType.HIRAGANA, "$BASE_URL/hira/ge.png"),
        JapaneseCharacter("ご", "go", CharacterType.HIRAGANA, "$BASE_URL/hira/go.png"),
        
        JapaneseCharacter("ざ", "za", CharacterType.HIRAGANA, "$BASE_URL/hira/za.png"),
        JapaneseCharacter("じ", "ji", CharacterType.HIRAGANA, "$BASE_URL/hira/ji.png"),
        JapaneseCharacter("ず", "zu", CharacterType.HIRAGANA, "$BASE_URL/hira/zu.png"),
        JapaneseCharacter("ぜ", "ze", CharacterType.HIRAGANA, "$BASE_URL/hira/ze.png"),
        JapaneseCharacter("ぞ", "zo", CharacterType.HIRAGANA, "$BASE_URL/hira/zo.png"),
        
        JapaneseCharacter("だ", "da", CharacterType.HIRAGANA, "$BASE_URL/hira/da.png"),
        JapaneseCharacter("ぢ", "ji", CharacterType.HIRAGANA, "$BASE_URL/hira/ji2.png"),
        JapaneseCharacter("づ", "zu", CharacterType.HIRAGANA, "$BASE_URL/hira/zu2.png"),
        JapaneseCharacter("で", "de", CharacterType.HIRAGANA, "$BASE_URL/hira/de.png"),
        JapaneseCharacter("ど", "do", CharacterType.HIRAGANA, "$BASE_URL/hira/do.png"),
        
        // Handakuten (semi-voiced sounds)
        JapaneseCharacter("ば", "ba", CharacterType.HIRAGANA, "$BASE_URL/hira/ba.png"),
        JapaneseCharacter("び", "bi", CharacterType.HIRAGANA, "$BASE_URL/hira/bi.png"),
        JapaneseCharacter("ぶ", "bu", CharacterType.HIRAGANA, "$BASE_URL/hira/bu.png"),
        JapaneseCharacter("べ", "be", CharacterType.HIRAGANA, "$BASE_URL/hira/be.png"),
        JapaneseCharacter("ぼ", "bo", CharacterType.HIRAGANA, "$BASE_URL/hira/bo.png"),
        
        JapaneseCharacter("ぱ", "pa", CharacterType.HIRAGANA, "$BASE_URL/hira/pa.png"),
        JapaneseCharacter("ぴ", "pi", CharacterType.HIRAGANA, "$BASE_URL/hira/pi.png"),
        JapaneseCharacter("ぷ", "pu", CharacterType.HIRAGANA, "$BASE_URL/hira/pu.png"),
        JapaneseCharacter("ぺ", "pe", CharacterType.HIRAGANA, "$BASE_URL/hira/pe.png"),
        JapaneseCharacter("ぽ", "po", CharacterType.HIRAGANA, "$BASE_URL/hira/po.png"),
        
        // Compound sounds (youon)
        JapaneseCharacter("きゃ", "kya", CharacterType.HIRAGANA, "$BASE_URL/hira/kya.png"),
        JapaneseCharacter("きゅ", "kyu", CharacterType.HIRAGANA, "$BASE_URL/hira/kyu.png"),
        JapaneseCharacter("きょ", "kyo", CharacterType.HIRAGANA, "$BASE_URL/hira/kyo.png"),
        
        JapaneseCharacter("しゃ", "sha", CharacterType.HIRAGANA, "$BASE_URL/hira/sha.png"),
        JapaneseCharacter("しゅ", "shu", CharacterType.HIRAGANA, "$BASE_URL/hira/shu.png"),
        JapaneseCharacter("しょ", "sho", CharacterType.HIRAGANA, "$BASE_URL/hira/sho.png"),
        
        JapaneseCharacter("ちゃ", "cha", CharacterType.HIRAGANA, "$BASE_URL/hira/cha.png"),
        JapaneseCharacter("ちゅ", "chu", CharacterType.HIRAGANA, "$BASE_URL/hira/chu.png"),
        JapaneseCharacter("ちょ", "cho", CharacterType.HIRAGANA, "$BASE_URL/hira/cho.png"),
        
        JapaneseCharacter("にゃ", "nya", CharacterType.HIRAGANA, "$BASE_URL/hira/nya.png"),
        JapaneseCharacter("にゅ", "nyu", CharacterType.HIRAGANA, "$BASE_URL/hira/nyu.png"),
        JapaneseCharacter("にょ", "nyo", CharacterType.HIRAGANA, "$BASE_URL/hira/nyo.png"),
        
        JapaneseCharacter("ひゃ", "hya", CharacterType.HIRAGANA, "$BASE_URL/hira/hya.png"),
        JapaneseCharacter("ひゅ", "hyu", CharacterType.HIRAGANA, "$BASE_URL/hira/hyu.png"),
        JapaneseCharacter("ひょ", "hyo", CharacterType.HIRAGANA, "$BASE_URL/hira/hyo.png"),
        
        JapaneseCharacter("みゃ", "mya", CharacterType.HIRAGANA, "$BASE_URL/hira/mya.png"),
        JapaneseCharacter("みゅ", "myu", CharacterType.HIRAGANA, "$BASE_URL/hira/myu.png"),
        JapaneseCharacter("みょ", "myo", CharacterType.HIRAGANA, "$BASE_URL/hira/myo.png"),
        
        JapaneseCharacter("りゃ", "rya", CharacterType.HIRAGANA, "$BASE_URL/hira/rya.png"),
        JapaneseCharacter("りゅ", "ryu", CharacterType.HIRAGANA, "$BASE_URL/hira/ryu.png"),
        JapaneseCharacter("りょ", "ryo", CharacterType.HIRAGANA, "$BASE_URL/hira/ryo.png"),
        
        JapaneseCharacter("ぎゃ", "gya", CharacterType.HIRAGANA, "$BASE_URL/hira/gya.png"),
        JapaneseCharacter("ぎゅ", "gyu", CharacterType.HIRAGANA, "$BASE_URL/hira/gyu.png"),
        JapaneseCharacter("ぎょ", "gyo", CharacterType.HIRAGANA, "$BASE_URL/hira/gyo.png"),
        
        JapaneseCharacter("じゃ", "ja", CharacterType.HIRAGANA, "$BASE_URL/hira/ja.png"),
        JapaneseCharacter("じゅ", "ju", CharacterType.HIRAGANA, "$BASE_URL/hira/ju.png"),
        JapaneseCharacter("じょ", "jo", CharacterType.HIRAGANA, "$BASE_URL/hira/jo.png"),
        
        JapaneseCharacter("びゃ", "bya", CharacterType.HIRAGANA, "$BASE_URL/hira/bya.png"),
        JapaneseCharacter("びゅ", "byu", CharacterType.HIRAGANA, "$BASE_URL/hira/byu.png"),
        JapaneseCharacter("びょ", "byo", CharacterType.HIRAGANA, "$BASE_URL/hira/byo.png"),
        
        JapaneseCharacter("ぴゃ", "pya", CharacterType.HIRAGANA, "$BASE_URL/hira/pya.png"),
        JapaneseCharacter("ぴゅ", "pyu", CharacterType.HIRAGANA, "$BASE_URL/hira/pyu.png"),
        JapaneseCharacter("ぴょ", "pyo", CharacterType.HIRAGANA, "$BASE_URL/hira/pyo.png")
    )

    val katakana = listOf(
        // Basic sounds remain the same, just update URL
        JapaneseCharacter("ア", "a", CharacterType.KATAKANA, "$BASE_URL/kana/a.png"),
        JapaneseCharacter("イ", "i", CharacterType.KATAKANA, "$BASE_URL/kana/i.png"),
        JapaneseCharacter("ウ", "u", CharacterType.KATAKANA, "$BASE_URL/kana/u.png"),
        JapaneseCharacter("エ", "e", CharacterType.KATAKANA, "$BASE_URL/kana/e.png"),
        JapaneseCharacter("オ", "o", CharacterType.KATAKANA, "$BASE_URL/kana/o.png"),

        // K-series
        JapaneseCharacter("カ", "ka", CharacterType.KATAKANA, "$BASE_URL/kana/ka.png"),
        JapaneseCharacter("キ", "ki", CharacterType.KATAKANA, "$BASE_URL/kana/ki.png"),
        JapaneseCharacter("ク", "ku", CharacterType.KATAKANA, "$BASE_URL/kana/ku.png"),
        JapaneseCharacter("ケ", "ke", CharacterType.KATAKANA, "$BASE_URL/kana/ke.png"),
        JapaneseCharacter("コ", "ko", CharacterType.KATAKANA, "$BASE_URL/kana/ko.png"),

        // S-series
        JapaneseCharacter("サ", "sa", CharacterType.KATAKANA, "$BASE_URL/kana/sa.png"),
        JapaneseCharacter("シ", "shi", CharacterType.KATAKANA, "$BASE_URL/kana/shi.png"),
        JapaneseCharacter("ス", "su", CharacterType.KATAKANA, "$BASE_URL/kana/su.png"),
        JapaneseCharacter("セ", "se", CharacterType.KATAKANA, "$BASE_URL/kana/se.png"),
        JapaneseCharacter("ソ", "so", CharacterType.KATAKANA, "$BASE_URL/kana/so.png"),

        // T-series
        JapaneseCharacter("タ", "ta", CharacterType.KATAKANA, "$BASE_URL/kana/ta.png"),
        JapaneseCharacter("チ", "chi", CharacterType.KATAKANA, "$BASE_URL/kana/chi.png"),
        JapaneseCharacter("ツ", "tsu", CharacterType.KATAKANA, "$BASE_URL/kana/tsu.png"),
        JapaneseCharacter("テ", "te", CharacterType.KATAKANA, "$BASE_URL/kana/te.png"),
        JapaneseCharacter("ト", "to", CharacterType.KATAKANA, "$BASE_URL/kana/to.png"),

        // N-series
        JapaneseCharacter("ナ", "na", CharacterType.KATAKANA, "$BASE_URL/kana/na.png"),
        JapaneseCharacter("ニ", "ni", CharacterType.KATAKANA, "$BASE_URL/kana/ni.png"),
        JapaneseCharacter("ヌ", "nu", CharacterType.KATAKANA, "$BASE_URL/kana/nu.png"),
        JapaneseCharacter("ネ", "ne", CharacterType.KATAKANA, "$BASE_URL/kana/ne.png"),
        JapaneseCharacter("ノ", "no", CharacterType.KATAKANA, "$BASE_URL/kana/no.png"),

        // H-series
        JapaneseCharacter("ハ", "ha", CharacterType.KATAKANA, "$BASE_URL/kana/ha.png"),
        JapaneseCharacter("ヒ", "hi", CharacterType.KATAKANA, "$BASE_URL/kana/hi.png"),
        JapaneseCharacter("フ", "fu", CharacterType.KATAKANA, "$BASE_URL/kana/fu.png"),
        JapaneseCharacter("ヘ", "he", CharacterType.KATAKANA, "$BASE_URL/kana/he.png"),
        JapaneseCharacter("ホ", "ho", CharacterType.KATAKANA, "$BASE_URL/kana/ho.png"),

        // M-series
        JapaneseCharacter("マ", "ma", CharacterType.KATAKANA, "$BASE_URL/kana/ma.png"),
        JapaneseCharacter("ミ", "mi", CharacterType.KATAKANA, "$BASE_URL/kana/mi.png"),
        JapaneseCharacter("ム", "mu", CharacterType.KATAKANA, "$BASE_URL/kana/mu.png"),
        JapaneseCharacter("メ", "me", CharacterType.KATAKANA, "$BASE_URL/kana/me.png"),
        JapaneseCharacter("モ", "mo", CharacterType.KATAKANA, "$BASE_URL/kana/mo.png"),

        // Y-series
        JapaneseCharacter("ヤ", "ya", CharacterType.KATAKANA, "$BASE_URL/kana/ya.png"),
        JapaneseCharacter("ユ", "yu", CharacterType.KATAKANA, "$BASE_URL/kana/yu.png"),
        JapaneseCharacter("ヨ", "yo", CharacterType.KATAKANA, "$BASE_URL/kana/yo.png"),

        // R-series
        JapaneseCharacter("ラ", "ra", CharacterType.KATAKANA, "$BASE_URL/kana/ra.png"),
        JapaneseCharacter("リ", "ri", CharacterType.KATAKANA, "$BASE_URL/kana/ri.png"),
        JapaneseCharacter("ル", "ru", CharacterType.KATAKANA, "$BASE_URL/kana/ru.png"),
        JapaneseCharacter("レ", "re", CharacterType.KATAKANA, "$BASE_URL/kana/re.png"),
        JapaneseCharacter("ロ", "ro", CharacterType.KATAKANA, "$BASE_URL/kana/ro.png"),

        // W-series
        JapaneseCharacter("ワ", "wa", CharacterType.KATAKANA, "$BASE_URL/kana/wa.png"),
        JapaneseCharacter("ヲ", "wo", CharacterType.KATAKANA, "$BASE_URL/kana/wo.png"),

        // N
        JapaneseCharacter("ン", "n", CharacterType.KATAKANA, "$BASE_URL/kana/n.png"),

        // Dakuten (voiced sounds)
        JapaneseCharacter("ガ", "ga", CharacterType.KATAKANA, "$BASE_URL/kana/ga.png"),
        JapaneseCharacter("ギ", "gi", CharacterType.KATAKANA, "$BASE_URL/kana/gi.png"),
        JapaneseCharacter("グ", "gu", CharacterType.KATAKANA, "$BASE_URL/kana/gu.png"),
        JapaneseCharacter("ゲ", "ge", CharacterType.KATAKANA, "$BASE_URL/kana/ge.png"),
        JapaneseCharacter("ゴ", "go", CharacterType.KATAKANA, "$BASE_URL/kana/go.png"),

        JapaneseCharacter("ザ", "za", CharacterType.KATAKANA, "$BASE_URL/kana/za.png"),
        JapaneseCharacter("ジ", "ji", CharacterType.KATAKANA, "$BASE_URL/kana/ji.png"),
        JapaneseCharacter("ズ", "zu", CharacterType.KATAKANA, "$BASE_URL/kana/zu.png"),
        JapaneseCharacter("ゼ", "ze", CharacterType.KATAKANA, "$BASE_URL/kana/ze.png"),
        JapaneseCharacter("ゾ", "zo", CharacterType.KATAKANA, "$BASE_URL/kana/zo.png"),

        JapaneseCharacter("ダ", "da", CharacterType.KATAKANA, "$BASE_URL/kana/da.png"),
        JapaneseCharacter("ヂ", "ji", CharacterType.KATAKANA, "$BASE_URL/kana/ji2.png"),
        JapaneseCharacter("ヅ", "zu", CharacterType.KATAKANA, "$BASE_URL/kana/zu2.png"),
        JapaneseCharacter("デ", "de", CharacterType.KATAKANA, "$BASE_URL/kana/de.png"),
        JapaneseCharacter("ド", "do", CharacterType.KATAKANA, "$BASE_URL/kana/do.png"),

        // Handakuten (semi-voiced sounds)
        JapaneseCharacter("バ", "ba", CharacterType.KATAKANA, "$BASE_URL/kana/ba.png"),
        JapaneseCharacter("ビ", "bi", CharacterType.KATAKANA, "$BASE_URL/kana/bi.png"),
        JapaneseCharacter("ブ", "bu", CharacterType.KATAKANA, "$BASE_URL/kana/bu.png"),
        JapaneseCharacter("ベ", "be", CharacterType.KATAKANA, "$BASE_URL/kana/be.png"),
        JapaneseCharacter("ボ", "bo", CharacterType.KATAKANA, "$BASE_URL/kana/bo.png"),

        JapaneseCharacter("パ", "pa", CharacterType.KATAKANA, "$BASE_URL/kana/pa.png"),
        JapaneseCharacter("ピ", "pi", CharacterType.KATAKANA, "$BASE_URL/kana/pi.png"),
        JapaneseCharacter("プ", "pu", CharacterType.KATAKANA, "$BASE_URL/kana/pu.png"),
        JapaneseCharacter("ペ", "pe", CharacterType.KATAKANA, "$BASE_URL/kana/pe.png"),
        JapaneseCharacter("ポ", "po", CharacterType.KATAKANA, "$BASE_URL/kana/po.png"),


        // Compound sounds (youon)
        JapaneseCharacter("キャ", "kya", CharacterType.KATAKANA, "$BASE_URL/kana/kya.png"),
        JapaneseCharacter("キュ", "kyu", CharacterType.KATAKANA, "$BASE_URL/kana/kyu.png"),
        JapaneseCharacter("キョ", "kyo", CharacterType.KATAKANA, "$BASE_URL/kana/kyo.png"),

        JapaneseCharacter("シャ", "sha", CharacterType.KATAKANA, "$BASE_URL/kana/sha.png"),
        JapaneseCharacter("シュ", "shu", CharacterType.KATAKANA, "$BASE_URL/kana/shu.png"),
        JapaneseCharacter("ショ", "sho", CharacterType.KATAKANA, "$BASE_URL/kana/sho.png"),

        JapaneseCharacter("チャ", "cha", CharacterType.KATAKANA, "$BASE_URL/kana/cha.png"),
        JapaneseCharacter("チュ", "chu", CharacterType.KATAKANA, "$BASE_URL/kana/chu.png"),
        JapaneseCharacter("チョ", "cho", CharacterType.KATAKANA, "$BASE_URL/kana/cho.png"),

        JapaneseCharacter("ニャ", "nya", CharacterType.KATAKANA, "$BASE_URL/kana/nya.png"),
        JapaneseCharacter("ニュ", "nyu", CharacterType.KATAKANA, "$BASE_URL/kana/nyu.png"),
        JapaneseCharacter("ニョ", "nyo", CharacterType.KATAKANA, "$BASE_URL/kana/nyo.png"),

        JapaneseCharacter("ヒャ", "hya", CharacterType.KATAKANA, "$BASE_URL/kana/hya.png"),
        JapaneseCharacter("ヒュ", "hyu", CharacterType.KATAKANA, "$BASE_URL/kana/hyu.png"),
        JapaneseCharacter("ヒョ", "hyo", CharacterType.KATAKANA, "$BASE_URL/kana/hyo.png"),

        JapaneseCharacter("ミャ", "mya", CharacterType.KATAKANA, "$BASE_URL/kana/mya.png"),
        JapaneseCharacter("ミュ", "myu", CharacterType.KATAKANA, "$BASE_URL/kana/myu.png"),
        JapaneseCharacter("ミョ", "myo", CharacterType.KATAKANA, "$BASE_URL/kana/myo.png"),

        JapaneseCharacter("リャ", "rya", CharacterType.KATAKANA, "$BASE_URL/kana/rya.png"),
        JapaneseCharacter("リュ", "ryu", CharacterType.KATAKANA, "$BASE_URL/kana/ryu.png"),
        JapaneseCharacter("リョ", "ryo", CharacterType.KATAKANA, "$BASE_URL/kana/ryo.png"),

        JapaneseCharacter("ギャ", "gya", CharacterType.KATAKANA, "$BASE_URL/kana/gya.png"),
        JapaneseCharacter("ギュ", "gyu", CharacterType.KATAKANA, "$BASE_URL/kana/gyu.png"),
        JapaneseCharacter("ギョ", "gyo", CharacterType.KATAKANA, "$BASE_URL/kana/gyo.png"),

        JapaneseCharacter("ジャ", "ja", CharacterType.KATAKANA, "$BASE_URL/kana/ja.png"),
        JapaneseCharacter("ジュ", "ju", CharacterType.KATAKANA, "$BASE_URL/kana/ju.png"),
        JapaneseCharacter("ジョ", "jo", CharacterType.KATAKANA, "$BASE_URL/kana/jo.png"),

        JapaneseCharacter("ビャ", "bya", CharacterType.KATAKANA, "$BASE_URL/kana/bya.png"),
        JapaneseCharacter("ビュ", "byu", CharacterType.KATAKANA, "$BASE_URL/kana/byu.png"),
        JapaneseCharacter("ビョ", "byo", CharacterType.KATAKANA, "$BASE_URL/kana/byo.png"),

        JapaneseCharacter("ピャ", "pya", CharacterType.KATAKANA, "$BASE_URL/kana/pya.png"),
        JapaneseCharacter("ピュ", "pyu", CharacterType.KATAKANA, "$BASE_URL/kana/pyu.png"),
        JapaneseCharacter("ピョ", "pyo", CharacterType.KATAKANA, "$BASE_URL/kana/pyo.png")
    )
}