package com.github.bun133.wordchain

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.tinked.RunnableTask
import com.github.bun133.wordchain.axios.GooRequest
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


class ChatProcess(val conf: WordChainConfig) {
    // 前回のチャットの最後の文字
    private var final: Char? = null

    /**
     * 現在処理中かどうか
     */
    private var isSyncing = false

    fun process(e: AsyncChatEvent) {
        val t = e.message() as? TextComponent
        if (t != null) {
            val data = ChatData(e.player, t)
            if (isSyncing) {
                e.player.sendMessage(text("現在処理中です", NamedTextColor.RED))
            } else {
                processInter(data)
            }
        } else {
            println("[ERROR] The Component is not textComponent")
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun processInter(chat: ChatData) {
        val text = chat.message as? TextComponent
        if (text != null) {
            isSyncing = true
            val s = text.content()
            measureTime {
                GooRequest(s, conf.gooApiKey.value()).req()
                    .apply {
                        then(RunnableTask {
                            val r = it?.converted
                            println("Converted: $r")
                            if (r != null) {
                                val first = getFirstHiraganaFromString(r)
                                val last = getLastHiraganaFromString(r)

                                println("First Hiragana:${first}")
                                println("Last Hiragana:${last}")

                                if (first != null && last != null) {
                                    // ちゃんとしたチャット
                                    if (final == null || final == first) {
                                        // 前のチャットの最後の文字と、このチャットの最初の文字が合っていたら
                                        final = last

                                        Bukkit.broadcast(
                                            text("[") + chat.sender.displayName() + text("] ") + chat.message.hoverEvent(
                                                HoverEventSource { HoverEvent.showText(text("ひらがな変換:$r")) })
                                        )

                                        if (conf.toPrintLastChar.value()) {
                                            Bukkit.broadcast(text("最後の文字は「${final}」", NamedTextColor.GREEN))
                                        }
                                    } else {
                                        // 前のチャットの最後の文字と、このチャットの最初の文字が合っていなかったら
                                        chat.sender.sendMessage(text("しりとりがつながりませんでした", NamedTextColor.RED))
                                    }
                                } else {
                                    //日本語に変換不能なチャット
                                    chat.sender.sendMessage(text("ひらがなに変換できませんでした", NamedTextColor.RED))
                                }

                                isSyncing = false
                            }
                        })
                    }.run(Unit)
            }.also { println("Converted Time: $it") }
        } else {
            // Failed
            println("Failed: ${chat.message}")
        }
    }
}