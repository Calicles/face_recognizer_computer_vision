package com.antoine.structure_donnee.pathfinding;

/**
 * <b>Représente un noeud dans un graphe acyclique avec heuristique.</b>
 * <p>Enveloppe un item pour être représenter dans ce graphe.</p>
 *
 * @param <T> le type de la classe enveloppée
 * @param <V> le type de traité par l'heuristique (String, int, float...)
 *
 * @author Antoine
 */
public class Node_heuristic<T, V> {

    /**
     * <p>La classe qui est enveloppé pour être représenté dans le graphe</p>
     */
    private T item;

    /**
     * <p>L'heuristique utilisé en cas d'algorithme en ayant besoin</p>
     */
    private V heuristic;

    /**
     * <p>Le poids du noeud dans le graphe</p>
     */
    private int weight;

    /**
     * <p>Le parent de ce noeud dans le graphe</p>
     */
    private Node_heuristic<T, V> parentNode;


    /**
     * <p>Constructeur</p>
     *
     * @param item l'occurence à envelopper
     */
    public Node_heuristic(T item){
        this.item = item;
        weight = -1;
    }

    public T getItem(){
        return item;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setParentNode(Node_heuristic<T, V> parentNode)
    {
        this.parentNode = parentNode;
    }
    public Node_heuristic<T, V> getParent()
    {
        return parentNode;
    }

    public void setHeuristic(V value) {
        this.heuristic = value;
    }

    public V getHeuristic() {
        return heuristic;
    }
}
