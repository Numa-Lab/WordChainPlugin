package com.github.bun133.wordchain

// TODO 小文字の扱いを考える

/**
 * 文字列から最後の文字を取得する
 */
fun getLastHiragana(str: String): Char? {
    val hiragana = str.filter { it in '\u3041'..'\u3096' }
    return if (hiragana.isEmpty()) {
        null
    } else {
        hiragana.last()
    }
}

/**
 * 文字列から最初の文字を取得する
 */
fun getFirstHiragana(str: String): Char? {
    val hiragana = str.filter { it in '\u3041'..'\u3096' }
    return if (hiragana.isEmpty()) {
        null
    } else {
        hiragana.first()
    }
}