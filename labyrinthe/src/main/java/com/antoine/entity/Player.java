package com.antoine.entity;

import com.antoine.contracts.IEntity;
import com.antoine.contracts.IMap;
import com.antoine.geometry.Coordinates;


/**
 * <b>Classe représentant un joueur</b>
 * <p>Implémente les mouvements</p>
 *
 * @author antoine
 */
public class Player extends AbstractCharacter implements IEntity {


	/**
	 * <p>Donne une valeur au vecteur de déplacement</p>
	 */
	@Override
	public void movesLeft() {deplacement.movesLeft();}

	/**
	 * @see #movesLeft()
	 */
	@Override
	public void movesRight() {deplacement.movesRight();}

	/**
	 * @see #movesLeft()
	 */
	@Override
	public void movesUp() {deplacement.movesUp();}

	/**
	 * @see #movesLeft()
	 */
	 @Override
	public void movesDown() {deplacement.movesDown();}


	/**
	 * <p>Execute la translation selon le vecteur</p>
	 * @param map est utilisée pour la stratégie de déplacement et trouver les obstacles
	 * @return le vecteur après ajustement au collision pour vérifier s'il y a eu mouvement
	 * différent de 0 0.
	 */
	@Override
	public Coordinates memorizeMoves(IMap map) {
		Coordinates vector= deplacement.memorizeMoves(this.position, map);
		
		//Change les coordonnes du perso
		position.translate(vector.getX(), vector.getY());
		
		//change la sprite
		changeSprite(vector);

		return vector;
	}


	/**
	 * <p>donne la référence d'un sprite immobile
	 * au champ image pour affichage</p>
	 */
	private void animationStoped() {
		animIndex= 0;
		image= animation.get(direction)[1];
	}


	/**
	 * <p>replace le vecteur aux valeurs 0 0</p>
	 * @see this.animationStopped pour début
	 */
	public void movesReleased() {
		animationStoped();
		deplacement.released();
	}


	/**
	 * <p>Simule une translation
	 * (pour autre raison que déplacement clavier</p>
	 * @param vector vecteur de la translation
	 */
	@Override
	public void translate(Coordinates vector) {
		position.translate(vector.getX(), vector.getY());
	}


}
