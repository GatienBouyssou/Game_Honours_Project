package com.honours.AI.CBRSytem;

import java.util.List;

import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.manager.Team;
import com.honours.game.sprites.Player;

public class CBRSytem {
	private World world;
	private List<Team> teams;
	
	public CBRSytem(World world, List<Team> teams) {
		super();
		this.world = world;
		this.teams = teams;
	}
	
	public void query(Player player) {
		
	}
	
}
