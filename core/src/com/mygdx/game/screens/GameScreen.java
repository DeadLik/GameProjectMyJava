package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Coin;
import com.mygdx.game.Label;
import com.mygdx.game.MyCharacter;
import com.mygdx.game.PhysX;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private ShapeRenderer renderer; //ДЛЯ ОТЛАДКИ
    private Label label;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private List<Coin> coinList;
    private Texture fon;
    private MyCharacter nosRog;
    private PhysX physX;
    private Music music;

    private int[] foreGround, backGround;

    private int score;

    private boolean levelEnd;

    final Game game;

    public GameScreen(Game game) {
        this.game = game;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        physX = new PhysX();

        nosRog = new MyCharacter();
        fon = new Texture("fon.png");
        map = new TmxMapLoader().load("maps/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        RectangleMapObject o = (RectangleMapObject) map.getLayers().get("Слой объектов 2").getObjects().get("camera");

        if (map.getLayers().get("land") != null) {
            MapObjects mo = map.getLayers().get("land").getObjects();
            physX.addObjects(mo);
        }
        MapObject mo1 = map.getLayers().get("Слой объектов 2").getObjects().get("hero");
        physX.addObject(mo1);
        System.out.printf("" + physX.rockInit());

        foreGround = new int[1];
        foreGround[0] = map.getLayers().getIndex("Слой тайлов 2");
        backGround = new int[1];
        backGround[0] = map.getLayers().getIndex("Слой тайлов 1");

        batch = new SpriteBatch();
        renderer = new ShapeRenderer(); //ДЛЯ ОТЛАДКИ

        label = new Label(36);

        coinList = new ArrayList<>();
        MapLayer ml = map.getLayers().get("coins");
        if (ml != null) {
            MapObjects mo = ml.getObjects();
            if (mo.getCount() > 0) {
                for (int i = 0; i < mo.getCount(); i++) {
                    RectangleMapObject tmpMo = (RectangleMapObject) ml.getObjects().get(i);
                    Rectangle rect = tmpMo.getRectangle();
                    coinList.add(new Coin(new Vector2(rect.x, rect.y)));
                }
            }
        }

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/musicBG.mp3"));
        music.setLooping(true);
        music.setVolume(1f);
        music.play();

        camera.zoom = 0.5f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        levelEnd = false;
        ScreenUtils.clear(1, 0, 0, 1);

        nosRog.setWalk(false);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            physX.setHeroForce(new Vector2(-500, 0));
            nosRog.setDir(true);
            nosRog.setWalk(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            physX.setHeroForce(new Vector2(500, 0));
            nosRog.setDir(false);
            nosRog.setWalk(true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && physX.cl.isOnGround()) {
            physX.setHeroForce(new Vector2(0, 1300));
        }

        //if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y--;

        camera.position.x = physX.getHero().getPosition().x;
        camera.position.y = physX.getHero().getPosition().y;
        camera.update();

        batch.begin();
        batch.draw(fon, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render(backGround);

        batch.begin();
        batch.draw(nosRog.getFrame(), Gdx.graphics.getHeight() / 1.58f, Gdx.graphics.getHeight() / 2.20f);
        label.draw(batch, "Монет собрано: " + String.valueOf(score), 0, 0);

        for (int i = 0; i < coinList.size(); i++) {
            int state;
            state = coinList.get(i).draw(batch, camera);
            if (coinList.get(i).isOverlaps(nosRog.getRect(), camera)) {
                if (state == 0) coinList.get(i).setState();
                if (state == 2) {
                    coinList.remove(i);
                    score++;
                    if (score > 7) {
                        levelEnd = true;
                    }
                }
            }
        }

        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render(foreGround);

/*        Color heroClr = new Color(Color.WHITE);
        mapRenderer.render(foreGround);
        renderer.setColor(heroClr);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < coinList.size(); i++) {
            coinList.get(i).shapeDraw(renderer, camera);
            if (coinList.get(i).isOverlaps(nosRog.getRect(), camera)) {
                coinList.remove(i);
                heroClr = Color.BLUE;
            }
        }
        renderer.setColor(heroClr);
        renderer.rect(nosRog.getRect().x, nosRog.getRect().y, nosRog.getRect().width, nosRog.getRect().height);
        renderer.end(); // ДЛЯ ОТЛАДКИ*/

        physX.step();
        //physX.debugDraw(camera);


        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.GRAY);
        for (Fixture fixture : physX.rockBodys) {
            float cx = (fixture.getBody().getPosition().x - camera.position.x) / camera.zoom + Gdx.graphics.getWidth() / 2;
            float cy = (fixture.getBody().getPosition().y - camera.position.y) / camera.zoom + Gdx.graphics.getHeight() / 2;
            float cR = fixture.getShape().getRadius() / camera.zoom;
            renderer.circle(cx, cy, cR);
        }
        renderer.end();

        if (levelEnd == true) {
            game.setScreen(new InScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        coinList.get(0).dispose();
        physX.dispose();
        music.stop();
        music.dispose();
    }
}
