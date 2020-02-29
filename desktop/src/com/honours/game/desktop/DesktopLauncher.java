package com.honours.game.desktop;

<<<<<<< HEAD
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
=======
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
>>>>>>> fixing gradle
import com.honours.game.HonoursGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
<<<<<<< HEAD
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.pauseWhenMinimized = true;
        config.title = "Game of the year";
        config.width = 1800;
        config.height = 1000;
        new LwjglApplication(new HonoursGame(), config);
=======
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        int width = Lwjgl3ApplicationConfiguration.getDisplayMode().width - 200;
        int height = Lwjgl3ApplicationConfiguration.getDisplayMode().height - 100;
        config.setResizable(false);
    	config.setWindowedMode(width, height);
    	config.setDecorated(true);
    	config.useVsync(true);		
    	config.setTitle("RavTech");
        
		new Lwjgl3Application(new HonoursGame(), config);
>>>>>>> fixing gradle
	}
}
