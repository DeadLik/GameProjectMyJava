package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyCharacter {
    private AnimPlayer animRight, animJump, animIdle;
    private float dTime;
    private boolean dir, run, jump;
    private Rectangle rectangle;

    public MyCharacter() {
        animRight = new AnimPlayer("hero/runRight.png", 8, 1, 16.0f, Animation.PlayMode.LOOP);
        animJump = new AnimPlayer("hero/jump.png", 1, 1, 16.0f, Animation.PlayMode.LOOP);
        animIdle = new AnimPlayer("hero/idle.png", 1, 1, 16.0f, Animation.PlayMode.LOOP);
        rectangle = new Rectangle(Gdx.graphics.getWidth() / 2.63f, Gdx.graphics.getHeight() / 2, animIdle.getFrame().getRegionWidth(), animIdle.getFrame().getRegionHeight());
    }

    public void setTime(float time) {dTime = time;}

    public TextureRegion getFrame() {
        dTime = Gdx.graphics.getDeltaTime();
        TextureRegion tmp;
        if (dir & run & !jump) {
            animRight.step(dTime);
            if (!animRight.getFrame().isFlipX()) animRight.getFrame().flip(true, false);
            tmp = animRight.getFrame();
        } else if (!dir & run & !jump) {
            animRight.step(dTime);
            if (animRight.getFrame().isFlipX()) animRight.getFrame().flip(true, false);
            tmp = animRight.getFrame();
        } else if (dir & !run & !jump) {
            animIdle.step(dTime);
            if (!animIdle.getFrame().isFlipX()) animIdle.getFrame().flip(true, false);
            tmp = animIdle.getFrame();
        } else if (!dir & !run & !jump) {
            animIdle.step(dTime);
            if (animIdle.getFrame().isFlipX()) animIdle.getFrame().flip(true, false);
            tmp = animIdle.getFrame();
        } else if (dir & !run & jump) {
            animJump.step(dTime);
            if (!animJump.getFrame().isFlipX()) animJump.getFrame().flip(true, false);
            tmp = animJump.getFrame();
        } else {
            animJump.step(dTime);
            if (animJump.getFrame().isFlipX()) animJump.getFrame().flip(true, false);
            tmp = animJump.getFrame();
        }
        return tmp;
    }

    public void setWalk(boolean b) {run = b;}
    public void setDir(boolean b) {dir = b;}
    public Rectangle getRect() {return rectangle;}
}
