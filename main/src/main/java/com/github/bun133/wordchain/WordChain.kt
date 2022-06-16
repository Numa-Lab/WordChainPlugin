package com.github.bun133.wordchain

import com.github.bun133.wordchain.command.WordChainStartCommand
import com.github.bun133.wordchain.listener.ChatListener
import dev.kotx.flylib.flyLib
import net.kunmc.lab.configlib.ConfigCommandBuilder
import org.bukkit.plugin.java.JavaPlugin

class WordChain : JavaPlugin() {
    lateinit var conf: WordChainConfig
    override fun onEnable() {
        conf = WordChainConfig(this).also {
            it.saveConfigIfAbsent()
            it.loadConfig()
        }

        flyLib {
            command(WordChainStartCommand(conf))
        }

        ChatListener(conf, ChatProcess(conf)).also {
            server.pluginManager.registerEvents(it, this)
        }
    }

    override fun onDisable() {
        conf.saveConfigIfAbsent()
    }
}