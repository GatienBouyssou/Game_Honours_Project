package com.honours.AI;

import java.util.List;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.player.PlayerType;

public abstract class AIManager {
	protected static final int EXPECTED_NUMBER_OF_PLAYERS = 2;

	public static final int REFRESH_RATE = 5;	
	protected World world;
	protected Array<Player> inGamePlayers= new Array<>(EXPECTED_NUMBER_OF_PLAYERS);	
	protected Timer timer;
	
	public AIManager(World world, List<Team> allTheTeams) {
		this.world = world;
		timer = new Timer();
		for (Team team : allTheTeams) {
			List<Integer> playersAlive = team.getListOfPlayersAlive();
			for (Integer playerId : playersAlive) {
				Player player = team.getPlayerById(playerId);
				inGamePlayers.add(player);
				if (player.getPlayerType() == PlayerType.AI) {
					buildMonitorePlayer(player);
				}
			}
		}
	}
	
	protected abstract void buildMonitorePlayer(Player player);
	
	public void run(float runInterval) {
		timer.scheduleTask(new Task() {
			@Override
			public void run() {
				try {
					update();
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		}, 0, runInterval);
	}
	
	public abstract void update();
	
	public abstract void resolvePendingActions();

	public void playerDies(Player player) {
		inGamePlayers.removeValue(player, true);
	}

	public void restart() {
		timer.stop();
		timer.start();
	}
	
	public void dispose() {
		timer.stop();
	}
}
