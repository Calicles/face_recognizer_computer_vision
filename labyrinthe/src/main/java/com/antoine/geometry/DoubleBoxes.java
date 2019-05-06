package com.antoine.geometry;


/**
 * <b>Classe contenant deux Rectangles</b>
 * <p>Permet à l'écran de se déplacer en suivant les mouvements du joueur (scrolling)</p>
 *
 * @author antoine
 *
 */
public class DoubleBoxes {

	/**
	 * simule la forme et la position de l'écran d'affichage
	 */
	private Rectangle screen;

	/**
	 * zone qui déclenche le déplacement de l'écran si atteinte par le joueur
	 */
	private Rectangle scrollBox;


	public DoubleBoxes(Rectangle screen, Rectangle scrollBox) {
		this.screen= screen;
		this.scrollBox= scrollBox;
	}

	//================   getters pour l'écran   ==================
	public int getScreenBeginX() {return screen.getBeginX();}
	public int getScreenEndX() {return screen.getEndX();}
	public int getScreenBeginY() {return screen.getBeginY();}
	public int getScreenEndY() {return screen.getEndY();}
	public int getScreenWidth(){return screen.getWidth();}
	public int getScreenHeight(){return screen.getHeight();}


	//================   Test pour scrolling   ===================
	public boolean isPlayerOnTopScroll(int yPosition) {
		return yPosition <= scrollBox.getBeginY();
	}
	public boolean isPlayerOnBottomScroll(int yPosition) {
		return yPosition >= scrollBox.getEndY();
	}
	public boolean isPlayerOnLeftScroll(int xPosition) {
		return xPosition <= scrollBox.getBeginX();
	}
	public boolean isPlayerOnRightScroll(int xPosition) {
		return xPosition >= scrollBox.getEndX();
	}

	public void scroll(int xVector, int yVector) {
		screen.translate(xVector, yVector);
		scrollBox.translate(xVector, yVector);
	}

	//=============================================================

    public Rectangle getScreen() {
		return this.screen;
    }

	public int getScrollBeginX() {
		return scrollBox.getBeginX();
	}

	public int getScrollBeginY() {
		return scrollBox.getBeginY();
	}

	public Rectangle getScroll() {
		return scrollBox;
	}
}
