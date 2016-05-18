package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Screens.*;

public class MyGdxGame extends Game {



	@Override
	public void create() {
		setScreen(new FollowWaypoints());
	}
}
