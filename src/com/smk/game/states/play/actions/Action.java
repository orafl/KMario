package com.smk.game.states.play.actions;

import com.smk.game.states.play.model.Actor;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Action {
    void act(double dt, @NotNull Actor actor);
}
