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
        config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width - 200;
        config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height - 100;
        new LwjglApplication(new HonoursGame(), config);
	}
}
