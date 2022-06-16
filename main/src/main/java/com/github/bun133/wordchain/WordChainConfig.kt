package com.github.bun133.wordchain

import com.github.bun133.bukkitfly.server.banPlayerByName
import net.kunmc.lab.configlib.BaseConfig
import net.kunmc.lab.configlib.value.BooleanValue
import net.kunmc.lab.configlib.value.EnumValue
import net.kunmc.lab.configlib.value.StringValue
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class WordChainConfig(plugin: Plugin) : BaseConfig(plugin) {
    // GooラボのAPIキー
    val gooApiKey = StringValue("")

    // しりとり失敗したときの挙動(最後に"ん"がついたとき)
    val toBANPlayer = EnumValue(FailTreat.KICK)

    // 実行中かどうか
    val isGoingOn = BooleanValue(false)

    // 前のチャットの最後のひらがなのヒントを出力するかどうか
    val toPrintLastChar = BooleanValue(true)
}

enum class FailTreat(val treat: (Player) -> Unit) {
    BAN({
        Bukkit.getServer().banPlayerByName(it, true)
    }),
    KICK({
        it.kick(com.github.bun133.bukkitfly.component.text("キックされました"))
    }),
    NONE({});
}