package com.smk.game

import com.rafl.engine.execution.AbstractGame
import com.rafl.engine.execution.GameContext
import com.rafl.engine.gfx.Display
import com.rafl.engine.gfx.Renderer
import com.rafl.engine.sfx.AudioPlayer
import com.smk.game.input.keyboard.KeyInput
import com.smk.game.states.test.NewActors
import java.awt.Dimension

fun main(args: Array<String>) {
    val game = Game()
    Config.init(game)
    game.context.setState(NewActors::class.java)
    game.start()
}

class Game : AbstractGame(60) {

    internal val display: Display = Display("Game", 840)
    private val renderer: Renderer
    internal val context: GameContext

    init {
        display.createView()
        display.addKeyListener(Config.keyListener)

        this.renderer = Renderer(display)
        this.context = GameContext()
    }

	override fun everySec() {
        println(super.getCurrentFps())
	}

	override fun onUpdate(delta: Double) {
        display.requestFocus()
        Config.keyListener.update()
		context.update(delta)
	}

	override fun onRender() {
		renderer.clear()
        context.render(renderer)
        display.show()
	}
}

object Config {
    val keyListener = KeyInput()
    val bgm = AudioPlayer()
    lateinit var resolution: Dimension; private set

    fun init(game: Game) {
        val display = game.display
        resolution = Dimension(display.width, display.height)
    }
}