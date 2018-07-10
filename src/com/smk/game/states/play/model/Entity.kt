package com.smk.game.states.play.model

import com.rafl.engine.gfx.Animation
import com.rafl.engine.gfx.Sprite
import com.smk.game.states.play.stages.Stage
import com.smk.game.utils.Axis
import com.smk.game.utils.Direction
import com.smk.game.utils.Point
import com.smk.game.utils.Vector2
import com.smk.game.utils.Velocity
import com.smk.game.utils.limit
import com.smk.game.utils.times
import java.awt.Color
import java.awt.Dimension

open class Entity(origin: Point, velocity: Velocity) {

    var position: Point = origin; set(value) {
        field = value; destination = value
    }
    private var destination: Point = position
    private var speed: Vector2 = Vector2.NULL

    var isIdle = true; private set

    private val acceleration: Vector2 = velocity.distance
    val maxSpeed: Vector2 = velocity.time * acceleration

    val MAX_FORCE = Vector2(0f, 3f)

    private var pull = Vector2.NULL

    private fun advance() {
        destination = position + speed
    }

    private fun move(dx: Float, dy: Float, cap: Vector2) {
        if(dx == 0f && dy == 0f) return

        speed += Vector2(if(dx == 0f && pull.x == 0f) -speed.x else dx,
                         if(dy == 0f && pull.y == 0f) -speed.y else dy)
        speed = speed limit cap
        advance()
    }

    fun move(dir: Set<Direction>) {
        if(dir.isEmpty()) return
        var dx = 0f; var dy = 0f

        dir.forEach {
            when(it) {
                Direction.UP -> dy -= acceleration.y
                Direction.DOWN -> dy += acceleration.y
                Direction.LEFT -> dx -= acceleration.x
                Direction.RIGHT -> dx += acceleration.x
            }
        }

        move(dx, dy, cap = maxSpeed)
    }

    fun update(dt: Double) {

        var px = pull.x; var py = pull.y

        if(position != destination) {
            val intent = dt * speed + pull
            position += Vector2(
                    if(canMoveHorizontally(intent)) intent.x else 0f.also { px = 0f },
                    if(canMoveVertically(intent)) intent.y else 0f.also { py = 0f })
            isIdle = false
        }

        move(px, py, cap = MAX_FORCE)

        onUpdate(dt)
    }

    protected open fun onUpdate(dt: Double) {}

    protected open fun canMoveHorizontally(intent: Vector2) = true
    protected open fun canMoveVertically(intent: Vector2) = true

    fun exert(force: Vector2) { pull += force }

    fun block(axis: Axis) = when (axis) {
        Axis.X -> pull = Vector2(0f, pull.y)
        Axis.Y -> pull = Vector2(pull.x, 0f)
    }

    open fun size() : Dimension = Dimension(1, 1)
}