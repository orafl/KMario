package com.smk.game.states.play.model

import com.rafl.engine.gfx.Animation
import com.rafl.engine.gfx.SpriteSheet
import com.smk.game.states.play.actions.Action
import com.smk.game.utils.Vector2
import com.smk.game.utils.loadSprite
import java.awt.Color

class Character
private constructor( val role: Role, private val actions: Map<Action, Animation>,
                     val speed: Float, val weight: Int) {
    companion object {
        private val chars = HashMap<String, Character>()

        operator fun get(name: String) = chars[name] ?:
            when(name) {
                "mario" -> create("mario", Role.PLAYER, marioActions(), 1.5f,  0)

                "goomba" -> create("goomba", Role.ENEMY, mapOf(
                        Action {_, a -> /*if(!a.falling) a.move()
                       if(a.collides) {
                           a.speed =  Vector2(-2f, 0f)
                           a.moveLeft()
                       } */} to Animation.Builder(SpriteSheet(
                                loadSprite("/sprites/goomba.png"), 32, 32, 2, 0))
                                .frame(0, 0, 100)
                                .frame(1, 0, 100)
                                .setTransparency(Color(0x93bbec))
                                .build()), 1.5f, 1)

                "koopa" -> create("koopa", Role.ENEMY, mapOf(

                ), 1.5f, 2)

                else -> throw IllegalArgumentException("No such character")
            }

        private fun create(name: String, role: Role, actions: Map<Action, Animation>,
                           speed: Float, weight: Int) =
                Character(role, actions, speed, weight).also { chars[name] = it }

        private fun marioActions() =
                mapOf(Action {_, _ ->} to Animation.Builder(SpriteSheet(
                            loadSprite("/sprites/smb-mario-small.png"), 32, 31, 2, 2))
                            .frame(1, 0, 100)
                            .frame(2, 0, 100)
                            .frame(3, 0, 100)
                            .setTransparency(Color.WHITE)
                            .build())
    }

    fun getActions() : Map<Action, Animation> {
        val map = HashMap<Action, Animation>()
        actions.forEach { action, animation -> map[action] = Animation.from(animation) }
        return map
    } //or alternatively: actions.keys.zip(actions.values.map { Animation.from(it)}).toMap()

}