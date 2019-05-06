package com.antoine.geometry;


/**
 * <b>Représente une tuile de décors</b>
 * L'ensemble de tuile mis bout à bout forme la carte
 *
 * @author antoine
 */
public class Tile {

	/**Représente l'état de la tuile, en dessous la tuile est traversable*/
	private static final int SOLID= 6;

	/**La tuile portant ce numéro dans le champs tile_num est une sortie*/
	private static final int EXIT= -1;

	/**Dimension de la tuile*/
	private static int width, height;

	/**Le numéro lié à l'image devant être affichée, ainsi qu'à l'état de la tuile (solide, sortie...)*/
	private int tile_num;

	/**Coordonnées dans la carte de la tuile*/
	Coordinates coordinates;


	public Tile(int tile_num, int x, int y)
	{
		this.tile_num= tile_num;
		coordinates = new Coordinates(x, y);
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public int getTile_num() {return tile_num;}
	public int getX() {return coordinates.getX();}
	public int getY() {return coordinates.getY();}

	/**
	 * <p>Test la solidité</p>
	 * @return true si tuile solide (intraversable), false sinon
	 */
	public boolean isSolid() {return tile_num >= SOLID;}


	/**
	 * <p>Test si la tuile est une sortie</p>
	 * @return true si son numéro correspond à EXIT, false sinon
	 */
	public boolean isExit() {return tile_num == EXIT;}


	/**
	 * <p>Test si la tuile contient cette coordonée.</p>
	 * @param point la coordonnée à tester
	 * @return true si le point est contenu dans le rectangle ou égal à une bordure, false sinon
	 */
    public boolean contains(Coordinates point) {

		return Rectangle.isInBox(this.toRectangle(), point);
    }

	/**
	 * @return Le rectangle au dimension de l'occurence
	 */
	public Rectangle toRectangle(){
		return new Rectangle(coordinates.getX(), coordinates.getX() + width,
				coordinates.getY(),  coordinates.getY() + height);
	}

	/**
	 * @return les coordonnées de l'occurence
	 */
	public Coordinates toCoordinates() {
		return coordinates.clone();
	}

	/**
	 * @see Object#clone()
	 * @return une nouvelle occurence avec les champs copiés
	 */
	@Override
	public Object clone() {
		return new Tile(this.tile_num, this.coordinates.getX(), this.coordinates.getY());
	}

	//====================   Static   ==================

	public static void setWidth(int width){
		Tile.width = width;
	}

	public static void setHeight(int height){
		Tile.height = height;
	}

	public static int getSolidNum(){return SOLID;}

}
