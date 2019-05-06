package com.antoine.modele.map;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;
import com.antoine.services.Map_reader;

/**
 * <b>Représente le cadre pour une carte de jeu d'un niveau.</b>
 *
 * @author Antoine
 */
public abstract class AbstractTileMap {

	/**Associe un entier à une image, l'image étant chargée une seul fois en mémoire,
	 *  elle peut être reproduite par la vue à volonté*/
	protected HashMap<Integer, BufferedImage> tileSet;

	/**La carte sous forme de matrice de tuiles*/
	protected Tile[][] map;

	/**Dimensions d'une tuiles*/
	protected int tile_width, tile_height;


	public AbstractTileMap(HashMap<Integer, BufferedImage> tileSet, int[][] map)
	{
		this.tileSet= tileSet;
		this.tile_width= tileSet.get(0).getWidth();
		this.tile_height= tileSet.get(0).getHeight();
		initMap(map);
	}

	public AbstractTileMap(){

	}

	public void setTileSet(String fileTileSetUrl){
		tileSet= Map_reader.readTileSet(fileTileSetUrl);
	}

	/**
	 * @param fileMapUrl l'url du fichier qui contient les données de la carte.
	 */
	public void setMap(String fileMapUrl){
		int[][] buffer;
		buffer = Map_reader.readMap(fileMapUrl);
		this.tile_width= tileSet.get(0).getWidth();
		this.tile_height= tileSet.get(0).getHeight();
		Tile.setWidth(tile_width);
		Tile.setHeight(tile_height);
		initMap(buffer);
	}
	
	public int getTile_width() {return tile_width;}

	public int getTile_height() {return tile_height;}

	public int getWidth(){return map[0].length * tile_width;}

	public int getHeight(){ return map.length * tile_height;}

	public Tile[][] getMap(){return map;}

	public HashMap<Integer, BufferedImage> getTileSet() {return tileSet;}

	/**
	 *
	 * @return le nombre de colonne de la matrice.
	 */
	public int getWidthInTile() {
		return map[0].length;
	}

	/**
	 *
	 * @return le nombre de ligne de la matrice.
	 */
	public int getHeightInTile() {
		return map.length;
	}

	public int[] getDimension() {
		int[] tab= new int[2];
		tab[0]= tileSet.get(0).getWidth() * map[0].length;
		tab[1]= tileSet.get(0).getHeight() * map.length;
		return tab;
	}

	/**
	 * <p>Rempli la matrice et génère les instances des tuiles qui la compose.</p>
	 * @param map une matrice d'entier (les valeurs des clés du tileset).
	 */
	private void initMap(int[][] map)
	{
		this.map= new Tile[map.length][map[0].length];
		int tile_num;
		for(int i=0;i<map.length;i++)
		{
			for(int j=0;j<map[0].length;j++)
			{
				tile_num= map[i][j];
				this.map[i][j]= new Tile(tile_num, j * tile_width, 
						i * tile_height);
			}
		}
		
	}

	/**
	 * <p>Vérifie si une tuile solide barre la route d'une entié.</p>
	 * @param board le rectangle servant de surface à inspectée sous forme d'indice de matrice.
	 *              les coordonnées du rectancle sont les indices dans la matrice de départ et
	 *              sa heuteur la borne de i et la longueur la borne pour j.
	 * @return une tuile qui elle barre la route, null sinon.
	 */
	public Tile isSolidTileOnRoad(Rectangle board) {
		for(int i= board.getBeginY(); i<=board.getEndY();i++) {
			for(int j= board.getBeginX(); j<= board.getEndX();j++) {
				if(map[i][j].isSolid()) {
					return map[i][j];
				}
			}
		}
		return null;
	}

	/**<p>Trouve la tuile marquée comme sortie.</p>
	 * @return la tuile marquée.
	 */
	public Tile findExit() {
		for(int i= 0; i< map.length;i++) {
			for(int j= 0; j< map[0].length;j++) {
				if(map[i][j].isExit()) {
					return map[i][j];
				}
			}
		}
		return null;
	}
	
	public Tile findTile(int row, int col) {
		return map[row][col];
	}

	

}
