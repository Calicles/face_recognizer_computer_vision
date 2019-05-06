package com.antoine.geometry;


import java.awt.*;

/**
 * <b>Classe qui représente un rectangle</b>
 *
 * @author antoine
 *
 */
public class Rectangle {


	/**
	 * les coordonnées et ses dimensions
	 */
	private int X, Y, width, height;


	//===========================  Constructeur  ============================
	public Rectangle(int width, int height) {
		X= 0; Y= 0;
		this.width= width;
		this.height= height;
	}

	public Rectangle(int X, int endX, int Y, int endY) {
		this.X= X;
		this.Y= Y;
		this.width= endX - X;
		this.height= endY - Y;
	}

	public Rectangle(Coordinates coordinates, int endX, int endY) {
		this.X= coordinates.getX();
		this.Y= coordinates.getY();
		this.width= endX - X;
		this.height= endY - Y;
	}
	//========================================================================


	//=============== getters Coordonnées Point haut droit   =================
    public int getEndX() {return X + width;}
	public int getEndY() {return Y + height;}

	//=============== idem point haut gauche   ================================
	public int getBeginX() {return X;}
	public int getBeginY() {return Y;}

	//===============   Hauteur et longueur   =================================
	public int getWidth() {return width;}
	public int getHeight() {return height;}


	public Dimension getDimension() {
		return new Dimension(width, height);
	}

	public void translate(int x, int y) {
		X += x;
		Y += y;
	}

	//==============   Tests de Bordures   ===================================
	public boolean isOnLeft(int toTest) {return toTest < X;}
	public boolean isOnRight(int toTest) {return toTest > getEndX();}
	public boolean isOnTop(int toTest) {return toTest < Y;}
	public boolean isOnBottom(int toTest) {return toTest >= getEndY();}

	public static boolean isOver(int x, int position) {return x>position;}
	public static boolean isBefore(int x, int position) {return x<position;}


	/**
	 * <p>Calcule si un rectangle est contenu dans un autre</p>
	 * @param box conteneur
	 * @param position contenu
	 * @return true si les coordonnées de tous les points du contenu sont intégralement
	 * comprises dans l'espace du conteneur, false sinon
	 */
	public static  boolean isInBox(Rectangle box, Rectangle position) {
		return box.getBeginX() <= position.getBeginX() &&
				box.getEndX() >= position.getEndX() &&
				box.getBeginY() <= position.getBeginY() &&
				box.getEndY() >= position.getEndY();
	}

	public static boolean isInBox(Rectangle box, Coordinates point){
		return (point.getX() >= box.getBeginX() &&
				point.getX() <= box.getEndX()) &&
				(point.getY() >= box.getBeginY() &&
						point.getY() <= box.getEndY());
	}

    public void setCoordinates(int x, int y) {
		this.X= x;
		this.Y= y;
    }

    public void setCoordinates(Coordinates position) {
		this.X= position.getX();
		this.Y= position.getY();

    }


	/**
	 * <p>Opère une translation</p>
	 * @param vector de translation
	 */
	public void translate(Coordinates vector) {
		X += vector.getX();
		Y += vector.getY();

    }


	/**
	 * <p>Calcule de distance entre deux figure
	 * utilise leur coordonnées de point centrale</p>
	 * @param position1 rectangle 1
	 * @param position2 rectangle 2
	 * @param radius rayon
	 * @return true si la distance entre les deux figure est inférieure au rayon, false sinon
	 */
	public static boolean isNext(Rectangle position1, Rectangle position2, int radius) {

		return Pythagore.calculDistance(findMiddleCoor(position1), findMiddleCoor(position2)) < radius;

	}

	/**
	 * <p>Calcule de coordonnée</p>
	 * @param position le rectangle
	 * @return les coordonnées du point centrale de la figure (l'origine relatif)
	 */
	public static Coordinates findMiddleCoor(Rectangle position){
		return new Coordinates(findMiddleX(position), findMiddleY(position));
	}

	public static int findMiddleX(Rectangle position){
		return position.getBeginX() + position.getWidth() / 2;
	}

	public static int findMiddleY(Rectangle position){
		return position.getBeginY() + position.getHeight() / 2;
	}

	/**
	 * <p>Détecte une collision entre deux rectangles.</p>
	 * algorithme de l'espace autour du rectangle
	 * @param position1 rectangle 1
	 * @param position2 rectangle 2
	 * @return true si pas d'espace, false sinon
	 */
	public static boolean isTouching(Rectangle position1, Rectangle position2){
		       //le x est plus petit que le point haut droit de figure 2 et son point haut droit plus grand que x figure 2
		return (position1.getBeginX() < position2.getEndX()) && (position1.getEndX() > position2.getBeginX()) &&
				// pareil pour les y, mais par rapport au point bas droit
				(position1.getBeginY() < position2.getEndY()) && (position1.getEndY() > position2.getBeginY());
	}


	/**
	 * <p>Détection de collision par algorithme des axes séparateurs</p>
	 * @param position1 rectangle 1
	 * @param position2 rectangle 2
	 * @return true si la distance entre les coordonnées d'origine des figures est plus petite
	 * que la somme de leur deux demi hauteur (axe y) ou demi longueur (x), false sinon
	 */
	public static boolean isTouching2(Rectangle position1, Rectangle position2){
		Coordinates middlePosition1, middlePosition2;
		int deltaX, deltaY;

		//Prend les coordonnées des points au centre des rectangles
		middlePosition1= findMiddleCoor(position1);
		middlePosition2= findMiddleCoor(position2);

		//Calcule les distances qui sépare chaque points
		deltaX= Math.abs(middlePosition1.getX() - middlePosition2.getX());
		deltaY= Math.abs(middlePosition1.getY() - middlePosition2.getY());

		//Prend en compte la largeur et la hauteur de la figure pour vérifier s'il n y a pas d'espace entre les deux
		//donc on ajoute la moitié de H et de L, car le point est au centre.
		return (deltaX < (position1.getWidth() / 2 + position2.getWidth() / 2)) &&
				(deltaY < (position1.getHeight() / 2 + position2.getHeight() / 2));
    }

	/**
	 * <p>Calcule l'égalité de deux coordonnées</p>
	 * @param obj dont les coordonnées sont à vérifier
	 * @return true si même coordonnées, false sinon
	 */
	public boolean equalsCoordinates(Object obj){
		return new Coordinates(X, Y).equals(obj);
	}

}
