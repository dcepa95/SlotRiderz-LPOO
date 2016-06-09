package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.LoadingScreen;

/**
 * Game application
 */
public class MyGdxGame extends Game {

	public static int WIDTH;
	public static int HEIGHT;
	public SpriteBatch batch;
	public AssetManager assets;


	@Override
	public void create() {
		batch=new SpriteBatch();
		WIDTH= Gdx.graphics.getWidth();
		HEIGHT= Gdx.graphics.getHeight();
		assets=new AssetManager();
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void dispose() {
		assets.dispose();
		super.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	@Override
	public Screen getScreen() {
		return super.getScreen();
	}
}
