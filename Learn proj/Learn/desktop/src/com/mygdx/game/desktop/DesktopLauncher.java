package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "SlotRiderZ";
		cfg.width = 1280;
		cfg.height = 720;
		//cfg.addIcon("img/logo1.png", Files.FileType.Internal);
		//cfg.fullscreen=true;
		//cfg.vSyncEnabled=false;
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
