package com.smk.game.utils

fun clamp(value: Int, min:Int, max: Int) =
        Math.min(Math.max(value, min), max)

fun Float.round() : Int =
        if(this > 0) Math.round(this)
        else (Math.round(Math.abs(this)) * Math.signum(this)).toInt()
