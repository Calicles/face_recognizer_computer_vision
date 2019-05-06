package com.antoine.transfert_strategy;

import com.antoine.contracts.IMap;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;

/**
 * <b>Défini les règle de gestion des déplacements de personnage (joueur ou non joueur)</b>
 * Utilisé dans le pattern Strategy.
 */
public abstract class AbstractTransfer {

	/**Le vecteur de déplacement possible au personnage*/
	protected Coordinates vector;

	/**Le vecteur dont le joueur va être translaté, après réajustage de calcule de collision*/
	protected int xDirection, yDirection;


	public void setVector(Coordinates vector){
		this.vector= vector;
	}

	/**
	 * <p>Donne une valeur nul au vecteur de déplacement.</p>
	 */
	public void released() {
		xDirection= 0;
		yDirection= 0;
	}

	/**
	 * <p>Enregistre la translation.</p>
	 * @param position la position du personnage.
	 * @param map la carte, utilisé pour collision avec zone inaccessible.
	 * @return le vecteur ajusté.
	 */
	abstract Coordinates memorizeMoves(Rectangle position, IMap map);


	/**
	 * <p>Ajuste le vecteur de daplacement par projection avec une zone inaccessible.</p>
	 * @param position la position du personnage.
	 * @param map la carte de jeu.
	 */
	protected void adaptVectors(Rectangle position, IMap map) {
		
		//On cherche si vecteur null pas de calcul
		if(xDirection != 0 || yDirection !=0) {
			
			//On cherche la direction
			if(xDirection < 0) {
				
				checkLeft(position, map);
				
			}else if(xDirection > 0) {

				checkRight(position, map);
				
			}else if(yDirection < 0) {
				
				checkUp(position, map);
				
			}else if(yDirection > 0) {
				
				checkDown(position, map);
			}
		}
		
	}

	/**
	 * <p>Vérifie si collision possible dans une direction donnée.</p>
	 * @param position la position du joueur.
	 * @param map la carte de jeu.
	 */
	private void checkLeft(Rectangle position, IMap map) {
		
		Tile tile;

		int playerX= position.getBeginX();
		
		if(playerX <= map.getTile_width()){
			
			if(playerX < vector.getX()) {
				xDirection= 0 - playerX;
			}
			
		}else if((tile= checkLeftTiles(position, map)) != null) {
			
			if((tile.getX() + map.getTile_width()) - playerX >= -vector.getX())
				xDirection= (tile.getX() + map.getTile_width() + 1) - playerX;
		}
		yDirection= 0;
	}

	/**
	 * @see #checkLeft(Rectangle, IMap)
	 */
	private void checkRight(Rectangle position, IMap map) {
		
		Tile tile;
		int playerEndX= position.getEndX();

		if(playerEndX >= (map.getWidth() - map.getTile_width())){
			if(playerEndX >= (map.getWidth() - vector.getX())) {
				xDirection= map.getWidth() - playerEndX;
			}

		}else if((tile= checkRightTiles(position, map)) != null) {
			if(tile.getX() - position.getEndX() <= vector.getX())
				xDirection= tile.getX() - (position.getEndX() + 1);

		}
		yDirection= 0;
	}

	/**
	 * @see #checkLeft(Rectangle, IMap)
	 */
	private void checkUp(Rectangle position, IMap map) {
		
		Tile tile;
		int playerY= position.getBeginY();
		
		if(playerY <= map.getTile_height()){
			
			if(playerY < vector.getY()) {
				yDirection= 0 - playerY;
			}
			
		}else if((tile= checkOnUpTiles(position, map)) != null){
			
			if((tile.getY() + map.getTile_height()) - playerY >= -4)
				yDirection= (tile.getY() + map.getTile_height()+1) - playerY;
			
		}
		xDirection= 0;
	}

	/**
	 * @see #checkLeft(Rectangle, IMap)
	 */
	private void checkDown(Rectangle position, IMap map) {
		
		Tile tile;
		int playerEndY= position.getEndY();

		if(playerEndY >= (map.getHeight() - map.getTile_height())) {
				if(playerEndY > (map.getHeight() - vector.getY())) {
					yDirection= map.getHeight() - position.getEndY();
				}
		}else if((tile= checkOnDownTiles(position, map)) != null) {
			if(tile.getY() - playerEndY <= vector.getY())
				yDirection= tile.getY() - (playerEndY+1);
		}
		xDirection= 0;
	}

	/**
	 * <p>Vérifie si une tuile est inaccessible dans une direction</p>
	 * L'examen prend en compte la taille du personnage, il peut en effet être sur plusieurs tuiles.
	 * @param position la position et dimensions du personnage.
	 * @param map la carte de jeu.
	 * @return une tuile si elle bloque le passage, null sinon.
	 */
	protected Tile checkOnDownTiles(Rectangle position, IMap map) {

		int x, y, endX;
		
		x= position.getBeginX() / map.getTile_width();
		endX= position.getEndX() / map.getTile_width();
		y= position.getEndY() / map.getTile_height() +1;

		return map.isSolidTileOnRoad(new Rectangle(new Coordinates(x, y), endX, y));
	}

	/**
	 * @see #checkOnDownTiles(Rectangle, IMap)
	 */
	protected Tile checkOnUpTiles(Rectangle position, IMap map) {

		int x, y, endX;
		
		x= position.getBeginX() / map.getTile_width();
		endX= position.getEndX() / map.getTile_width();
		y= position.getBeginY() / map.getTile_height() -1;
		
		return map.isSolidTileOnRoad(new Rectangle(new Coordinates(x, y), endX, y));
	}

	/**
	 * @see #checkOnDownTiles(Rectangle, IMap)
	 */
	protected Tile checkRightTiles(Rectangle position, IMap map) {
		
		int x, y, endY;
		
		x= position.getEndX() / map.getTile_width() +1;
		y= position.getBeginY() / map.getTile_height();
		endY= position.getEndY() / map.getTile_height();

		return map.isSolidTileOnRoad(new Rectangle(new Coordinates(x, y), x, endY));
	}

	/**
	 * @see #checkOnDownTiles(Rectangle, IMap)
	 */
	protected Tile checkLeftTiles(Rectangle position, IMap map) {
		
		int x, y, endY;
		
		x= position.getBeginX() / map.getTile_width() -1;
		y= position.getBeginY() / map.getTile_height();
		endY= position.getEndY() / map.getTile_height();
		
		return map.isSolidTileOnRoad(new Rectangle(new Coordinates(x, y), x, endY));
	}
	
}
