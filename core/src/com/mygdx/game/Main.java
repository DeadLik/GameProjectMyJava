package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.InScreen;

public class Main extends Game {

    @Override
    public void create () {
        this.setScreen(new InScreen(this));
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
