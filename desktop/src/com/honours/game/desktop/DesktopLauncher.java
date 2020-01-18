package com.honours.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.honours.game.HonoursGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.pauseWhenMinimized = true;
        config.title = "Game of the year";
        config.width = 1800;
        config.height = 1000;
        new LwjglApplication(new HonoursGame(), config);
	}
}
