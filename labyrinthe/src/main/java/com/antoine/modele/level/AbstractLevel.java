package com.antoine.modele.level;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.antoine.contracts.*;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;

/**
 * <b>Classe abstraite qui représente le cadre d'un niveau de jeu.</b>
 *
 * @author Antoine
 */
public abstract class AbstractLevel implements IStructure {

	/**La carte de jeu*/
	protected IMap map;

	/**Le joueur*/
	protected IEntity player;

	/**Les dimensions de la map sous forme de rectangle*/
	protected Rectangle mapSize;

	/**Les dimensions et coordonnées du rectange qui simule la sortie d'un labyrinthe*/
	private Rectangle exit;

	/**Path de l'image de fin, chargé à la sortie du joueur pour economiser la mémoire*/
	protected String endImageUrl;

	/**Etat, niveau terminé ou non*/
	protected boolean running;

	/**Dimensions des tuiles*/
	protected int tile_width, tile_height;

	/**id du niveau*/
	protected int id;

	/**Utilisé pour attribuer les Ids*/
	private static int numberOfInstance = 0;


	protected AbstractLevel(){
		id = numberOfInstance;
		numberOfInstance++;
		running= true;
	}

	public void setMap(IMap map){
		this.map= map;
		tile_width= map.getTile_width();
		tile_height= map.getTile_height();
		exit= tileToRectangle(map.findExit());
		setMapSize();
	}

	public int getId(){
		return id;
	}

	@Override
	public IEntity getPlayer(){return player;}

	@Override
	public String getEndImageUrl(){return endImageUrl;}

	@Override
	public IMap getMap(){return map;}

	public void setEndImageUrl(String endImageUrl){
		this.endImageUrl= endImageUrl;
	}

	public void setPlayer(IEntity player){
		this.player= player;
	}

	public int getMapWidth(){return mapSize.getWidth();}

	public int getMapHeight(){return mapSize.getHeight();}

	public Dimension getDimension() {return mapSize.getDimension();}

	/**
	 *
	 * @return true si niveau n'est pas terminé, false sinon.
	 */
	public boolean isRunning() {return running;}

	@Override
	public Rectangle getScreen(){ return null;}

	private void setMapSize() {
		int[] tab= map.getDimension();
		mapSize= new Rectangle(tab[0], tab[1]);
	}

	/**
	 * @see IJeu#playerMovesReleased()
	 */
	public void playerMovesReleased(){
		player.movesReleased();
	}

	/**
	 * @see IJeu#playerMovesLeft()
	 */
	public void playerMovesLeft() {
		checkRunning();
		player.movesLeft();

		player.memorizeMoves(map);
	}

	/**
	 * @see IJeu#playerMovesRight()
	 */
	public void playerMovesRight() {
		checkRunning();
		player.movesRight();

		player.memorizeMoves(map);
	}

	/**
	 * @see IJeu#playerMovesUp()
	 */
	public void playerMovesUp() {
		checkRunning();
		player.movesUp();

		player.memorizeMoves(map);

	}

	/**
	 * @see IJeu#playerMovesDown()
	 */
	public void playerMovesDown() {
		checkRunning();
		player.movesDown();
		player.memorizeMoves(map);

	}
	

	public boolean isOnLeft(int toTest) {
		return mapSize.isOnLeft(player.getX() + toTest);
	}

	public boolean isOnRight(int toTest) {
		int x= player.getX() + player.getWidth();
		return mapSize.isOnRight(x + toTest);
	}

	/**
	 * <p>Teste si le joueur atteint la brdure de l'écran, par projection (ajout du vecteur)</p>
	 * @param toTest le vecteur.
	 * @return true si le joueur atteint la bordure, false sinon.
	 */
	protected boolean isOnTop(int toTest) {
		return mapSize.isOnTop(player.getY() + toTest);
	}

	/**
	 * @see #isOnTop(int)
	 * @param toTest le vecteur
	 * @return true si le joueur atteint la bordure bas de la carte.
	 */
	protected boolean isOnBottom(int toTest) {
		int y= player.getY() + player.getHeight();
		return mapSize.isOnBottom(y + toTest);
	}

	/**
	 * <p>Calcule si un rectangle est inclus dans un autre.</p>
	 * @param entity le rectangle qui représente un personnage.
	 * @param rec le rectangle dans lequel entity peut être inclus.
	 * @return true si le rectangle est inclus, false sinon.
	 */
	private boolean isIEntityInBox(Rectangle entity, Rectangle rec) {
		return Rectangle.isInBox(rec, entity);
	}

	/**
	 * <p>Calcule si le joueur est entrée dans la sortie.</p>
	 * @return true si le joueur est entré dans l'espace de sortie, false sinon.
	 */
	private boolean isPlayerOnExit() {
		return isIEntityInBox(player.getPosition(), exit);
	}

	/**
	 * <p>Transforme la tuile qui symbolise la sortie en rectangle pouvant contenir le joueur.</p>
	 * @param tile la tuile marquée comme sortie.
	 * @return un nouveau rectangle de dimensions de deux tuiles.
	 */
	private Rectangle tileToRectangle(Tile tile) {
		int endX= tile.getX() + tile_width * 2;
		int endY= tile.getY() + tile_height * 2;
		return new Rectangle(tile.getX(), endX, tile.getY(), endY);

	}

	/**
	 * @see IJeu#accept(IAfficheur)
	 * @param visiteur le visiteur.
	 */
	public void accept(IAfficheur visiteur){
		visiteur.visit(this);
	}

	/**
	 * <p>Vérifie si le joueur est totalement dans l'espace de sortie.</p>
	 * Place le flag runnin à false si le joueur est sortie.
	 */
	protected void checkRunning(){
		if(isPlayerOnExit()){
			running= false;
		}
	}
}
