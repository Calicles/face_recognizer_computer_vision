package com.antoine.structure_donnee.pathfinding;

import com.antoine.contracts.IHeuristic;
import com.antoine.contracts.IPathfinding;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Pythagore;
import com.antoine.geometry.Tile;

import java.util.Comparator;

/**
 * <b>Représente une heuristique utilisée dans un algorithme de recherche</b>
 *
 * @author Antoine
 */
public class HeuristicBestDistance implements IHeuristic<Integer> {

    /**
     * <p>Calcule la distance.</p>
     * @see IHeuristic#getHeuristicCost(com.antoine.contracts.IPathfinding)
     * @param pathfinder pour récupérer le noeud entrain d'être examiné ainsi que le but à atteindre
     * @return La distance au carré séparant ce point du but (utilise un entier pour rapidité)
     */
    @Override
    public Integer getHeuristicCost(IPathfinding pathfinder) {

        Node_heuristic<Tile, Integer> node1 = pathfinder.getAdjacentNode();
        Node_heuristic<Tile, Integer> goal = pathfinder.getGoalNode();

        Coordinates node1coor = node1.getItem().toCoordinates();
        Coordinates node2coor = goal.getItem().toCoordinates();
        return Pythagore.calculDistanceInSquarre(node1coor, node2coor);
    }

    /**
     * <p>Utilisé pour initialiser le comparateur de la structure de donnée de l'algorithme (la liste des éléments non
     *  encore examinée, mais suceptible de l'être).</p>
     *  Le fait d'avoir une structure de donnée triée accélère significativement l'algorithme.
     * @see IHeuristic#getComparator()
     * @return la plus petite distance
     */
    @Override
    public Comparator<Node_heuristic<Tile, Integer>> getComparator() {
        return Comparator.comparingInt(o -> {
            if (o.getHeuristic() == null)
                return -1;
            else
                return o.getHeuristic();
        });
    }
}
