package me.znotchill.blossom.server.essentials.classes

import me.znotchill.blossom.server.BlossomServer

interface Essential<C : EssentialConfig> {
    val config: C

    fun load(server: BlossomServer)
}

interface EssentialConfig