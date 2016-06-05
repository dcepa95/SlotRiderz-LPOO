package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.*;

public class MyGdxGame extends Game {

	public static int WIDTH;
	public static int HEIGHT;
	public SpriteBatch batch;


	@Override
	public void create() {
		batch=new SpriteBatch();
		WIDTH= Gdx.graphics.getWidth();
		HEIGHT= Gdx.graphics.getHeight();
		setScreen(new MenuCarScreen(this));
	}
}
