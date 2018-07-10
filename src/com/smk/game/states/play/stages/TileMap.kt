package com.smk.game.states.play.stages

import com.rafl.engine.gfx.Renderer
import com.rafl.engine.gfx.Sprite
import com.smk.game.utils.Point
import com.smk.game.utils.contains
import com.smk.game.utils.times
import java.awt.Dimension
import java.awt.Rectangle

class TileMap(private val rows: Int, private val columns: Int,
              val tileSize: Int, val origin: Point) {

    class Tile(val sprite: Sprite, val solid: Boolean)

    val sizeInPixels by lazy {Dimension(rows, columns) * tileSize}

    private val tiles = Array<Tile?>(rows*columns) {null}

    operator fun get(x: Int, y: Int) : Tile? {
        if(x < 0 || x >= rows) return null
        else if (y < 0 || y >= columns) return null

        return tiles[x + y * columns]
    }

    fun solidTile(x: Int, y: Int) : Boolean {
        val t = this[x, y]
        return t != null && t.solid
    }

    fun onCollision(x: Int, y: Int, response: (Rectangle) -> Unit) {
        val t = this[x, y] ?: return
        if(t.solid) response(Rectangle(
                x*tileSize, y*tileSize, tileSize, tileSize))
    }

    operator fun set(x: Int, y: Int, tile: Tile?) {
        tiles[x + y * columns] = tile
    }

    private fun iterate(action: (x: Int, y: Int) -> Unit) {
        for(y in 0 until columns)
            for (x in 0 until rows) action(x, y)
    }

    fun render(r: Renderer?, camera: Camera) {
        iterate {x, y ->
            val pos = camera.offset(x*tileSize, y*tileSize)
            if(pos in camera.bounds) {
                val t = this[x, y] ?: return@iterate
                r?.render(t.sprite, pos.x.toInt(), pos.y.toInt())
            }
        }
    }

}