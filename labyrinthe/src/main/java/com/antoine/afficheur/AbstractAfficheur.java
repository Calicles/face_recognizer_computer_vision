package com.antoine.afficheur;

import com.antoine.contracts.IAfficheur;
import com.antoine.contracts.IStructure;

import java.awt.*;

/**
 * <b>Classe qui gère l'affichage du modèle par le pattern visiteur.</b>
 * supporte l'API awt/swing.
 *
 * L'object Graphics est transmis par la vue, les classe de spécialisation organisent l'affichagge.
 *
 * @author Antoine
 */
public abstract class AbstractAfficheur {

    /**transmis par la vue**/
    protected Graphics g;

    /**
     * @see com.antoine.contracts.IAfficheur#visit(IStructure)
     * @param structure dont l'affichage doit être organisé.
     */
    public abstract void visit(IStructure structure);

    /**
     * @see IAfficheur#freeGraphics()
     * @param g l'objet transmis par la vue.
     */
    public void setGraphics(Graphics g){
        this.g= g;
    }

    /**
     * @see IAfficheur#freeGraphics()
     */
    public void freeGraphics(){
        this.g= null;
    }
}
