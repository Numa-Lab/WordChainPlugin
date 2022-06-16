package com.github.bun133.wordchain

// TODO 小文字の扱いを考える

/**
 * 文字列から最後の文字を取得する
 */
fun getLastHiraganaFromString(str: String): Char? {
    val hiragana = str.mapNotNull { forceLargeHiragana(it) }
    return if (hiragana.isEmpty()) {
        null
    } else {
        hiragana.last()
    }
}

/**
 * 文字列から最初の文字を取得する
 */
fun getFirstHiraganaFromString(str: String): Char? {
    val hiragana = str.mapNotNull { forceLargeHiragana(it) }
    return if (hiragana.isEmpty()) {
        null
    } else {
        hiragana.first()
    }
}

// 強制的に大文字ひらがなに変換する
fun forceLargeHiragana(char: Char): Char? {
    return if (smallHiraganaToLargeHiragana.containsKey(char)) {
        smallHiraganaToLargeHiragana[char]
    } else if (dakutenToKana.contains(char)) {
        dakutenToKana[char]
    } else if (hiragana.contains(char)) {
        char
    } else {
        null
    }
}

// 小文字 to 大文字
val smallHiraganaToLargeHiragana = mapOf<Char, Char>(
    'ぁ' to 'あ',
    'ぃ' to 'い',
    'ぅ' to 'う',
    'ぇ' to 'え',
    'ぉ' to 'お',
    'ゃ' to 'や',
    'ゅ' to 'ゆ',
    'ょ' to 'よ',
    'っ' to 'つ',
    'ゎ' to 'わ'
)

// 濁点 + 半濁点 to 清音
@Suppress("SpellCheckingInspection")
val dakutenToKana = mapOf<Char, Char>(
    'が' to 'か',
    'ぎ' to 'き',
    'ぐ' to 'く',
    'げ' to 'け',
    'ご' to 'こ',
    'ざ' to 'さ',
    'じ' to 'し',
    'ず' to 'す',
    'ぜ' to 'せ',
    'ぞ' to 'そ',
    'だ' to 'た',
    'ぢ' to 'ち',
    'づ' to 'つ',
    'で' to 'て',
    'ど' to 'と',
    'ば' to 'は',
    'び' to 'ひ',
    'ぶ' to 'ふ',
    'べ' to 'へ',
    'ぼ' to 'ほ',
    'ば' to 'は',
    'ぴ' to 'ひ',
    'ぶ' to 'ふ',
    'べ' to 'へ',
    'ぼ' to 'ほ',
    'ぱ' to 'は',
    'ぴ' to 'ひ',
    'ぷ' to 'ふ',
    'ぺ' to 'へ',
    'ぽ' to 'ほ',
)

// ひらがな全部
val hiragana = listOf<Char>(
    'あ',
    'い',
    'う',
    'え',
    'お',
    'か',
    'き',
    'く',
    'け',
    'こ',
    'さ',
    'し',
    'す',
    'せ',
    'そ',
    'た',
    'ち',
    'つ',
    'て',
    'と',
    'な',
    'に',
    'ぬ',
    'ね',
    'の',
    'は',
    'ひ',
    'ふ',
    'へ',
    'ほ',
    'ま',
    'み',
    'む',
    'め',
    'も',
    'や',
    'ゆ',
    'よ',
    'ら',
    'り',
    'る',
    'れ',
    'ろ',
    'わ',
    'を',
    'ん'
)