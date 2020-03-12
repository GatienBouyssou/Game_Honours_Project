package com.honours.elasticsearch.model.action;

import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;

public class Action {
	private ActionMove movingDirection;
	private Array<Integer> spellIds;
	
	public Action(ActionMove movingDirection, Array<Integer> spellIds) {
		super();
		this.movingDirection = movingDirection;
		this.spellIds = spellIds;
	}
	
	public static Action generateAction(Player player) {
		return new Action(getRandomActionMove(), getRandomSpellsToCast(player));
	}

	private static ActionMove getRandomActionMove() {
		ActionMove[] possibleMoves = ActionMove.values();
		int random = getRandomBellow(possibleMoves.length);
		return possibleMoves[random];
	}
	
	private static Array<Integer> getRandomSpellsToCast(Player player) {
		Array<Integer> spellsToCast = getSpellsIndeces(player);	
		int randomSize = getRandomBellow(spellsToCast.size);		
		while (spellsToCast.size > randomSize) {
			spellsToCast.removeIndex(getRandomBellow(spellsToCast.size));			
		}
		return spellsToCast;
	}

	private static Array<Integer> getSpellsIndeces(Player player) {
		Array<Integer> spellsToCast = new Array<Integer>();
		Array<Spell> spellsAvailable = player.getListOfSpells();
		for (int i = 0; i < spellsAvailable.size; i++) {
			spellsToCast.add(i);
		}
		return spellsToCast;
	}
	
	private static int getRandomBellow(int nbrOfSpellsAvailable) {
		return new Random().nextInt(nbrOfSpellsAvailable);
	}

	public ActionMove getMoveDestination() {
		return movingDirection;
	}

	public void setMoveDestination(ActionMove movingDirection) {
		this.movingDirection = movingDirection;
	}

	public Array<Integer> getSpellId() {
		return spellIds;
	}

	public void setSpellId(Array<Integer> spellIds) {
		this.spellIds = spellIds;
	}
}
