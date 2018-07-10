package com.smk.game.input

import com.rafl.engine.input.InputDevice
import com.rafl.engine.input.InputObserver

abstract class AbstractInputDevice<T> : InputDevice<T> {

    private val observers = ArrayList<InputObserver<T>?>()

    fun update(input: Set<T>) {
        observers.forEach { it?.registerInputs(input) }
    }

    final override fun add(ob: InputObserver<T>?) {
        observers.add(ob)
    }

    final override fun remove(ob: InputObserver<T>?) {
        observers.remove(ob)
    }
}