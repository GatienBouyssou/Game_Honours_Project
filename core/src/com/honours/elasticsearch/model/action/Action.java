package com.honours.elasticsearch.model.action;

import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;

public class Action {
	private ActionDirection movingDirection;

	private Integer[] spellIds;
	private ActionType actionType;
	private ActionLength spellLength;
	private ActionDirection spellDirection;
		
	public Action(ActionDirection movingDirection, Array<Integer> spellIds, ActionType actionType,
			ActionLength spellLength, ActionDirection spellDirection) {
		super();
		this.movingDirection = movingDirection;
		try {			
			this.spellIds = spellIds.toArray(Integer.class);
		} catch (Exception e) {
			
		}
		this.actionType = actionType;
		this.spellLength = spellLength;
		this.spellDirection = spellDirection;
	}

	public static Action generateAction(Player player) {
		return new Action(getRandomEnum(ActionDirection.class), getRandomSpellsToCast(player), getRandomEnum(ActionType.class), 
				getRandomEnum(ActionLength.class), getRandomEnum(ActionDirection.class));
	}

	private static <T extends Enum<T>> T getRandomEnum(Class<T> enumType) {
		T[] enumConstants = enumType.getEnumConstants();
		int random = getRandomBellow(enumConstants.length);
		return enumConstants[random];
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

	public ActionDirection getMoveDestination() {
		return movingDirection;
	}

	public void setMoveDestination(ActionDirection movingDirection) {
		this.movingDirection = movingDirection;
	}

	public Integer[] getSpellId() {
		return spellIds;
	}

	public void setSpellId(Integer[] spellIds) {
		this.spellIds = spellIds;
	}
	
	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public ActionLength getSpellLength() {
		return spellLength;
	}

	public void setSpellLength(ActionLength spellLength) {
		this.spellLength = spellLength;
	}

	public ActionDirection getSpellDirection() {
		return spellDirection;
	}

	public void setSpellDirection(ActionDirection spellDirection) {
		this.spellDirection = spellDirection;
	}

	public String toJson(Gson gson) {
		return gson.toJson(this);
	}
}
