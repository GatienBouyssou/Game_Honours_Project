package com.honours.AI.unsofisticatedAI;

import java.util.List;

import com.badlogic.gdx.physics.box2d.World;
import com.honours.AI.AIManager;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;

public class DoNothingAI extends AIManager {

	public DoNothingAI(World world, List<Team> allTheTeams) {
		super(world, allTheTeams);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildMonitorePlayer(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resolvePendingActions() {
		// TODO Auto-generated method stub

	}

}
