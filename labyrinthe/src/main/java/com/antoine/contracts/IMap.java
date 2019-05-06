package com.antoine.contracts;

import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

/**
 * <b>Représente une carte d'un niveau composée de tuile.</b>
 *
 * @author Antoine
 */
public interface IMap {

    /**
     * @return la longueur d'une tuile.
     */
    int getTile_width();

    /**
     * @return la hauteur d'une tuile.
     */
    int getTile_height();

    /**
     * @return la tuile qui représente la sortie du niveau.
     */
    Tile findExit();

    /**
     * @return les dimensions de la map.
     */
    int[] getDimension();

    /**
     * @return la longueur de la carte.
     */
    int getWidth();

    /**
     * @return la hauteur de la carte.
     */
    int getHeight();

    /**
     * @return la carte sous-forme de matrice de tuiles.
     */
    Tile[][] getMap();

    /**
     * @return le HashMap associant le numéro d'une tuile à son image.
     */
    HashMap<Integer, BufferedImage> getTileSet();

    /**
     * <p>Utilisée pour calculer les collisions et affiner les vecteur de déplacement par projection.</p>
     * @param rectangle les indices des tuiles adjacentes au personnage, selon sa direction. la longeur du rectangle
     *                  est le nombre de tuiles à vérifier en longueur. La heuteur du rectangle est le nombre de tuile
     *                  en hauteur pouvant entrer en contact avec le joueur (si le joueur est à cheval sur plusieurs tuiles).
     * @return une tuile si une collision peut survenir, null si la voie est libre.
     */
    Tile isSolidTileOnRoad(Rectangle rectangle);

    /**
     * <p>Retourne une sous-partie de la carte</p>
     * Utilisé surtout pour calcul de trajectoire en pathfinding.
     * @param surface la surface à étudié pour fournir un path de trajectoire.
     * @return les tuiles contenu dans la surface sous forme de liste.
     */
    List<Tile> getSubMap(Rectangle surface);

    /**
     * @see #getSubMap(Rectangle)
     * @param surface la surface à étudier.
     * @return les tuiles contenu dans la surface sous forme de matrice.
     */
    Tile[][] getsubMapInArray(Rectangle surface);

    /**
     * <p>Trouve les indices d'une tuile sur la matrice qui contient une position sur la map</p>
     * @param position la position dont une tuile peut contenir les coordonnées.
     * @return les indices de la tuile qui contient cette position.
     */
    Coordinates getCoorinatesInTile(Coordinates position);

    /**
     * <p>Verifie si la tuile présentes à ces indices sur la matrice est une tuile solide.</p>
     * @param i la ligne de la tuile.
     * @param j la colonne de la tuile.
     * @return true si la tuile est solide, false sinon.
     */
    boolean isSolideTile(int i, int j);

    /**
     * <p>Retourne le nombre de tuile que contient la carte dans le sens de la longueur.</p>
     * @return le nombre de tuile.
     */
    int getWidthInTile();

    /**
     * <p>Retourn le nombre de tuile que contient la carte dans le sens de la hauteur.</p>
     * @return le nomre de tuile en hauteur.
     */
    int getHeightInTile();
}
