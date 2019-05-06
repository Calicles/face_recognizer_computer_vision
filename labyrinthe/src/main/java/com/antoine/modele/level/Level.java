package com.antoine.modele.level;

import com.antoine.contracts.IEntity;
import com.antoine.contracts.ILevel;
import com.antoine.contracts.LevelListener;
import com.antoine.events.LevelChangeEvent;
import com.antoine.geometry.Coordinates;

import java.util.List;
import java.util.Stack;

/**
 * <b>Représente un niveau de abse.</b>
 * Le déroulement de ce type de niveau s'appui sur un Thread externe pour faire tourner la boucle logique de jeu.
 * La taille de la carte ne doit pas dépassé celle de l'écran.
 *
 * @author Antoine
 */
public class Level extends AbstractLevel implements ILevel {

	/**Si le niveau est celui en cours*/
	private boolean selected;

	public Level(){
		super();
	}


	public boolean isSelected() {return selected;}
	public void selected() {selected= true;}
	public void deselected() {selected= false;}
	
	@Override
	public int getScreenX() {
		return 0;
	}

	@Override
	public int getScreenY() {
		return 0;
	}

	@Override
	public int getScreenWidth() {
		return 0;
	}

	@Override
	public int getScreenHeight() {
		return 0;
	}

	@Override
	public int getPlayerX() {
		return player.getX();
	}

	@Override
	public int getPlayerY() {
		return player.getY();
	}

	@Override
	public void setListeners(List<LevelListener> listeners) {

	}

	@Override
	public void start() {

	}

	@Override
	public IEntity getBoss() {
		return null;
	}

	@Override
	public void setEvent(LevelChangeEvent event) {

	}


	//TODO Remove after test
	@Override
	public Stack<Coordinates> getPath() {
		return null;
	}

}
