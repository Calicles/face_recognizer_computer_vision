package com.antoine.manager.niveau;

import java.awt.Dimension;

import com.antoine.contracts.*;
import com.antoine.modele.Game;
import com.antoine.manager.musique.Jukebox;

/**
 * <b>Représente le contrôleur entre la vue et le modèle.</b>
 *
 * @author Antoine
 */
public class LevelManager implements Presentateur {

	/**Le modèle*/
	IJeu game;

	public LevelManager()  {
		game= new Game();
	}


	/**
	 *
	 * @return La longueur de la map en cours.
	 */
	public int getMapWidth() {return game.getMapWidth();}

	/**
	 *
	 * @return La hauteur de la map en cours.
	 */
	public int getMapHeight() {return game.getMapHeight();}


	/**
	 * <p>Change le niveau en cours.</p>
	 * @see IJeu#switchLeveApple()
	 */
	public void switchLeveApple() {
		game.switchLeveApple();
	}

	/**
	 * @see #switchLeveApple()
	 */
	public void switchLevelRarity() {
		game.switchLevelRarity();
	}

	/**
	 * @see #switchLeveApple()
	 */
	public void switchLevelRainbow() {
		game.switchLevelRainbow();
	}

	/**
	 * @see Presentateur#playerMovesLeft()
	 */
	public void playerMovesLeft(){
		game.playerMovesLeft();
	}

	/**
	 * @see Presentateur#playerMovesRight()
	 */
	public void playerMovesRight(){
		game.playerMovesRight();
	}

	/**
	 * @see Presentateur#playerMovesUp()
	 */
	public void playerMovesUp(){
		game.playerMovesUp();
	}

	/**
	 * @see Presentateur#playerMovesDown()
	 */
	public void playerMovesDown(){
		game.playerMovesDown();
	}

	/**
	 * @see Presentateur#playerMovesReleased()
	 */
	public void playerMovesReleased(){
		game.playerMovesReleased();
	}

	/**
	 *
	 * @return les dimensions de la carte en cours d'utilisation.
	 */
	public Dimension getDimension() {
		return game.getDimension();
	}

	/**
	 * @see Presentateur#AddListener(LevelListener)
	 * @param listener l'écoutant à ajouter.
	 */
	public void AddListener(LevelListener listener) {
		game.addListener(listener);
	}

	/**
	 * @see IJeu#removeListener(LevelListener)
	 * @param listener l'écoutant à effacer.
	 */
	public void removeListener(LevelListener listener) {
		game.removeListener(listener);
	}

	/**
	 * @see Presentateur#accept(IAfficheur)
	 * @param visiteur le visteur qui interprête la structure.
	 */
	@Override
	public void accept(IAfficheur visiteur) {
		game.accept(visiteur);
	}

	@Override
	public Jukebox getJukebox() {
		return game.getJukebox();
	}
}
