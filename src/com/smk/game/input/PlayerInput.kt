package com.smk.game.input

import com.rafl.engine.input.InputObserver
import com.smk.game.input.keyboard.Key
import com.smk.game.states.play.model.Actors
import com.smk.game.utils.Direction
import java.util.EnumSet

object PlayerInput : InputObserver<Key>{

    override fun registerInputs(activated: MutableSet<Key>?) {
        val dirs = EnumSet.noneOf(Direction::class.java)
        activated?.forEach {
                when(it) {
                Key.UP -> dirs.add(Direction.UP)
                Key.DOWN -> dirs.add(Direction.DOWN)
                Key.LEFT -> dirs.add(Direction.LEFT)
                Key.RIGHT -> dirs.add(Direction.RIGHT)
            }
        }

        //Actors.player.move(dirs)
    }

}