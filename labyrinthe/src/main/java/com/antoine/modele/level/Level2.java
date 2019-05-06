package com.antoine.modele.level;

import com.antoine.contracts.IEntity;
import com.antoine.contracts.IJeu;
import com.antoine.contracts.ILevel;
import com.antoine.contracts.LevelListener;
import com.antoine.events.LevelChangeEvent;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.util.List;
import java.util.Stack;

/**
 * <b>Représente un niveau dont les dimensions de la carte peuvent dépasser les dimensions de l'éran.</b>
 * Le reste de la carte n'est alors pas affiché.
 * L'affichage se fait si le joueur atteint une bordure limite de la carte (bord de l'écran).
 * Ce type de carte ne prend en charge les nouvelle zone que dans le sens de la hauteur (haut, bas).
 *
 * @author Antoine
 */
public class Level2 extends AbstractLevel implements ILevel {

	/**Simule l'écran et ses positions sur la carte*/
	private Rectangle screen;

	/**
	 * <p>Enregistre les dimensions de l'écran.</p>
	 */
	public Level2(){
		super();
		screen= new Rectangle(0, 20, 20, 40);

	}

	/**
	 * @see IJeu#playerMovesUp()
	 * Gère en plus le fait que le joueur peut atteindre une zone limite de l'écran
	 * et provoquer l'affichage
	 * d'une nouvelle zone de jeu.
	 */
	@Override
	public void playerMovesUp()
	{
		if(!isOnTop(-8) && isOnTopScreen())
			loadMap(0, -20, -player.getHeight());
		super.playerMovesUp();
	}

	/**
	 * @see #playerMovesUp()
	 */
	@Override
	public void playerMovesDown()
	{
		if(!isOnBottom(4) && isOnBottomScreen())
			loadMap(0, 20, player.getHeight());
		super.playerMovesDown();
	}

	/**
	 * <p>Calule si le joueur à atteint une bordure de l'écran.</p>
	 * @return true si le joueur touche le bord, false sinon.
	 */
	private boolean isOnBottomScreen() {
		return (playerScreenPositionY() + player.getHeight()) > 
			(20 * tile_height - 4);
	}

	/**
	 * @see #isOnTopScreen()
	 * @return true si le joueur touche le bord, false sinon
	 */
	private boolean isOnTopScreen() {
		return playerScreenPositionY() <= 4;
	}

	/**
	 * <p>Change les coordonnées de l'écran pour produire l'affichage de la nouvelle zone de jeu.</p>
	 * @param xVector valeur i du vecteur.
	 * @param yVector valeur j du vecteur
	 * @param playerVector vecteur de déplacement du joueur pour que celui-ci "suive" le chargement
	 *                     de la nouvelle zone.
	 */
	private void loadMap(int xVector, int yVector, int playerVector) {
		screen.translate(xVector, yVector);
		player.translate(new Coordinates(0, playerVector));
	}

	/**
	 * <p>Calcule la coordonnée Y du joueur sur l'écran.</p>
	 * @return coordonnée Y traduite en coordonnée écran.
	 */
	private int playerScreenPositionY() {
		int coef=0;
		coef= player.getY() / (tile_height * 20);
		return player.getY() - (coef * (tile_height * 20));
	}

	@Override
	public Rectangle getScreen(){
		return screen;
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


	@Override
	public void setListeners(List<LevelListener> listeners) {

	}

	@Override
	public void start() {

	}

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
		return 0;
	}

	@Override
	public int getPlayerY() {
		return 0;
	}

	@Override
	public void selected() {

	}

	@Override
	public void deselected() {

	}

	@Override
	public boolean isSelected() {
		return false;
	}
}
