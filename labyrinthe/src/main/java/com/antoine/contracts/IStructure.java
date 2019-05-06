package com.antoine.contracts;

import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.util.Stack;

/**
 * <b>Représente une structure à visiter</b>
 * Utilisé dans cadre du pattern "Visiteur".
 *
 * @author Antoine
 */
public interface IStructure {

    /**
     * <p>Accepte le visiteur qui doit étudier cette structure.</p>
     * @param visiteur le visiteur.
     */
    void accept(IAfficheur visiteur);

    /**
     * @return la carte de la structure.
     */
    IMap getMap();

    /**
     * @return le joueur de cette structure.
     */
    IEntity getPlayer();

    /**
     * @return vrai si la structure est en cours d'utilisation, false sinon.
     */
    boolean isRunning();

    /**
     * @return l'image de fin.
     */
    String getEndImageUrl();

    /**
     * @return l'écran de jeu.
     */
    Rectangle getScreen();

    /**
     * @return le personnage non joueur de la structure.
     */
    IEntity getBoss();

    /**
     * @return la longueur de la carte.
     */
    int getMapHeight();

    /**
     * @return la hauteur de la carte.
     */
    int getMapWidth();

    /**
     * @return le path du personnage non joueur, pour environnement de débugage.
     */
    Stack<Coordinates> getPath();
}
