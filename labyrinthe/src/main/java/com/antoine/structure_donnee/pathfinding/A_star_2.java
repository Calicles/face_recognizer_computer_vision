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
 * <b>Implémente l'algorithme A* (trouve un chemin plus rapidement que Dijkstra, mais pas forcément le plus court),
 * avec en complément placement du path en fonction de la surface de l'entité qui se déplace</b>
 *
 * @param <T> Le type utilisé pour le calcule de l'heuristique
 *
 * @author Antoine
 */
public class A_star_2<T> extends AbstractPathfinding_algo implements IPathfinding {

    /**Sous-matrice découpée dans la map*/
    private Node_heuristic<Tile, T>[][] Matrix;

    /**Liste des tuiles selectionnées pour examen dans le calcul du meilleur chemin*/
    private TreeSet<Node_heuristic<Tile, T>> openList;

    /**listes des tuiles examinées*/
    private ArrayList<Node_heuristic<Tile, T>> closedList;

    /**heuristique utilisé dans l'algorithme*/
    private IHeuristic<T> heuristic;

    private Node_heuristic<Tile, T> currentNode, adjacentNode, goal;

    /**Sert à savoir si besoin de décaler le path à la taille du perso*/
    private boolean isMoverSmallerThanTile;

    /**
     * <p>Constructeur plein</p>
     * @param heuristic utilisé pour l'algorithme
     */
    public A_star_2(IHeuristic heuristic) {

        this.heuristic = heuristic;

    }

    public A_star_2() {
    }

    public void setHeuristic(IHeuristic heuristic) {
        this.heuristic = heuristic;

            Comparator<Node_heuristic<Tile, T>> comp = heuristic.getComparator();
        openList = new TreeSet<>(heuristic.getComparator());
    }

    /**
     * @see IPathfinding#getPath()
     * @return le path construit
     */
    @Override
    public Stack<Coordinates> getPath() {
        return (Stack<Coordinates>) path.clone();
    }

    /**
     * @see IPathfinding#getAdjacentNode()
     * @return une tuile adjacente à celle en cours d'examen.
     */
    @Override
    public Node_heuristic getAdjacentNode() {
        return this.adjacentNode;
    }

    /**
     * @see IPathfinding#getGoalNode()
     * @return la tuile contenant les coordonnées du but à atteindre.
     */
    @Override
    public Node_heuristic getGoalNode() {
        return this.goal;
    }

    /**
     * @see AbstractPathfinding_algo#createDataStruct()
     */
    @Override
    protected void createDataStruct()
    {
        closedList = new ArrayList<>();
    }

