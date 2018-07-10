import com.rafl.engine.execution.AbstractGame
import com.rafl.engine.execution.GameContext
import com.rafl.engine.execution.GameState
import com.rafl.engine.gfx.Display
import com.rafl.engine.gfx.Renderer
import com.rafl.engine.input.InputObserver
import com.rafl.engine.sfx.AudioPlayer
import com.smk.game.input.keyboard.Key
import com.smk.game.input.keyboard.KeyInput
import com.smk.game.states.play.model.Actor
import com.smk.game.states.play.model.PhysicsComponent
import com.smk.game.states.play.stages.Camera
import com.smk.game.states.play.stages.Stage
import com.smk.game.utils.Direction
import com.smk.game.utils.Point
import com.smk.game.utils.Vector2
import com.smk.game.utils.Velocity
import com.smk.game.utils.loadTilemap
import java.awt.Dimension
import java.util.EnumSet

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

class NewActors : GameState {

    val cam = Camera(Config.resolution)
    val stage = Stage(loadTilemap("/maps/mymap.txt") ?:
    throw IllegalArgumentException("Failed to load map"), arrayOf(), cam)

    val player = Actor(Point.ORIGIN, Velocity(Vector2(1f, 1f), 2)){
        PhysicsComponent(this, Vector2(0f, .16f), stage)
    }

    val controller = InputObserver<Key> {
        val dirs = EnumSet.noneOf(Direction::class.java)
        it?.forEach {
            if(it != null) when(it) {
                Key.UP -> dirs.add(Direction.UP)
                Key.DOWN -> dirs.add(Direction.DOWN)
                Key.LEFT -> dirs.add(Direction.LEFT)
                Key.RIGHT -> dirs.add(Direction.RIGHT)
            }
        }

        player.move(dirs)
    }

    override fun update(delta: Double) {
        cam.focus = player.position
        stage.update(delta)
    }

    override fun render(renderer: Renderer?) {
        stage.render(renderer)
    }

    override fun initialize(context: GameContext?) {
        Config.keyListener.add(controller)
    }
}