package com.smk.game.states.play.model

import com.rafl.engine.gfx.Animation
import com.rafl.engine.gfx.Sprite
import com.smk.game.states.play.stages.Stage
import com.smk.game.utils.Axis
import com.smk.game.utils.Point
import com.smk.game.utils.Vector2
import com.smk.game.utils.Velocity
import com.smk.game.utils.limit
import java.awt.Color
import java.awt.Dimension

class Actor(origin: Point, velocity: Velocity,
            physicsComponent: Actor.() -> PhysicsComponent)
    : Entity(origin, velocity), Visible {

    private var _sprite: Sprite? = null
    var animation: Animation? = null
    private val physics = physicsComponent()

    override val sprite: Sprite get() = _sprite ?: Sprite(32, 32, Color.YELLOW)

    override fun onUpdate(dt: Double) {
        _sprite = animation?.play()
        physics.update()
    }

    override fun canMoveHorizontally(intent: Vector2): Boolean =
            !physics.collidesHorizontally(intent)

    override fun canMoveVertically(intent: Vector2): Boolean =
            !physics.collidesVertically(intent)

    override fun size(): Dimension = sprite.dimension

}

class PhysicsComponent(val actor: Actor, private var gravity: Vector2, val stage: Stage?) {

    private var falling: Boolean = false; set(value) {
        if(value != field) if(value) actor.exert(gravity) else actor.block(Axis.Y)
        field = value
    }

    init {
        falling = true
        stage?.add(actor)
    }

    fun update() {
        //gravity += gravity; gravity = gravity limit MAX_GRAVITY
    }

    fun collidesHorizontally(intent: Vector2) : Boolean {
        val map = stage?.map ?: return false
        val size = actor.size()

        val xvertex = ((actor.position.x + if(intent.x > 0) size.width else 0)
                + intent.x.toInt())/map.tileSize

        return map.solidTile(xvertex, actor.position.y/map.tileSize)
                || map.solidTile(xvertex, (actor.position.y + size.height)/map.tileSize)
    }

    fun collidesVertically(intent: Vector2) : Boolean {
        val map = stage?.map ?: return false
        val size = actor.size()

        val yvertex = ((actor.position.y + if(intent.y > 0) size.height + 1 else 0)
                + intent.y.toInt())/map.tileSize

        var r = false

        map.onCollision(actor.position.x/map.tileSize, yvertex) {
            falling = false; r = true
            with(actor.position) {
                actor.position = Point(x, 0/*it.y +
                        if(intent.y > 0) -size.height else size.height*/)
            }
        }

        map.onCollision((actor.position.x + size.width)/map.tileSize, yvertex) {
            falling = false; r = true
            with(actor.position) {
                actor.position = Point(x, 0/*it.y +
                        if(intent.y > 0) -size.height else size.height*/)
            }
        }

        /*if(map.solidTile(actor.position.x/map.tileSize, yvertex)) {
            actor.position = Point(actor.position.x/map.tileSize, yvertex)
            falling = false; return true
        }

        if(map.solidTile((actor.position.x + size.width)/map.tileSize, yvertex)) {
            actor.position = Point((actor.position.x + size.width)/map.tileSize, yvertex)
            falling = false; return true
        }*/

        falling = true; return r

        /*return if(map.solidTile(actor.position.x/map.tileSize, yvertex)
                || map.solidTile((actor.position.x + size.width)/map.tileSize, yvertex))
            true.also {
                falling = false

            }
        else false.also { falling = true }*/
    }

}

/*
class Actor(private val char: Character, origin: Vector2 = Vector2(0f, 0f)) {

    var position: Vector2 = origin
    var sprite: Sprite? = Sprite(32, 32, Color.RED); private set
    private val actions = char.getActions()

    private var destination = Vector2(0f, 0f)
    var speed = Vector2(char.speed, char.speed)
    var falling = true; private set

    private val g = Vector2(0f, 0.16f)
    private val maxSpeed = Vector2(15f, 15f)

    var collides = false

    fun update(dt: Double, stage: Stage) {
        actions.forEach { (action, animation) ->
            action.act(dt, this)
            sprite = animation.play()
        }

        if(position != destination) {
            move(dt, destination, stage)
        }

        destination = Vector2(0f, 0f)

        if(falling) {
            if(speed.y < maxSpeed.y) speed += (g * char.weight)
        } else speed = Vector2(char.speed, char.speed)
        moveDown()
    }

    fun move() { destination += speed }

    private fun move(dt: Double, dest: Vector2, stage: Stage) {

        val nextPos = dest * dt.toFloat()
        val map = stage.map ?: return Unit.also { position += dest }

        var xpos = position.x
        var ypos = position.y

        val xvertex = (getX() + (if(nextPos.x > 0) 1+getWidth() else 0) + nextPos.x).toInt()/map.tileSize

        if(!map.solidTile(xvertex, getY()/map.tileSize) &&
                !map.solidTile(xvertex, (getY() + getHeight())/map.tileSize)) {
            xpos += nextPos.x
            collides = false
        }
        else collides = true

        val yvertex = (getY() + (if(nextPos.y > 0) 1+getHeight() else 0) + nextPos.y).toInt()/map.tileSize

        if(!map.solidTile(getX()/map.tileSize, yvertex) &&
                !map.solidTile((getX() + getWidth())/map.tileSize, yvertex)){
            ypos += nextPos.y
            falling = true
        }
        else {
            if(nextPos.y > 0) falling = false
        }

        position = Vector2(xpos, ypos)

    }

	fun moveDown() { destination += Vector2(0f, speed.y) }
	
	fun moveUp() { destination += Vector2(0f, -(speed.y*2)) }

	fun moveLeft() { destination += Vector2(-speed.x, 0f) }

	fun moveRight() { destination += Vector2(speed.x, 0f) }

    fun getX() = position.x.toInt()
    fun getY() = position.y.toInt()

    fun getWidth() = sprite?.width ?: 0
    fun getHeight() = sprite?.height ?: 0

}*/
