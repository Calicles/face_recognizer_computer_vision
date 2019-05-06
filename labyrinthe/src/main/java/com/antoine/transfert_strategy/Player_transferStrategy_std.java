package com.antoine.transfert_strategy;

import com.antoine.contracts.IJeu;
import com.antoine.contracts.IMap;
import com.antoine.contracts.ITransfert_strategy;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.util.Stack;

/**
 * <b>Calsse qui représente la capacité de déplacement d'un joueur.</b>
 *
 * @author Antoine
 */
public class Player_transferStrategy_std extends AbstractTransfer implements ITransfert_strategy {

	public Player_transferStrategy_std(){

	}

	/**
	 * @see IJeu#playerMovesLeft()
	 */
	public void movesLeft() {
		xDirection= -vector.getX();
	}

	/**
	 * @see IJeu#playerMovesRight()
	 */
	public void movesRight() {
		xDirection= vector.getX();
	}

	/**
	 * @see IJeu#playerMovesUp()
	 */
	public void movesUp() {
		yDirection= -vector.getY();
	}

	/**
	 * @see IJeu#playerMovesDown()
	 */
	public void movesDown() {
		yDirection= vector.getY();
	}

	/**
	 * <p>Ajuste le vecteur de déplacement à la présence d'un obstacle par projection.</p>
	 * @param position de l'entité qui se déplace.
	 * @param map la carte.
	 * @return le vecteur après calcule.
	 */
	@Override
	public Coordinates memorizeMoves(Rectangle position, IMap map) {
		
		adaptVectors(position, map);
		return new Coordinates(xDirection, yDirection);
	}

	@Override
	public Coordinates memorizeMoves() {
		return null;
	}

	@Override
	public void think() {

	}

	@Override
	public void setAttributes(Rectangle ownPosition, Rectangle palyer1, IMap map) {

	}

	@Override
	public void startThinking() {

	}

	//TODO Remove after test
	@Override
	public Stack<Coordinates> getPath() {
		return null;
	}

}
