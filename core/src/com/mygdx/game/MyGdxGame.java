package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    AnimPlayer rhinocerosAnim;
    Label label;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private List<Coin> coinList;

    private int x;

    @Override
    public void create() {
        map = new TmxMapLoader().load("maps/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        batch = new SpriteBatch();
        rhinocerosAnim = new AnimPlayer("NosRogAnim.png", 8, 1, 16, Animation.PlayMode.LOOP);
        label = new Label(36);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        RectangleMapObject o = (RectangleMapObject) map.getLayers().get("Слой объектов 2").getObjects().get("camera");
        camera.position.x = o.getRectangle().x;
        camera.position.y = o.getRectangle().y;
        camera.zoom = 0.5f;
        camera.update();

        coinList = new ArrayList<>();
        MapLayer ml = map.getLayers().get("coins");
        if (ml != null) {
            MapObjects mo = ml.getObjects();
            if (mo.getCount() > 0) {
                for (int i = 0; i < mo.getCount(); i++) {                                       // не придумал как в foreach перевести, перевел нижний for
                    RectangleMapObject tmpMo = (RectangleMapObject) ml.getObjects().get(i);
                    Rectangle rect = tmpMo.getRectangle();
                    coinList.add(new Coin(new Vector2(rect.x, rect.y)));
                }
            }
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.CYAN);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x--;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x++;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y++;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y--;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        rhinocerosAnim.step(Gdx.graphics.getDeltaTime());

        batch.begin();
        batch.draw(rhinocerosAnim.getFrame(), Gdx.graphics.getHeight() / 2, Gdx.graphics.getHeight() / 2);
        label.draw(batch, "Первая игра!", 0, 0);

        for (Coin coin : coinList) {
            coin.draw(batch, camera);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        rhinocerosAnim.dispose();
        coinList.get(0).dispose();
    }
}
