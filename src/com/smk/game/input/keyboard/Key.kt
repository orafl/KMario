package com.smk.game.input.keyboard

import java.awt.event.KeyEvent

enum class Key(private val code: Int) {

    UP(KeyEvent.VK_W), DOWN(KeyEvent.VK_S),
    LEFT(KeyEvent.VK_A), RIGHT(KeyEvent.VK_D);

    companion object {
        private val map = HashMap<Int, Key>()
        operator fun get(code: Int) = map[code]

        init { for(k in Key.values()) map[k.code] = k }
    }
}