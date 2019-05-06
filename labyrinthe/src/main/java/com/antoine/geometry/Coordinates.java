package com.antoine.geometry;


/**
 * <b>Représente les coordonnées d'un point dans un plan</b>
 *
 * @author antoine
 */
public class Coordinates {
	
	private int x, y;


	//===============   Constructeurs   ===================
	public Coordinates(int x, int y) {
		this.x= x;
		this.y= y;
	}

	public Coordinates(){}

	//=====================================================

	public int getX() {return x;}
	public int getY() {return y;}
	
	public void setCoordinates(Coordinates newPosition) {
		this.x= newPosition.getX();
		this.y= newPosition.getY();
	}

	public void setX(String x){
		this.x= Integer.parseInt(x);
	}

	public void setY(String y){
		this.y= Integer.parseInt(y);
	}


	/**
	 * <p>Opère une translation</p>
	 * @param vector de déplacement
	 */
	public void translate(Coordinates vector) {
		this.x += vector.getX();
		this.y += vector.getY();
	}
	
	public void setCoordinates(int x, int y) {this.x= x; this.y= y;}

	/**
	 * <p>Teste de valeur</p>
	 * @return true si point à l'origine ou vecteur null
	 */
	public boolean isZero() {
		return x != 0 && y != 0;
	}


	/**
	 * <p>Test de valeur des coordonnées</p>
	 * @param obj dont les coordonnées sont à tester
	 * @return true si même coordonnées, false sinon
	 */
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof Coordinates)){
			return false;
		}
		Coordinates coordinates= (Coordinates) obj;
		return ((x == coordinates.getX()) && (y == coordinates.getY()));
	}

	/**
	 * @see Object#clone()
	 * @return une nouvelle occurence de même coordonnée
	 */
	@Override
	public Coordinates clone() {
		return new Coordinates(x, y);
	}

}
