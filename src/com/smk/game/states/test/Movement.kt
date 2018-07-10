package com.smk.game.states.test

import com.rafl.engine.execution.GameContext
import com.rafl.engine.execution.GameState
import com.rafl.engine.gfx.Renderer
import com.rafl.engine.gfx.Sprite
import com.rafl.engine.input.InputObserver
import com.smk.game.Config
import com.smk.game.input.keyboard.Key
import com.smk.game.states.play.model.Entity
import com.smk.game.utils.Direction
import com.smk.game.utils.Point
import com.smk.game.utils.Vector2
import java.awt.Color
import com.smk.game.utils.div
import java.util.EnumSet

class EntityMovement : GameState {

    private val e = Entity(origin = Point(0, 0), velocity = Vector2(3f, 3f)/2)

    private val eSprite = Sprite(40, 40, Color.RED)

    private val controller = InputObserver<Key> { keys ->
        val dirs = EnumSet.noneOf(Direction::class.java)
        keys.forEach {
            if(it != null) when(it) {
                Key.UP -> dirs.add(Direction.UP)
                Key.DOWN -> dirs.add(Direction.DOWN)
                Key.LEFT -> dirs.add(Direction.LEFT)
                Key.RIGHT -> dirs.add(Direction.RIGHT)
            }
        }

        e.move(dirs)
    }

    override fun update(delta: Double) {
        e.update(delta)
    }

    override fun render(renderer: Renderer?) {
        with(e.position) {
            renderer?.render(eSprite, x, y)
        }
    }

    override fun initialize(context: GameContext?) {
        Config.keyListener.add(controller)
        e.exert(Vector2(0f, .16f))
    }
}