package com.antoine.contracts;

import com.antoine.geometry.Tile;
import com.antoine.structure_donnee.pathfinding.Node_heuristic;

import java.util.Comparator;

/**
 * <b>Interface une heuristique de recherche</b>
 * @param <T> le type de retour utilisé dans les calcules.
 *
 * @author Antoine
 */
public interface IHeuristic<T> {

    /**
     * <p>Retourne une valeur pour calculer quel sera le meilleur item suivant à choisir dans l'algorithme</p>
     * @param pathfinder pour opérer le calcul
     * @return la valeur calculée
     */
    T getHeuristicCost(IPathfinding pathfinder);


    /**
     * <p>Sert à initialisée une structure de donnée pour amélioré la rapidité de recherche</p>
     * La structure de donnée doit renvoyé l'item potentiellement le meilleur.
     * @return Un comparateur de la valeur des heuristique de chaque item.
     */
    Comparator<Node_heuristic<Tile, T>> getComparator();

}
