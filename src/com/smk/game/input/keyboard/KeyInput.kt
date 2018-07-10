package com.smk.game.input.keyboard

import com.smk.game.input.AbstractInputDevice
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.EnumSet

class KeyInput : AbstractInputDevice<Key>(), KeyListener {

    private val pressed = EnumSet.noneOf(Key::class.java)

    override fun update() {
        super.update(pressed)
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        val key = Key[e?.keyCode ?: 0]
        if(key != null) pressed.add(key)
    }

    override fun keyReleased(e: KeyEvent?) {
        val key = Key[e?.keyCode ?: 0]
        if(key != null) pressed.remove(key)
    }
}