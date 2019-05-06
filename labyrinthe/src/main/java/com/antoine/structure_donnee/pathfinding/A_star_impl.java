package com.antoine.structure_donnee.pathfinding;

import com.antoine.contracts.IHeuristic;
import com.antoine.contracts.IMap;
import com.antoine.contracts.IPathfinding;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Pythagore;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;

import java.util.*;

/**
 * <b>Implémentation de l'algorithme A*.</b>
 * <p>Doit rechercher le plus court chemin dans graphe en utilisant une heuristique
 * comme priorité de recherche.</p>
 *
 * @author Antoine
 */
public class A_star_impl extends AbstractPathfinding_algo implements IPathfinding {

    /**
     * <p>Graphe de recherche.</p>
     */
    private ArrayList<Node_heuristic<Tile, Integer>> graphe;

    /**
     * <p>Liste des tuiles non encore scrutées.</p>
     */
    private TreeSet<Node_heuristic<Tile, Integer>> openList;

    /**
     * <p>Liste des tuiles traitées.</p>
     */
    private ArrayList<Node_heuristic<Tile, Integer>> closedList;

    private Node_heuristic<Tile, Integer> currentNode, goal;

    private int adjacent;



    public A_star_impl() {
        super();
        graphe = new ArrayList<>();

        adjacent = (int) Math.pow(Tile.getWidth(), 2);
    }


    @Override
    public Stack<Coordinates> createPath(Rectangle mover, Coordinates goal, IMap map) {

        super.createRectangle(mover, goal, map);

        Node_heuristic<Tile, Integer> node;

        //Récupère le morceau de la carte selon le rectangle
        List<Tile> subList = map.getSubMap(surface);

        for (Tile t : subList) {
            node = new Node_heuristic<>(t);

            //N'ajoute pas la tuile si elle est solide
            if (!node.getItem().isSolid())
                graphe.add(node);

            //Si la tuile contient les coordonnées de l'entité, elle est placé en départ
            if (t.contains(start)) {
                node.setWeight(0);
                node.setHeuristic(0);
                openList.add(node);

                //Si contient les coordonées du but, la tuile est en arrivée
            } else if (t.contains(goal)) {
                this.goal = node;
            }
        }

        startSearch();


        return path;
    }

    @Override
    public Stack<Coordinates> getPath() {
        return (Stack<Coordinates>) path.clone();
    }

    @Override
    public Node_heuristic<Tile, Integer> getAdjacentNode() {
        return null;
    }

    @Override
    public Node_heuristic<Tile, Integer> getGoalNode() {
        return null;
    }

    @Override
    public void setHeuristic(IHeuristic heuristic) {

    }

    private void startSearch() {

        while (!openList.isEmpty()) {

            currentNode = openList.first();
            
            if (currentNode == goal) {
                createFinalPath();
                return;
            }

            for (Node_heuristic<Tile, Integer> n : graphe) {

                if (getDist(n, currentNode) <=  adjacent) {

                    int weight = currentNode.getWeight() + 1;
                    int heuristic = n.getWeight() + getDist(n, goal);

                    if (!closedList.contains(n)) {

                        if (openList.contains(n) && n.getWeight() > weight) {

                            n.setWeight(weight);

                            n.setHeuristic(heuristic);

                            n.setParentNode(currentNode);


                        }else if (n.getWeight() > weight || n.getWeight() == -1) {

                            n.setWeight(weight);

                            n.setHeuristic(heuristic);

                            if(currentNode.getWeight() != 0)
                                n.setParentNode(currentNode);

                            openList.add(n);
                        }
                    }
                }

            }
            closedList.add(currentNode);
            openList.remove(currentNode);
        }
    }

    private void createFinalPath() {

        if (goal.getParent() == null)
            return;

        Node_heuristic<Tile, Integer> node;

        node = goal;

        while (node != null) {

            path.push(Rectangle.findMiddleCoor(node.getItem().toRectangle()));

            node = node.getParent();
        }
    }

    @Override
    protected void createDataStruct() {

        //TODO replace
        //openList = new TreeSet<>(Comparator.comparingInt(Node_heuristic<Tile, Integer>::getHeuristic));
        closedList = new ArrayList<>();
    }

    @Override
    protected void clear() {

        graphe.clear();
        openList.clear();
        closedList.clear();
        path.clear();

    }

    private int getDist(Node_heuristic<Tile, Integer> node1, Node_heuristic<Tile, Integer> node2) {
        Tile t1 = node1.getItem(), t2 = node2.getItem();

        return Pythagore.calculDistanceInSquarre(t1.toCoordinates(), t2.toCoordinates());
    }
}
