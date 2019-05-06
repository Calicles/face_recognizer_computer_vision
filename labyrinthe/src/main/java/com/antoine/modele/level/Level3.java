package com.antoine.modele.level;

import com.antoine.contracts.*;
import com.antoine.events.LevelChangeEvent;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.DoubleBoxes;
import com.antoine.geometry.Rectangle;

import java.util.List;
import java.util.Stack;

/**
 * <b>Type de niveau qui gère le "scrolling".</b>
 * La caméra doit suivre les déplacements du personnage.
 * Les dimensions de la carte dépasse celle de l'écran.
 * Les zones sont chargés que si le joueur se déplace vers celles-ci,
 * créant un effet de déroulement.
 *
 * @author Antoine
 */
public class Level3 extends AbstractLevel implements ILevel {

	/**Double rectangle qui gère les dimensions de l'écran et la zone limite qui entraîne le déroulement de la carte*/
	protected DoubleBoxes boxes;


	public Level3(){
		super();
	}

	@Override
	public void setMap(IMap map){
		super.setMap(map);
		initBoxes();
	}

	/**
	 * <p>Initialise les dimensions et les positions de l'écran et de la "scrollBox".</p>
	 */
	protected void initBoxes() {
		Rectangle screen= new Rectangle(0, 20*tile_width, 20*tile_height,
				40* tile_height);
		Rectangle scrollBox= new Rectangle(5*tile_width, 15*tile_width,
				25*tile_height, 35*tile_height);
		this.boxes= new DoubleBoxes(screen, scrollBox);
	}

	/**
	 * @see IJeu#playerMovesUp()
	 * gère également le déroulé de la carte si le joueur atteint la zone de "scroll".
	 * Le déroulé de la carte est synchronisé sur le déplacement du joueur.
	 */
	 @Override
	 public void playerMovesUp() {
		Coordinates vector;

		checkRunning();
		player.movesUp();
		vector= player.memorizeMoves(map);

		scrollUp(vector);
	 }

	/**
	 * @see #playerMovesUp()
	 */
	@Override
	public void playerMovesDown() {
		Coordinates vector;

		checkRunning();
		player.movesDown();
		vector= player.memorizeMoves(map);

		scrollDown(vector);
	}

	/**
	 * @see #playerMovesUp()
	 */
	@Override
	public void playerMovesLeft() {
		Coordinates vector;

		checkRunning();
		player.movesLeft();
		vector= player.memorizeMoves(map);

		scrollLeft(vector);
	}

	/**
	 * @see #playerMovesUp()
	 */
	@Override
	public void playerMovesRight() {
		Coordinates vector;

		checkRunning();
		player.movesRight();
		vector= player.memorizeMoves(map);

		scrollRight(vector);
	}

	/**
	 * <p>Déroule la carte.</p>
	 * @param vector le vecteur dont la carte sera déroulée.
	 */
	protected void scrollUp(Coordinates vector){
		 if(!screenOnTop() &&
				 boxes.isPlayerOnTopScroll(player.getY()+ vector.getY()))
			 boxes.scroll(0, vector.getY() );
	 }

	/**
	 * @see #scrollUp(Coordinates)
	 */
	 protected void scrollDown(Coordinates vector){
		 if(!screenOnBottom() &&
				 boxes.isPlayerOnBottomScroll(player.getY()+
						 player.getHeight() + vector.getY()))
			 boxes.scroll(0, vector.getY());
	 }

	/**
	 * @see #scrollUp(Coordinates)
	 */
	 protected void scrollLeft(Coordinates vector){
		 if(!screenOnLeft() &&
				 boxes.isPlayerOnLeftScroll(player.getX() + vector.getX()))
			 boxes.scroll(vector.getX(), 0);

	 }

	/**
	 * @see #scrollUp(Coordinates)
	 */
	 protected void scrollRight(Coordinates vector){
		 if(!screenOnRight() &&
				 boxes.isPlayerOnRightScroll(player.getX() + player.getWidth() + vector.getX()))
			 boxes.scroll(vector.getX(), 0);
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

	/**
	 * <p>Calcule si l'écran à atteint la bordure de la map, devant alors stopper le scrolling.</p>
	 * @return true si bord de la map atteint, false sinon.
	 */
	 private boolean screenOnRight() {
		 return boxes.getScreenEndX() >= mapSize.getEndX();
	 }

	/**
	 * @see #screenOnRight()
	 */
	private boolean screenOnLeft() {
		 return boxes.getScreenBeginX() <= 0;
	}

	/**
	 * @see #screenOnRight()
	 */
	private boolean screenOnBottom() {
		return boxes.getScreenEndY() >= mapSize.getEndY();
	}

	/**
	 * @see #screenOnRight()
	 */
	private boolean screenOnTop() {
		return boxes.getScreenBeginY() <= 0;
	}

	public int getScreenX() {
		return boxes.getScreenBeginX();
	}

	public int getScreenY() {
		return boxes.getScreenBeginY();
	}

	public int getScreenWidth() {
		return boxes.getScreenWidth();
	}

	public int getScreenHeight() {
		return boxes.getScreenHeight();
	}

	public int getPlayerX() {
		return player.getX();
	}

	public int getPlayerY() {
		return player.getY();
	}

	@Override
	public Rectangle getScreen() {
		return boxes.getScreen();
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

}
