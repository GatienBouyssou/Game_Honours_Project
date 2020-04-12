package com.honours.AI.unsofisticatedAI;

import java.util.List;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.honours.AI.AIManager;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;

public class SimpleAI extends AIManager {
	private Array<Agent> agents;
	
	public SimpleAI(World world, List<Team> allTheTeams) {
		super(world, allTheTeams);
		agents.shrink();
	}
	
	@Override
	public void update() {
		for (int i = 0; i < agents.size; i++) {
			agents.get(i).update(inGamePlayers);
		}
	}
	
	@Override
	protected void buildMonitorePlayer(Player player) {
		if (agents == null) agents = new Array<Agent>();
		agents.add(new Agent(player));
	}
	
	@Override
	public void resolvePendingActions() {
		for (int i = 0; i < agents.size; i++) {
			agents.get(i).resolveAction();
		}
	}
	
	@Override
	public void playerDies(Player player) {
		super.playerDies(player);
		for (int i = 0; i < agents.size; i++) {
			if (agents.get(i).getMonitoredPlayer() == player) {
				agents.removeIndex(i);
				return;
			}
		}
	}
}
