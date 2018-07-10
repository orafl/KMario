package com.smk.game.states.play.stages

import com.smk.game.utils.Point
import com.smk.game.utils.clamp
import java.awt.Dimension
import java.awt.Rectangle

class Camera(width: Int, height: Int) {

    constructor(dimension: Dimension) : this(
            dimension.width, dimension.height)

    val bounds = Rectangle(0,0, width, height)
    var focus: Point = Point(0, 0)

    fun update(context: Dimension) {
        bounds.x = clamp(focus.x.toInt() - bounds.width / 2,
                0, context.width - bounds.width)
        bounds.y = clamp(focus.y.toInt() - bounds.height / 2,
                0, context.height - bounds.height)
    }

    fun offset(x: Int, y:Int) =
            Point(x - bounds.location.x, y - bounds.location.y - 1)

    fun offset(vec2: Point) = offset(vec2.x, vec2.y)
}