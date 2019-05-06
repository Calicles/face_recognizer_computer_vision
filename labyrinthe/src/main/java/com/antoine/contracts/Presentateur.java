package com.antoine.contracts;

import com.antoine.manager.musique.Jukebox;

import java.awt.*;

/**
 * <b>Représente une forme de Contrôleur</b>
 * Relit les évènement intervenur sur la vue vers le modèle.
 *
 * @author Antoine
 */
public interface Presentateur {

    /**
     * <p>Flèche gauche activée.</p>
     */
    void playerMovesLeft();

    /**
     * <p>Flèche droite activée</p>
     */
    void playerMovesRight();

    /**
     * <p>Flèche bas activée</p>
     */
    void playerMovesUp();

    /**
     * <p>Flèche haut activée.</p>
     */
    void playerMovesDown();

    /**
     * @return les dimensions du modèle.
     */
    Dimension getDimension();

    /**
     * <p>Ajoute un écoutant pour être prévenu des changements du modèle.</p>
     * @param listener l'écoutant à ajouter.
     */
    void AddListener(LevelListener listener);

    /**
     * <p>Change le niveau en cours</p>
     */
    void switchLeveApple();

    /**
     * <p>Change le niveau en cours.</p>
     */
    void switchLevelRarity();

    /**
     * <p>Change le niveau en cours.</p>
     */
    void switchLevelRainbow();

    /**
     * <p>Touche relâchée.</p>
     */
    void playerMovesReleased();

    /**
     * @return la hauteur de la carte.
     */
    int getMapHeight();

    /**
     * <p>Accepte un visiteur pour le guider vers le modèle.</p>
     * @param afficheur
     */
    void accept(IAfficheur afficheur);

    /**
     * <p>FOurnit le gestionnaire de son.</p>
     * @return le gestionnaire de son.
     */
    Jukebox getJukebox();
}
