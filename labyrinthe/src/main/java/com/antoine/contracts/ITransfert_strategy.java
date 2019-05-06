package com.antoine.contracts;

import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.util.Stack;

/**
 * <b>Représente les fonctionnalité de déplacement d'un personnage.</b>
 *
 * @author Antoine
 */
public interface ITransfert_strategy {

    /**
     * <p>Action effectué après un mouvement.</p>
     * @see IEntity#movesReleased()
     */
    void released();

    /**
     * <p>Se déplace sur la gauche.</p>
     * @see IEntity#movesLeft()
     */
    void movesLeft();

    /**
     * <p>Se déplace sur la droite.</p>
     * @see IEntity#movesRight()
     */
    void movesRight();

    /**
     * <p>Se déplace vers le haut.</p>
     * @see IEntity#movesUp()
     */
    void movesUp();

    /**
     * <p>Se déplace vers le bas.</p>
     * @see IEntity#movesDown()
     */
    void movesDown();

    /**
     * <p>Calcule le mouvement en cours.</p>
     * Vérifie les collisions et ajuste le vecteur de déplacement dans ce cas.
     * @param position de l'entité qui se déplace.
     * @param map la carte.
     * @return le vecteur de déplacement, ajusté, qui pourra être enregistré par l'entité.
     */
    Coordinates memorizeMoves(Rectangle position, IMap map);

    /**
     * @see #memorizeMoves(Rectangle, IMap)
     * @return le vecteur de déplacement ajusté.
     */
    Coordinates memorizeMoves();

    /**
     * <p>Réveille le Thread qui doit calculer une trajectoire.</p>
     */
    void think();

    void setAttributes(Rectangle ownPosition, Rectangle palyer1, IMap map);

    /**
     * <p>Démarrage initiale du Thread de calcule des trajectoires.</p>
     */
    void startThinking();

    /**
     * <p>Retourne un chemin à suivre</p>
     * Pour débug.
     * @return le chemin.
     */
    //TODO Remove after Test
    Stack<Coordinates> getPath();
}
