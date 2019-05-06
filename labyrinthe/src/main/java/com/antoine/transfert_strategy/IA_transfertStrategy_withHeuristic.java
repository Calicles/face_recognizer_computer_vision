package com.antoine.transfert_strategy;

import com.antoine.contracts.IHeuristic;
import com.antoine.contracts.IMap;
import com.antoine.contracts.IPathfinding;
import com.antoine.contracts.ITransfert_strategy;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;
import com.antoine.structure_donnee.pathfinding.HeuristicBestDistance;
import com.antoine.structure_donnee.pathfinding.A_star_2;

import java.util.Stack;


/**
 * <b>Classe qui représente la capacité de déplacement d'un personnage non joueur
 * qui utilise l'algorithme passé en paramètre
 * pour trouver le meilleur chemin pour poursuivre le joueur.</b>
 *
 * @author Antoine
 */
public class IA_transfertStrategy_withHeuristic extends IA_transfertStrategy_std implements ITransfert_strategy {

    /**
     * <p>L'algorithme qui doit trouver le chemin vers le joueur.</p>
     */
    private IPathfinding pathfinder;

    /**
     * <p>Coordonnées utilisées pour mettre à jour la nécessitée de recalculée l'itinéraire</p>
     */
    private Coordinates currentPlayerPosition, oldPlayerPos;

    /**
     * <p>Pile de coordonnée représentant un pathfinding</p>
     */
    private Stack<Coordinates> path;

    /**
     * <p>La direction courante à suivre pour arriver au but</p>
     */
    private Coordinates currentStep;


    //===============================   Constructeurs   ==============================
    /**
     * <p>Constructeur vide.</p>
     * Donne la valeur d'un algorithme A*.2 au pathfinder.
     * Avec pour heuristique, la distance de la tuile au but à vol d'oiseau.
     */
    public IA_transfertStrategy_withHeuristic() {
        super();
        pathfinder = new A_star_2<Integer>();
        pathfinder.setHeuristic(new HeuristicBestDistance());
    }

    public IA_transfertStrategy_withHeuristic(IPathfinding pathfinder)
    {
        super();
        this.pathfinder = pathfinder;
    }

    //================================================================================

    /**
     * @see IA_transfertStrategy_std#setOwnPosition(Rectangle)
     */
    @Override
    public void setOwnPosition(Rectangle ownPosition){
        super.setOwnPosition(ownPosition);
    }

    /**
     * @see IA_transfertStrategy_std#setAttributes(Rectangle, Rectangle, IMap)
     */
    @Override
    public void setAttributes(Rectangle ownPosition, Rectangle player, IMap map) {
        super.setAttributes(ownPosition, player, map);
        currentPlayerPosition = new Coordinates(player.getBeginX(), player.getBeginY());
        oldPlayerPos = new Coordinates(player.getBeginX(), player.getBeginY());
    }


    //TODO Remove after Test
    @Override
    public Stack<Coordinates> getPath() {
        return pathfinder.getPath();
    }


    /**
     * <p>Utilise le pathfinder pour trouver un chemin vers le joueur.</p>
     * Adapte le vecteur en cas de chemin non trouver ou d'obstacle.
     */
    @Override
    protected void manHuntPlayer() {

        Coordinates middle = Rectangle.findMiddleCoor(ownPosition);
        Rectangle nexus = new Rectangle(middle.getX() - 3, middle.getX() + 3, middle.getY() - 3, middle.getY() + 3);



        currentPlayerPosition.setCoordinates(Rectangle.findMiddleCoor(player1));

        if (!oldPlayerPos.equals(currentPlayerPosition) || currentStep == null) {
            oldPlayerPos.setCoordinates(currentPlayerPosition.getX(), currentPlayerPosition.getY());

            path = pathfinder.createPath(
                    ownPosition, currentPlayerPosition,
                    map
            );
            if (path != null && !path.isEmpty()) {
                currentStep = path.pop();
            }
        }

        if (currentStep != null) {
            if (Rectangle.isInBox(nexus, currentStep)) {

                if (path != null && !path.isEmpty())
                    currentStep = path.pop();
                else
                    currentStep = null;
            }
        }
        go(middle, nexus);
    }

    /**
     * <p>Se passe du calcule de l'algorithme pour chasser le joueur.</p>
     */
    private void goToPlayer()
    {
        super.manHuntPlayer();
    }

    /**
     * <p>Calcul du vecteur en fonction de la prochaine position à atteindre.</p>
     * @param middle la coordonnée du centre du rectangle qui représente le personnage.
     * @param nexus un carré construit autour du centre du personnage.
     */
    private void go(Coordinates middle, Rectangle nexus)
    {
        if(currentStep != null) {

            if (currentStep.getX() < middle.getX() && inHeight(nexus))
                movesLeft();
            else if (currentStep.getY() < middle.getY())
                movesUp();
            else if (currentStep.getX() > middle.getX() && inHeight(nexus))
                movesRight();
            else if (currentStep.getY() > middle.getY())
                movesDown();

        }else {
                goToPlayer();
        }
    }

    /**
     * <p>Calcule si la coordonnée Y de la prochaine direction est compris dans les bornes du nexus.</p>
     * @param nexus petit carré construit autour du point central du rectangle qui représente le personnage.
     * @return true si la coordonnée Y de la prochaine étape est comprise dans les bornes du nexus
     * (entre le début du rectangle et sa fin), false sinon.
     */
    private boolean inHeight(Rectangle nexus) {
        return (currentStep.getY() >= nexus.getBeginY() && currentStep.getY() <= nexus.getEndY());
    }

    /**
     * <p>Calcule dans l'axe des X du nexus.</p>
     * @see #inHeight(Rectangle)
     * @param nexus petit carré construit autour du point central du personnage.
     * @return true si la coordonnée X de la prochaine étape du pathfinding est comprise
     * entre les bornes du nexus.
     */
    private boolean inWidth(Rectangle nexus) {
        return (currentStep.getX() >= nexus.getBeginX() && currentStep.getX() <= nexus.getEndX());
    }
}
