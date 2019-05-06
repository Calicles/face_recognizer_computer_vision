package com.antoine.entity;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.antoine.contracts.ITransfert_strategy;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;
import com.antoine.services.Character_reader;

/**
 *<b>classe abstraite de personnage</b>
 *
 * @author antoine
 */
public abstract class AbstractCharacter extends AbstractImage {

	/**contient les sprites par clé de direction.*/
	protected HashMap<Integer,BufferedImage[]> animation;

	/**coordonnées du personnage*/
	protected Rectangle position;

	/**
	 * pattern strategy.
	 * ce champ gère les dalculs de vecteur
	 * pour les déplacements
	 * et annule le mouvement en cas de collision.
	 */
	protected ITransfert_strategy deplacement;

	/**
	 * clé du champ animation.
	 * 0 bas
	 * 1 gauche
	 * 2 droite
	 * 3 haut
	 */
	protected int direction;

	/**
	 * indice des tableaux contenu dans animation.
	 * produit l'animation
	 * incrémenté si touche maintenu
	 * puis retour à 0.
	 */
	protected int animIndex;

	/**Sert à ralentir l'annimation*/
	protected int tempo;



	protected AbstractCharacter(){super();}

	/**
	 * <p>Doit être appelé après setAnimation()
	 * lors de l'injection</p>
	 * Les dimensions du rectangle sont fourni par les dimensions de l'image de la superclasse.
	 * @param position les coordonnées de départ du personnage
	 */
	public void setPosition(Coordinates position) {
		this.position= new Rectangle(position, position.getX() + getWidth(), position.getY() + getHeight());
	}

	public void setAnimation(String animationSet){
		animation= Character_reader.readCharactereAnimation(animationSet);
		image= animation.get(0)[0];
	}

	public void setDeplacement(ITransfert_strategy deplacement_strategy){
		this.deplacement= deplacement_strategy;
	}
	
	public Rectangle getPosition() {return position;}

	public int getX() {return position.getBeginX();}

	public int getEndX() { return position.getEndX();}

	public int getY() {return position.getBeginY();}

	public int getEndY() {return position.getEndY();}

	public Rectangle toRectangle(){
		return position;
	}


	public void translateTo(Coordinates newPosition) {
		position.setCoordinates(newPosition);
	}

	/**
	 * Change la valeur de la clé de animation selon
	 * la valeur du vecteur de déplacement.
	 * @param vector le vecteur de déplacement
	 */
	protected void changeSprite(Coordinates vector) {
		if (vector.getX() != 0 || vector.getY() != 0) {
			if (vector.getX() != 0) {
				if (vector.getX() < 0) {
					direction = 1;
				} else {
					direction = 2;
				}
			} else {
				if (vector.getY() < 0) {
					direction = 3;
				} else {
					direction = 0;
				}
			}
			updateIndex();
			image = animation.get(direction)[animIndex];
		}
	}

	/**
	 * <p>change le sprite séléctionné
	 * pour simuler l'animation
	 * lors de l'affichage</p>
	 * Utilise le coefficient tempo pour ralentir l'animation.
	 */
	public void updateIndex() {
		tempo++;
		if(tempo % 4 == 0) {
			animIndex++;
			if(tempo >= 100)
				tempo= 0;
		}
		if(animIndex >= animation.get(0).length)
			animIndex= 0;
	}

}