    /**
     * @see IPathfinding#createPath(Rectangle, Coordinates, IMap)
     * @param mover l'entité qui se déplace
     * @param goal le but à atteindre
     * @param map la carte sur laquelle se deplace le mover
     * @return une pile contenant les coordonnées à atteindre
     */
    @Override
    public Stack<Coordinates> createPath(Rectangle mover, Coordinates goal, IMap map)
    {
        this.createRectangle(mover, goal, map);

        int row, col, moverWidthInTile, moverHeightInTile;
        Coordinates currentNodeCoor;

        moverWidthInTile = mover.getWidth() / Tile.getWidth();
        if (moverWidthInTile %2 != 0 && moverWidthInTile > 1) moverWidthInTile--;
        moverHeightInTile = mover.getHeight() / Tile.getHeight();
        if (moverHeightInTile %2 != 0 && moverHeightInTile > 1) moverHeightInTile--;

        if (moverWidthInTile < 1 && moverHeightInTile < 1)
            isMoverSmallerThanTile = true;
        else
            isMoverSmallerThanTile = false;

        setData(goal, map);

        //+++++++++++++++++++++++++++++++++++    start's algorithm   +++++++++++++++++++++++++++++++++++++++++

        while (!openList.isEmpty()) {

            currentNode = openList.first();

            if (currentNode == this.goal || (!isMoverSmallerThanTile && goalIsInProximalDist(moverWidthInTile, moverHeightInTile))) {

                if(!isMoverSmallerThanTile) this.goal = currentNode;

                //adapt_path_forMover(mover, map);
                fillPath();

                return path;
            }

            //==========   for the calcul of index of the currentNode in Matrix  ===========

            currentNodeCoor = currentNode.getItem().toCoordinates();

            //=================   get adjacents Tiles   ==================

            for (int i = -1 ; i < 2; i++) {

                for (int j = -1; j < 2; j++) {

                    //the current tile
                    if (i == 0 && j == 0)
                        continue;

                    //diagonal tile
                    if ((i != 0) && (j != 0))
                        continue;

                    int weight = currentNode.getWeight() + 1;

                    col = j + (currentNodeCoor.getX() / Tile.getWidth() - surface.getBeginX());
                    row = i + (currentNodeCoor.getY() / Tile.getHeight() - surface.getBeginY());

                    if (col >= Matrix[0].length || row >= Matrix.length || col < 0 || row < 0)
                        continue;

                    adjacentNode = Matrix[row][col];

                    if (adjacentNode.getItem().isSolid())
                        continue;

                    if (!closedList.contains(adjacentNode)) {

                        if (isMoverSmallerThanTile || !isRestrictTileArround(row, col, moverWidthInTile, moverHeightInTile)) {

                            if (adjacentNode.getWeight() > weight || adjacentNode.getWeight() == -1) {

                                adjacentNode.setWeight(weight);

                                adjacentNode.setHeuristic(this.heuristic.getHeuristicCost(this));

                                adjacentNode.setParentNode(currentNode);
                            }
                            openList.add(adjacentNode);
                        }
                    }
                }
            }
            openList.remove(currentNode);
            closedList.add(currentNode);
        }
        //++++++++++++++++++++++++++++++++++++++++  ALGORITHM'S END   +++++++++++++++++++++++++++++++++++

        //If there, no result
        return null;
    }

    /**
     * <p>Remonte la liste chaînée pour instruire le path.</p>
     */
    private void fillPath() {

        Node_heuristic<Tile, T> node = goal;

        while (node.getParent() != null)
        {
            path.push(Rectangle.findMiddleCoor(node.getItem().toRectangle()));

            node = node.getParent();
        }
    }

