package com.smk.game.states.play

import com.rafl.engine.execution.GameContext
import com.rafl.engine.execution.GameState
import com.rafl.engine.gfx.Renderer
import com.rafl.engine.utils.Timer
import com.smk.game.Config
import com.smk.game.input.PlayerInput
import com.smk.game.states.play.model.Actor
import com.smk.game.states.play.model.Actors
import com.smk.game.states.play.model.Character
import com.smk.game.states.play.stages.Camera
import com.smk.game.states.play.stages.Stage
import com.smk.game.utils.Point
import com.smk.game.utils.Vector2
import com.smk.game.utils.loadTilemap

class PlayState : GameState {

    private val cam = Camera(Config.resolution)

    private lateinit var stage: Stage

    fun setStage(path: String, actors: Array<Actor>) {
        val map = loadTilemap(path) ?: throw IllegalArgumentException("Failed to load map")
        /*Actors.player.position =  Point(map.origin.x * map.tileSize,
                map.origin.y * map.tileSize)
        stage = Stage(map, actors,  cam)
        stage.add(Actors.player)*/
    }

    override fun initialize(context: GameContext?) {
		Config.keyListener.add(PlayerInput)
        setStage("/maps/mymap.txt", arrayOf())
	}

    private val timer = Timer(0.0)

    override fun update(delta: Double) {
        timer.refreshRate = 500.0 * delta
    	/*cam.focus = Actors.player.position
        if(timer.countdown()) stage.add(Actor(Character["goomba"]))*/
        stage.update(delta)
    }

    override fun render(renderer: Renderer?) {
        stage.render(renderer)
    }
}
