package com.antoine.contracts;

import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.awt.image.BufferedImage;

/**
 * <p>Représente un personnage.</p>
 *
 * @author Antoine
 */
public interface IEntity {

    /**
     * <p>Le personnage bouge à gauche.</p>
     */
    void movesLeft();

    /**
     * <p>Le personnage bouge à droite.</p>
     */
    void movesRight();

    /**
     * <p>Le personnage bouge vers le haut.</p>
     */
    void movesUp();

    /**
     * <p>Le personnage bouge vers le bas.</p>
     */
    void movesDown();

    /**
     * <p>Effectue la translation suite à la simulation d'un déplacement.</p>
     * Translation différée pour gérer la cohérence des données en multi-threading.
     * Cette méthode est appelée par la boucle principale du jeu.
     * @param map la carte pour gestion des collisions.
     * @return le vecteur de déplacement du personnage.
     */
    Coordinates memorizeMoves(IMap map);

    /**
     * <p>Fourni l'image du personnage pour affichage.</p>
     * @return l'image.
     */
    BufferedImage getImage();

    /**
     * @return la coordonnée sur l'axe des abscisses sur la carte.
     */
    int getX();

    /**
     * @return la coordonnée sur l'axe des ordonnées sur la carte.
     */
    int getY();

    /**
     * <p>Appelle à la suite d'un mouvement.</p>
     * Après une touche relachée ou à la suite d'un calcule pour un personnage non-joueur.
     */
    void movesReleased();

    /**
     * @return la longueur du personnage.
     */
    int getWidth();

    /**
     * @return la heuteur du personnage.
     */
    int getHeight();

    /**
     * <p>Effecture une translation rapide sur une carte, hors animation.</p>
     * @param verector le vecteur de déplacement.
     */
    void translate(Coordinates verector);

    /**
     * <p>Enregistre les coordonnées de départ du personnage.</p>
     * @param position la position de départ.
     */
    void setPosition(Coordinates position);

    /**
     * @return la position et les dimentions du personnage sous forme de rectangle.
     */
    Rectangle getPosition();

    void translateTo(Coordinates startBossPosition);
}
