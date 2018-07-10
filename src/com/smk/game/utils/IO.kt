package com.smk.game.utils

import com.rafl.engine.gfx.Sprite
import com.rafl.engine.gfx.SpriteSheet
import com.smk.game.states.play.stages.TileMap
import java.awt.Dimension
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.imageio.ImageIO

private class R

fun loadResource(path: String) : InputStream? =
        R::class.java.getResourceAsStream(path)

fun loadSprite(path: String) =
        Sprite(ImageIO.read(loadResource(path)))

fun loadTilemap(path: String) : TileMap? {
    val tokens = lexer(path).split(Regex("\\s+")).iterator()

    var map: TileMap? = null
    var size: Dimension? = null
    var tileset: SpriteSheet? = null
    var origin : Point? = null

    var tile: TileMap.Tile? = null

    while (tokens.hasNext()) {

        if(size != null && tileset != null && origin != null && map == null) {
            map = TileMap(size.width, size.height, tileset.sceneWidth(), origin)
        }

        when(tokens.next()) {
            "size" -> size = Dimension(
                    tokens.next().toInt(), tokens.next().toInt())
            "player@" -> origin = Point(tokens.next().toInt(), tokens.next().toInt())
            "tileset" -> tileset = SpriteSheet(loadSprite(
                    tokens.next()), tokens.next().toInt(),
                    tokens.next().toInt(), tokens.next().toInt())
            "tile" -> tile = TileMap.Tile(tileset?.getSprite(
                    tokens.next().toInt(), tokens.next().toInt()
            ) as Sprite, when(tokens.next()) {
                "solid" -> true
                "void" -> false
                else -> false
            })
            "@" ->  map?.set(tokens.next().toInt(), tokens.next().toInt(), tile)
        }
    }

    return map
}

private fun lexer(path: String): String {

    lateinit var reader: BufferedReader
    try {
        reader = BufferedReader(
                InputStreamReader(loadResource(path)))
        val sb = StringBuilder()
        var line = reader.readLine()

        while(line != null) {
            sb.append("$line\n")
            line = reader.readLine()
        }
        return sb.toString()
    }
    catch (ex: IOException) {
        ex.printStackTrace()
    } finally {
        try {reader.close()} catch (ex: UninitializedPropertyAccessException) {} }

    return ""
}