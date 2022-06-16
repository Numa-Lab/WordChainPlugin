package com.github.bun133.wordchain.command

import com.github.bun133.wordchain.WordChainConfig
import dev.kotx.flylib.command.Command
import net.kunmc.lab.configlib.ConfigCommandBuilder


class WordChainStartCommand(val conf: WordChainConfig) : Command("wc") {
    init {
        description("しりとりプラグインの起動/終了コマンド")
        usage {
            selectionArgument("Start/Stop", "Start", "Stop")
            executes {
                when (typedArgs[0]) {
                    "Start" -> {
                        conf.isGoingOn.value(true)
                        success("WordChainを起動しました")
                    }
                    "Stop" -> {
                        conf.isGoingOn.value(false)
                        success("WordChainを停止しました")
                    }
                    else -> fail("Start/Stopを指定してください")
                }
            }
        }

        children(ConfigCommandBuilder(conf).build())
    }
}