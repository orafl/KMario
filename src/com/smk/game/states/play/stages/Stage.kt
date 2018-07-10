package com.smk.game.states.play.stages

import com.rafl.engine.gfx.Renderer
import com.smk.game.Config
import com.smk.game.states.play.model.Actor
import com.smk.game.utils.contains
import java.awt.Dimension

class Stage(val map: TileMap?,
            actors: Array<Actor>,
            private val camera: Camera) {

    private val actors = actors.toMutableList()

    fun add(actor: Actor) = actors.add(actor)
    fun remove(actor: Actor) = actors.remove(actor)

    fun update(dt: Double) {
        camera.update(map?.sizeInPixels ?: Dimension(Config.resolution))
        actors.filter { camera.offset(it.position) in camera.bounds }
        .forEach { it.update(dt) }
    }

    fun render(r: Renderer?) {
        map?.render(r, camera)
        actors.forEach {
            val position = camera.offset(it.position)
            if(position in camera.bounds)
                r?.render(it.sprite, position.x, position.y)
        }
    }

}