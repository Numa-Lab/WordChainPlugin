package com.github.bun133.wordchain.listener

import com.github.bun133.wordchain.ChatData
import com.github.bun133.wordchain.ChatProcess
import com.github.bun133.wordchain.WordChainConfig
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListener(val conf: WordChainConfig, val process: ChatProcess) : Listener {
    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        if (conf.isGoingOn.value()) {
            if (e.recipients().containsAll(Bukkit.getOnlinePlayers())) {
                // 全体に送信されてるチャット
                e.isCancelled = true
                process.process(e)   // 丸投げ
            }
        }
    }
}