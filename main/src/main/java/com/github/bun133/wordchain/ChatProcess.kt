package com.github.bun133.wordchain

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.bukkitfly.server.plugin
import com.github.bun133.tinked.RunnableTask
import com.github.bun133.tinked.WaitTask
import com.github.bun133.wordchain.axios.GooRequest
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
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

                                        // チャットを送信
                                        Bukkit.broadcast(
                                            text("[") + chat.sender.displayName() + text("] ") + chat.message.hoverEvent(
                                                HoverEventSource { HoverEvent.showText(text("ひらがな変換:$r")) })
                                        )

                                        if (last == 'ん') {
                                            // 最後の文字がんだったら
                                            treatFailPlayer(
                                                conf.toBANPlayer.value(),
                                                chat.sender,
                                                conf.toPrintBAN.value()
                                            )
                                        } else {
                                            // 最後の文字がんでなかったら
                                            // 前回のチャットの最後の文字を更新
                                            final = last
                                        }

                                        // ヒントを表示
                                        if (conf.toPrintLastChar.value()) {
                                            Bukkit.broadcast(text("最後の文字は「${final}」", NamedTextColor.GREEN))
                                        }
                                    } else {
                                        // 前のチャットの最後の文字と、このチャットの最初の文字が合っていなかったら
                                        chat.sender.sendMessage(text("しりとりがつながりませんでした", NamedTextColor.LIGHT_PURPLE))
                                    }
                                } else {
                                    //日本語に変換不能なチャット
                                    chat.sender.sendMessage(text("ひらがなに変換できませんでした", NamedTextColor.YELLOW))
                                }
                            } else {
                                // 変換失敗
                                chat.sender.sendMessage(text("ひらがなに変換できませんでした", NamedTextColor.YELLOW))
                            }
                            isSyncing = false
                        })
                    }.run(Unit)
            }.also { println("Converted Time: $it") }
        } else {
            // Failed
            println("Failed: ${chat.message}")
        }
    }
}

// んを最後に着けてしまった馬鹿者をBANする
fun treatFailPlayer(treat: FailTreat, player: Player, toPrintBan: Boolean) {
    // AsyncでPlayer Kickとか出来ないみたいなので...
    WaitTask<Unit>(1L, plugin()!!).apply(RunnableTask {
        treat.treat(player)
        if (toPrintBan) {
            Bukkit.broadcast(player.displayName() + text("が最後に「ん」をつけました", NamedTextColor.RED))
        }
    }).run(Unit)
}