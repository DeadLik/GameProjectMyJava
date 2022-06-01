package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    AnimPlayer rhinocerosAnim;
    Label label;
    Texture img;

    private int x;

    @Override
    public void create() {
        batch = new SpriteBatch();
        rhinocerosAnim = new AnimPlayer("NosRogAnim.png", 8, 1, 16, Animation.PlayMode.LOOP);
        label = new Label(36);
        img = new Texture("Cloud.jpg");
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) x--;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) x++;

        rhinocerosAnim.step(Gdx.graphics.getDeltaTime());

        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(rhinocerosAnim.getFrame(), x, Gdx.graphics.getHeight() / 2);
        label.draw(batch, "Первая игра!", 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        rhinocerosAnim.dispose();
        img.dispose();
    }
}
