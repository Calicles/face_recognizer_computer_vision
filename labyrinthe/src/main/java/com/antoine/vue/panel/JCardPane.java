package com.antoine.vue.panel;

import com.antoine.contracts.LevelListener;
import com.antoine.events.LevelChangeEvent;
import com.antoine.structure_donnee.LevelState;

import javax.swing.*;
import java.awt.*;

/**
 * <b>Panel qui contient deux panels qu'il affiche à la suite.</b>
 *
 * @author Antoine
 */
public class JCardPane extends JPanel implements LevelListener {

    /**Panels*/
    private JPanel pane1, pane2, container;

    /**Les labels des panels à présenter*/
    private String label1, labelPane2;

    /**
     * <p>Constructeur plein.</p>
     * @param label1 le nom du panel 1.
     * @param panel1 le panel 1.
     * @param label2 le nom du panel 2.
     * @param pane2 le panel 2.
     */
    public JCardPane(String label1, JPanel panel1, String label2, JPanel pane2)
    {
        super(new BorderLayout());

        this.pane1 = panel1;
        this.pane2 = pane2;
        this.label1 = label1;
        pane2.setPreferredSize(panel1.getPreferredSize());
        this.labelPane2 = label2;
        container = new JPanel(new CardLayout());
        container.add(label1, panel1);
        container.add(label2, pane2);
        container.setBorder(BorderFactory.createRaisedBevelBorder());
        this.add(container, BorderLayout.CENTER);
    }

    /**
     * <p>Change de panel à présenter selon l'évolution du jeu.</p>
     * @see LevelListener#update(LevelChangeEvent)
     * @param lve l'évènement contenant des information pour guider la mise à jour.
     */
    @Override
    public void update(LevelChangeEvent lve){
        if (!isFirstsLevelsRunning(lve)){
            switchPane2();
        }
        repaint();
    }

    /**
     * <p>Vérifie si un point de déroulement du jeu est dépassé.</p>
     * @param lve levelEvent.
     * @return true si les trois premiers niveaux sont terminés, false sinon.
     */
    private boolean isFirstsLevelsRunning(LevelChangeEvent lve) {
        return (lve.valueOf(LevelState.APPLE_RUNNING) || lve.valueOf(LevelState.RARITY_RUNNING) ||
                lve.valueOf(LevelState.RAINBOW_RUNNING));
    }

    /**
     * @see JPanel#getPreferredSize()
     * @return
     */
    @Override
    public Dimension getPreferredSize(){
        return pane1.getPreferredSize();
    }

    /**
     * <p>Change le panel affiché.</p>
     */
    public void switchPane2(){
        CardLayout card= (CardLayout) container.getLayout();
        card.show(container, labelPane2);
    }


}
