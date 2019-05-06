package com.antoine.contracts;

import com.antoine.geometry.Rectangle;
import com.antoine.manager.musique.Jukebox;

import java.awt.*;

/**
 * <p>Représente le modèle, le jeu au complet.</p>
 *
 * @author Antoine
 */
public interface IJeu {

    /**
     *
     * @return la longueur de la carte.
     */
    int getMapWidth();

    /**
     *
     * @return La hauteur de la carte.
     */
    int getMapHeight();

    /**
     * <p>Change le niveau en cours sur le niveau Apple.</p>
     */
    void switchLeveApple();

    /**
     * <p>Charge le niveau en cours sur le niveu de Rarity.</p>
     */
    void switchLevelRarity();

    /**
     * <p>Charge le niveau en cours sur le niveau de Rainbow.</p>
     */
    void switchLevelRainbow();

    /**
     * @see IEntity#movesLeft()
     */
    void playerMovesLeft();

    /**
     * @see IEntity#movesRight()
     */
    void playerMovesRight();

    /**
     * @see IEntity#movesUp()
     */
    void playerMovesUp();

    /**
     * @see IEntity#movesDown()
     */
    void playerMovesDown();

    /**
     * @see IEntity#movesReleased()
     */
    void playerMovesReleased();

    /**
     * @return La dimension du niveau.
     */
    Dimension getDimension();

    /**
     * <p>Fait un ajout à la liste des écoutants</p>
     * @param listener l'écoutant à ajouté et à prévenir en ca de changement du modèle.
     */
    void addListener(LevelListener listener);

    /**
     * <p>Supprime un écoutant de la liste.</p>
     * @param listener l'écoutant à supprimer.
     */
    void removeListener(LevelListener listener);

    /**
     * @return la coordonnée X du rectangle qui simule l'écran du niveau.
     */
    int getScreenX();

    /**
     * @return la coordonnée Y du rectangle qui simule l'écran du niveau.
     */
    int getScreenY();

    /**
     * @return la longueur du rectangle qui simule l'écran sur la carte.
     */
    int getScreenWidth();

    /**
     * @return la hauteur du rectangle qui simule l'écran sur la carte.
     */
    int getScreenHeight();

    /**
     * @see IEntity#getX()
     * @return la coordonnée X du joueur sur la carte.
     */
    int getPlayerX();

    /**
     * @see IEntity#getY()
     * @return la coordonnée Y du joueur sur la carte.
     */
    int getPlayerY();

    /**
     * @see IAfficheur#visit(IStructure)
     * @param visiteur le visiteur qui visite le niveau en cours.
     */
    void accept(IAfficheur visiteur);

    /**
     * @return la carte du niveau en cours
     */
    IMap getMap();

    /**
     * @return le joueur du niveau en cours.
     */
    IEntity getPlayer();

    /**
     * @return true si le niveau n'est pas terminé, false sinon.
     */
    boolean isRunning();

    /**
     * @return l'image de fin du niveau en cours.
     */
    String getEndImageUrl();

    /**
     * @return le rectangle qui simule l'écran sur la carte.
     */
    Rectangle getScreen();

    /**
     * @return le personnage qui incarne le méchant.
     */
    IEntity getBoss();

    /**
     * @return le gestionnaire de son.
     */
    Jukebox getJukebox();
}
