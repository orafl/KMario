package com.smk.game.utils

import java.awt.Dimension
import java.awt.Rectangle

data class Point(val x: Int, val y: Int) {

    companion object {
        val ORIGIN = Point(0, 0)
    }

    operator fun plus(vec2: Vector2) =
            Point(x + vec2.x.round(), y + vec2.y.round())
    operator fun minus(vec2: Vector2) =
            Point(x - vec2.x.round(), y - vec2.y.round())
}

data class Vector2(val x: Float, val y: Float) {

    companion object {
        val NULL = Vector2(0f, 0f)
    }

    operator fun plus(other: Vector2) =
            Vector2(x + other.x, y + other.y)

    operator fun minus(other: Vector2) =
            Vector2(x - other.x, y - other.y)

    operator fun times(scalar: Int) = Vector2(x * scalar, y * scalar)

    operator fun times(scalar: Float) =
            Vector2(x * scalar, y * scalar)

    operator fun times(other: Vector2) =
            Vector2(x * other.x, y * other.y)

    operator fun div(div: Float) =
            if(div == 0f) throw ArithmeticException("division by zero")
            else Vector2(x/div, y/div)

    operator fun unaryPlus() = Vector2(+x, +y)
    operator fun unaryMinus() = Vector2(-x, -y)

    operator fun inc() = Vector2(x + 1, y + 1)
    operator fun dec() = Vector2(x - 1, y - 1)

    fun abs() = Vector2(Math.abs(x), Math.abs(y))
}

data class Velocity(val distance: Vector2, val time: Int)

enum class Direction { UP, DOWN, LEFT, RIGHT; }
enum class Axis { X, Y }

/**----------------------
 *  Utility functions
 * ----------------------
 */
operator fun Dimension.contains(p: Point) =
        p.x <= this.width && p.y <= this.height

operator fun Rectangle.contains(p: Point) =
        p.x <= this.width && p.y <= this.height

operator fun Dimension.times(scale: Int) =
        Dimension(this.width * scale, this.height *scale)

operator fun Vector2.div(time: Int) = Velocity(this, time)

operator fun Float.times(vector2: Vector2) =
        Vector2(this * vector2.x, this * vector2.y)

operator fun Double.times(vector2: Vector2) = this.toFloat() * vector2

operator fun Int.times(vector2: Vector2) =
        Vector2(this * vector2.x, this * vector2.y)

infix fun Vector2.limit(by: Vector2) : Vector2 {
    val abs = this.abs()

    return Vector2(if(abs.x > by.x) Math.signum(this.x) * by.x else this.x,
            if(abs.y > by.y) Math.signum(this.y) * by.y else this.y)
}