package com.antoine.contracts;

import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;
import com.antoine.structure_donnee.pathfinding.Node_heuristic;

import java.util.Stack;

/**
 * <b>Implémente un algorithme de recherche de chemin (plus court...)</b>
 *
 * @author Antoine
 */
public interface IPathfinding<T> {

    /**
     * <p>Lance un algorithme de recherche dans un graphe, ici la carte.</p>
     * @param mover l'entité qui doit trouver un chemin.
     * @param goal la position du but à atteindre sur la carte.
     * @param map la carte.
     * @return une suite de coordonnées contenu dans une pile (LIFO) qui doit représenter le
     * chemin à suivre.
     */
    Stack<Coordinates> createPath(Rectangle mover, Coordinates goal, IMap map);

    /**
     * @return le chemin à suivre sous forme de coordonnées empilées.
     */
    //TODO Remove after test
    Stack<Coordinates> getPath();

    /**
     * <p>En cas d'algorithme utilisant une heuristique</p>
     * Les tuiles adjacentes à celle en cours auront une valeur étudiée selon l'heuristique utilisée.
     * @return le noeud adjacent au noeud étudié dans l'algorithme principal.
     */
    Node_heuristic<Tile, T> getAdjacentNode();

    /**
     * @return le noeud qui contient les coordonnées du but à atteindre.
     */
    Node_heuristic<Tile, T> getGoalNode();

    /**
     * Fourni une heuristique à l'instance.
     * @param heuristic l'heuristique qui sera utilisée dans l'algorithme principal.
     */
    void setHeuristic(IHeuristic<T> heuristic);
}
