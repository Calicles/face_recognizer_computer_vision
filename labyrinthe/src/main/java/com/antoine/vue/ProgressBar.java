package com.antoine.vue;

import com.antoine.contracts.LevelListener;
import com.antoine.events.LevelChangeEvent;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

/**
 * <b>La barre de progression qui représente l'avancée du joueur dans le jeu.</b>
 *
 * @author Antoine
 */
public class ProgressBar extends JProgressBar implements LevelListener {


    public ProgressBar(int minimum, int maximum)
    {
        this.setBorderPainted(true);
        this.setStringPainted(true);

        this.setMinimum(minimum);
        this.setMaximum(maximum);

        this.setBackground(Color.PINK);
        this.setForeground(Color.RED);
    }

    /**
     * <p>Fait avancé la barre de progression selon les niveaux terminés.</p>
     * @see LevelListener#update(LevelChangeEvent)
     * @param lve l'évènement contenant des information pour guider la mise à jour.
     */
    @Override
    public void update(LevelChangeEvent lve) {
        String total = ""+lve.getNumber_level_finished()+ " / "+lve.getNumberOfLevel();
        setString(total);
        setValue(lve.getNumber_level_finished());
    }
}
