package com.honours.AI;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.physics.box2d.World;
import com.honours.AI.CBRSytem.CBRSytem;
import com.honours.game.manager.Team;

public class AIManager {
	private World world;
	private List<Team> teams;
	private CBRSytem cbrSytem; 
	
	public AIManager(World world, List<Team> allTheTeams) {
		this.world = world;
		this.teams = allTheTeams;
		this.cbrSytem = new CBRSytem(world, allTheTeams);
	}
	
	public void update() {

	}
}
