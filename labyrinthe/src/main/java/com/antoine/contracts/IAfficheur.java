package com.antoine.contracts;

import java.awt.*;

/**
 * <b>Gestion de l'organisation de l'affichage d'une structure.</p>
 * L'objet Graphics est uniquement fournit par la vue.
 *
 * @author Antoine
 */
public interface IAfficheur {

    /**
     * <p>Organise la manière dont l'objet Graphics doit afficher cette structure.</p>
     * @param structure à afficher par le Graphics.
     */
    void visit(IStructure structure);

    /**
     * <p>Permet de fournir l'objet Graphics au visiteur</p>
     * @param g chargé de l'affichage, son organisation est géré par le visiteur.
     *          doit être appelé avant une visite.
     */
    void setGraphics(Graphics g);

    /**
     * <p>Libère la référence de l'instance fournit par la vue.</p>
     * A appeler après chaque visite, pour respecter la stratégie de gestion
     * des ressources des APIs awt et swing.
     */
    void freeGraphics();
}
