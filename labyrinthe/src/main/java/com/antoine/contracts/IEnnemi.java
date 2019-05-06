package com.antoine.contracts;

import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.util.Stack;

/**
 * <b>Représente un ennemi</b>
 *
 * @author Antoine
 */
public interface IEnnemi extends IEntity {

    /**
     * <p>Redémarre le processus de reflexion dans le recherche de chemin.</p>
     * Le Thread qui implémente ce processus est réveillé par cette appelle.
     */
    void think();

    /**
     * <p>Translate les coordonnées du personnage par le vecteur de déplacement.</p>
     */
    void memorizeMoves();

    /**
     * <p>Affecte les attributs nécessaires aux déplacement.</p>
     * @param player le joueur dont la position peut être détecté.
     * @param map la carte pour détecter les collisions.
     */
    void setAttributes(Rectangle player, IMap map);

    /**
     * <p>Démarrage initiale du Thread qui gère le choix de direction du déplacement.</p>
     */
    void startThinking();

    /**
     * @return le path sous forme de pile de coordonnée.
     */
    //TODO Remove after Test
    Stack<Coordinates> getPath();
}
