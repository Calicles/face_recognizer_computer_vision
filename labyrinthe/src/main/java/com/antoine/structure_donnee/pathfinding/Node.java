package com.antoine.structure_donnee.pathfinding;


/**
 * <b>Représente un noeud dans un graphe acyclique.</b>
 * <p>Enveloppe un item pour être représenter dans ce graphe.
 * Peut-être utilisé avec heuristique</p>
 *
 * @param <T> le type de la classe enveloppée
 *
 * @author Antoine
 */
public class Node<T>  {

    /**
     * <p>La classe qui est enveloppé pour être représenté dans le graphe</p>
     */
    private T item;

    /**
     * <p>Le poids du noeud dans le graphe</p>
     */
    private int weight;

    /**
     * <p>Le parent de ce noeud dans le graphe</p>
     */
    private Node<T> parentNode;

    /**
     * <p>Constructeur</p>
     *
     * @param item l'occurence à envelopper
     */
    public Node(T item){
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

    public void setParentNode(Node<T> parentNode)
    {
        this.parentNode = parentNode;
    }
    public Node<T> getParent()
    {
        return parentNode;
    }
}
