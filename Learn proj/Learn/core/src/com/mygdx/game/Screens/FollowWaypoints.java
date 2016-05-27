package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enteties.AI;
import com.mygdx.game.Enteties.Car;
import com.mygdx.game.Enteties.Player;
import com.mygdx.game.Enteties.Track;

public class FollowWaypoints implements Screen {
    private ShapeRenderer sr;
    private ShapeRenderer GUI;
    private SpriteBatch b2;
    private SpriteBatch batch;
    private Array<Car> cars;
    private Sprite sprite;
    private Player player;
    private Array<AI> ais;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private BitmapFont bitmapFont;

    private Track track;

    private float timestep = 1 / 60f;
    Vector2 previous;


    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        bitmapFont = new BitmapFont();

        sr = new ShapeRenderer();
        GUI = new ShapeRenderer();
        batch = new SpriteBatch();
        b2=new SpriteBatch();

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 10.0f;
        track=new Track();


        sprite = new Sprite(new Texture("img/carGreen.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x,track.getBeginningPos().y);
        player = new Player(sprite,track.getTrack(),track.getCurvePoints(),0,new Vector2(track.getBeginningPos().x,track.getBeginningPos().y),camera,batch,sr);

        ais = new Array<AI>();
        cars = new Array<Car>();
        cars.add(player.getCar());
        cars.get(0).setPos(1);

        sprite = new Sprite(new Texture("img/carRed.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-25,track.getBeginningPos().y);
        ais.add(new AI(sprite,track.getTrack(),track.getCurvePoints(),1,new Vector2(track.getBeginningPos().x-25,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(0).getCar());
        cars.get(1).setPos(2);

        sprite = new Sprite(new Texture("img/carBlue.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-50,track.getBeginningPos().y);
        ais.add(new AI(sprite,track.getTrack(),track.getCurvePoints(),2,new Vector2(track.getBeginningPos().x-50,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(1).getCar());
        cars.get(2).setPos(3);

        sprite = new Sprite(new Texture("img/carBlack.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-75,track.getBeginningPos().y);
        ais.add(new AI(sprite,track.getTrack(),track.getCurvePoints(),3,new Vector2(track.getBeginningPos().x-75,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(2).getCar());
        cars.get(3).setPos(5);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //update player behaviour
        player.update(delta);
        for (AI car : ais) {
            car.update(delta);
        }


        //draw the track
        sr.setColor(Color.WHITE);
        sr.begin(ShapeRenderer.ShapeType.Line);
        for( Array<Vector2> path : track.getTrack()){

            previous = path.first();

            for(Vector2 waypoint : path){

                sr.line(previous,waypoint);
                previous=waypoint;
            }

            sr.line(previous,path.first());
        }
        sr.end();

        //draw cars
        batch.begin();
        player.getCar().draw(batch);
        for(Car car : cars ){
            car.draw(batch);
        }
        batch.end();


        //draw GUI
        GUI.setColor(Color.RED);
        GUI.begin(ShapeRenderer.ShapeType.Filled);
        GUI.circle(125,125,50);
        GUI.box(Gdx.graphics.getWidth()-250,100,0,50,50,0);
        GUI.box(Gdx.graphics.getWidth()-150,100,0,50,50,0);
        GUI.end();

        b2.begin();
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFont.draw(b2,"fps: " + Integer.toString(Gdx.graphics.getFramesPerSecond()),25,100);
        bitmapFont.draw(b2,"lap: " + Integer.toString(player.getLap()),25,75);
        b2.end();


    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 25;
        camera.viewportHeight = height / 25;
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
        sprite.getTexture().dispose();
        sr.dispose();
        debugRenderer.dispose();
    }

}
