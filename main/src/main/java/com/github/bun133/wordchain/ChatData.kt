package com.github.bun133.wordchain

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

data class ChatData(
    val sender: Player,
    val message: TextComponent
) {
    fun broadcast() {
        Bukkit.broadcast(message)
    }
}
