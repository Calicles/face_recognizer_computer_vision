package com.antoine.entity;

import com.antoine.contracts.IEnnemi;
import com.antoine.contracts.IMap;
import com.antoine.contracts.ITransfert_strategy;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;
import com.antoine.transfert_strategy.IA_transfertStrategy_std;

import java.util.Stack;

/**
 * <b>Classe qui représente un ennemi</b>
 * <p>possède une pseudo Intelligence Artificielle
 * pour choix de déplacement</p>
 *
 * @author antoine
 */
public class Boss extends AbstractCharacter implements IEnnemi {


	public Boss() {
		super();
	}

	/**
	 * @see #movesDown()
	 */
	@Override
	public void movesLeft() {
		deplacement.movesLeft();
		memorizeMoves();
	}

	/**
	 * @see #movesDown()
	 */
	@Override
	public void movesRight() {
		deplacement.movesRight();
		memorizeMoves();
	}

	/**
	 * @see #movesDown()
	 */
	@Override
	public void movesUp() {
		deplacement.movesUp();
		memorizeMoves();
	}

	/**
	 * <p>Donne une valeur de trajectoire au vecteur de déplacement et enregistre la translation.</p>
	 * Utilisé pour pour animer le personnage non joueur à l'écran.
	 */
	@Override
	public void movesDown() {
		deplacement.movesDown();
		memorizeMoves();
	}

	/**
	 * <p>Non utilisée.</p>
	 * @param map la carte pour gestion des collisions.
	 * @return le vecteur.
	 */
	@Override
	public Coordinates memorizeMoves(IMap map) {
		return null;
	}

	/**
	 * <p>Donnes les valeurs 0;0 au vecteur de déplacement.</p>
	 */
	@Override
	public void movesReleased() {
		deplacement.released();
	}

	/**
	 * <p>non utilisée.</p>
	 * @param verector le vecteur de déplacement.
	 */
	@Override
	public void translate(Coordinates verector) {

	}

	/**
	 * <p>Relance le cycle pour calculer une nouvelle trajectoire.</p>
	 * @see ITransfert_strategy#think()
	 */
	@Override
	public void think() {
		this.deplacement.think();
	}

	/**
	 * <p>Calcule d'eventuel collisions et ajuste les vecteurs, enregistre le déplacement.</p>
	 *
	 */
	@Override
	public void memorizeMoves() {
		Coordinates vectors= deplacement.memorizeMoves();
		this.changeSprite(vectors);
		position.translate(vectors);
	}

	/**
	 * <p>Enregistre les informations pour les faire utiliser par la transfert_strategy.</p>
	 * @see com.antoine.transfert_strategy.IA_transfertStrategy_std#setAttributes(Rectangle, Rectangle, IMap)
	 * @param palyer1 les positions et les dimensions du joueur sous forme de rectangle.
	 * @param map la carte pour détecter les collisions.
	 */
	@Override
	public void setAttributes(Rectangle palyer1, IMap map) {
		deplacement.setAttributes(position, palyer1, map);
	}

	/**
	 * @see IA_transfertStrategy_std#startThinking()
	 */
	@Override
	public void startThinking() {
		deplacement.startThinking();
	}


	/**
	 * @see IA_transfertStrategy_std#getPath()
	 * @return
	 */
	//TODO Remove after Test
	@Override
	public Stack<Coordinates> getPath() {
		return deplacement.getPath();
	}
}
