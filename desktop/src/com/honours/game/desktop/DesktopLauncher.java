package com.honours.game.desktop;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.honours.game.HonoursGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        int width = Lwjgl3ApplicationConfiguration.getDisplayMode().width;
        int height = Lwjgl3ApplicationConfiguration.getDisplayMode().height-60;
        
        config.setResizable(false);
    	config.setWindowedMode(width, height);
    	config.setDecorated(true);
    	config.useVsync(true);		
    	config.setTitle("Game of the Year");
    	try {
    		new Lwjgl3Application(new HonoursGame(), config);
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	
	}
}
