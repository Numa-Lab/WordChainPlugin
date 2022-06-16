package com.github.bun133.wordchain.axios

class GooRequest(val message: String, val apiKey: String) {
    fun req() = AxiosTask(
        "https://labs.goo.ne.jp/api/hiragana",
        listOf(
            "app_id" to apiKey,
            "sentence" to message,
            "output_type" to "hiragana"
        )
    )
}