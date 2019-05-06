package com.antoine.contracts;

import com.antoine.events.LevelChangeEvent;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.awt.*;
import java.util.List;
import java.util.Stack;

/**
 * <b>Représente un niveau de jeu avec tous ses composants.</b>
 *
 * @author Antoine
 */
public interface ILevel {

    /**
     * <p>Préviens le niveau qu'il est celui en cours d'utilisation.</p>
     */
    void selected();

    /**
     * @see IJeu#getMapWidth()
     * @return la longueur de la carte.
     */
    int getMapWidth();

    /**
     * @see IJeu#getMapHeight()
     * @return la hauteur de la carte.
     */
    int getMapHeight();

    /**
     * <p>Préviens le niveau qu'il n'est plus celui en cours d'utilisation.</p>
     */
    void deselected();

    /**
     * <p>Vérifie si le niveau est en cours d'utilisation.</p>
     * @return true si le niveau est celui séléctionné, false sinon.
     */
    boolean isSelected();

    /**
     * <p>Vérifie si le niveau est terminé.</p>
     * @return true si le joueur a fini, false sinon.
     */
    boolean isRunning();

    /**
     * @see IJeu#playerMovesLeft()
     */
    void playerMovesLeft();

    /**
     * @see IJeu#playerMovesRight()
     */
    void playerMovesRight();

    /**
     * @see IJeu#playerMovesUp()
     */
    void playerMovesUp();

    /**
     * @see #playerMovesDown()
     */
    void playerMovesDown();

    /**
     * @see #playerMovesReleased()
     */
    void playerMovesReleased();

    /**
     * @return les dimensions de la carte/
     */
    Dimension getDimension();

    /**
     * @see IJeu#getScreenX()
     * @return coordonnée de l'écran.
     */
    int getScreenX();

    /**
     * @see IJeu#getScreenHeight()
     * @return coordonnée de l'écran.
     */
    int getScreenY();

    /**
     * @see IJeu#getScreenWidth()
     * @return la longueur de l'acran.
     */
    int getScreenWidth();

    /**
     * @see IJeu#getScreenHeight()
     * @return la hauteur de l'écran.
     */
    int getScreenHeight();

    /**
     * @see IJeu#getPlayerX()
     * @return coordonnée du joueur.
     */
    int getPlayerX();

    /**
     * @see IJeu#getPlayerY()
     * @return coordonnée du joueur.
     */
    int getPlayerY();

    /**
     * @return le carte du niveau.
     */
    IMap getMap();

    /**
     * @return le joueur.
     */
    IEntity getPlayer();

    /**
     * @return l'image de fin de niveau.
     */
    String getEndImageUrl();

    /**
     * @return l'écran de jeu.
     */
    Rectangle getScreen();

    /**
     * @see IJeu#addListener(LevelListener)
     * @param listeners ajoute un écoutant.
     */
    void setListeners(List<LevelListener> listeners);

    /**
     * <p>Démarre le Thread du niveau s'il en a un.</p>
     */
    void start();

    /**
     * @return le boss du niveau s'il en a un.
     */
    IEntity getBoss();

    /**
     * <p>Pour un niveau qui a un Thread</p>
     * Gère lui même ses évènements.
     * @param event l'event à gérer.
     */
    void setEvent(LevelChangeEvent event);

    /**
     * @return l'id du niveau/
     */
    int getId();

    /**
     * @return Le path du pnj, pour débug.
     */
    //TODO Remove after Test
    Stack<Coordinates> getPath();
}