    /**
     * <p>Calcule si une tuile est dans une certaine distance du but.</p>
     * Ce calcule sert à éviter une incapacité de trouver un chemin si le personnage est plus grand qu'une tuile.
     * En effet l'alorithme empêcherait la tuile contenant le but dêtre trouvée si elle se situe près d'un mur.
     * @param moverWidthInTile longueur du personnage traduite en tuile (si sa longueur fait deux tuiles...).
     * @param moverHeightInTile hauteur du personnage en tuile.
     * @return vrai si la tuile actuelle est proche du but(selon les dimensions du personnages en tuils), false sinon.
     */
    private boolean goalIsInProximalDist(int moverWidthInTile, int moverHeightInTile) {
        if (isInSameX_line(currentNode.getItem().toCoordinates(), goal.getItem().toCoordinates())) {
            int square = Pythagore.calculDistanceInSquarre(currentNode.getItem().toCoordinates(), goal.getItem().toCoordinates());

            int powTile = (int) (Math.pow(Tile.getWidth(), 2) * (moverWidthInTile + 1));

            if (square <= powTile) {
                boolean test = true;
                return true;
            }
        } else if (isInSameY_line(currentNode.getItem().toCoordinates(), goal.getItem().toCoordinates())){
            int square2 = Pythagore.calculDistanceInSquarre(currentNode.getItem().toCoordinates(), goal.getItem().toCoordinates());
            int pow2 = (int) (Math.pow(Tile.getHeight(), 2) * (moverHeightInTile + 1));
            if (square2 <= pow2) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Calcule si les tuiles adjacentes aux tuiles (elles-mêmes adjacentes à celles examinée) sont proches
     * d'une tuile solide.</p>
     * @param row ligne dans la matrice de la tuile en cours d'examen.
     * @param col colonne de la tuile en cours d'exament.
     * @param moverWidthInTile longueur, en tuile, du personnage.
     * @param moverHeightInTile hauteur, en tuile, du personnage.
     * @return true si une tuile solide est à une distance inférieur de la longueur, en tuile du personnage
     *  ou de sa hauteur, false sinon.
     */
    private boolean isRestrictTileArround(int row, int col, int moverWidthInTile, int moverHeightInTile) {

        for (int i = -moverHeightInTile; i <= moverHeightInTile; i++)
        {
            if (i+row < 0 || i + row >= Matrix.length)
                continue;

            for (int j = -moverWidthInTile; j <= moverWidthInTile; j++)
            {
                //Out of map or current Node
                if (j + col < 0 || j + col >= Matrix[0].length || (i + row == row && j + col == col))
                    continue;

                if (Matrix[i + row][j + col].getItem().isSolid())
                    return true;
            }
        }

        return false;
    }

    /**
     * <p>Calcule si deux points sont alignés dans l'axe des Y</p>
     * @param coor1 premier point.
     * @param coor2 deuxième point.
     * @return true si les X sont égaux, false sinon.
     */
    private boolean isInSameY_line(Coordinates coor1, Coordinates coor2)
    {
        return coor1.getX() == coor2.getX();
    }

    /**
     * <p>Calcule si deux points sont alignés dans l'axe des X</p>
     * @param coor1 premier point.
     * @param coor2 deuxième point.
     * @return true si les Y sont égaux, false sinon.
     */
    private boolean isInSameX_line(Coordinates coor1, Coordinates coor2)
    {
        return coor1.getY() == coor2.getY();
    }

    /**
     * @see AbstractPathfinding_algo#clear()
     */
    @Override
    protected void clear()
    {
        path.clear();
        openList.clear();
        closedList.clear();
    }

    /**
     * <p>Créé un rectangle représenter la partie de la carte à copier (position et bornes).</p>
     * @see AbstractPathfinding_algo#createRectangle(Rectangle, Coordinates, IMap)
     * @param mover le rectangle qui représente l'entité se déplaçant.
     * @param goal le but.
     * @param map la carte à copier.
     */
    @Override
    protected void createRectangle(Rectangle mover, Coordinates goal, IMap map)
    {

        clear();

        //start = getMoverStartCorner(mover, goal);
        start = Rectangle.findMiddleCoor(mover);

        //========Création du rectangle pour découper la carte=======
        int boundX, boundY;

        int x = Math.min(start.getX(), goal.getX()) / Tile.getWidth();
        int y = Math.min(start.getY(), goal.getY()) / Tile.getHeight();

        int width = Math.max(start.getX(), goal.getX()) / Tile.getWidth();
        int height = Math.max(start.getY(), goal.getY()) / Tile.getHeight();


        //Si rectangle trop petit, on l'agrandit pour trouver des chemins possibles
        if (width < 15) {
            width += 5;
            x -= 5;
        }
        if (height < 15) {
            height += 5;
            y -= 5;
        }

        boundX = x + width;
        boundY = y + height;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (boundX > map.getWidthInTile() - 1) boundX = map.getWidthInTile();
        if (boundY > map.getHeightInTile() - 1) boundY = map.getHeightInTile();

        surface = new Rectangle(x, boundX, y, boundY);
        //===============================================================

    }

    /**
     * <p>initialise les données avant de démarrer l'algorithme.</p>
     * utilise le champ surface pour le réajuster aux dimensions de l'array des tuiles
     * pour récupérer la partie de la map.
     * wrap l'ensemble des tuiles dans des noeuds pour former la matrice.
     * @param map la carte
     */
    private void setData(Coordinates goal, IMap map)
    {

        Tile[][] subMap = map.getsubMapInArray(surface);

        Matrix = new Node_heuristic[subMap.length][subMap[0].length];

        Node_heuristic<Tile, T> node;

        for (int i = 0; i < subMap.length; i++) {

            for (int j = 0; j < subMap[0].length; j++) {

                node = new Node_heuristic<>(subMap[i][j]);

                Matrix[i][j] = node;

                //Si la tuile contient les coordonnées de l'entité, elle est placé en départ
                if (subMap[i][j].contains(start)) {
                    node.setWeight(0);
                    openList.add(node);

                    //Si contient les coordonées du but, la tuile est l'arrivée
                } else if (subMap[i][j].contains(goal)) {
                    this.goal = node;
                }
            }
        }
    }
}
