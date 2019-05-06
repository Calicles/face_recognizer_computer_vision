package com.antoine.vue.listeners;

import com.antoine.manager.musique.Jukebox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <b>Classe de listenr d'un JSlider.</b>
 * Adapte le volume du gestionnaire de son à la valeur du JSLider.
 */
public class SliderChangeSoundListener implements ChangeListener {

    /**Le gestionnaire de son*/
    private Jukebox jukebox;

    public SliderChangeSoundListener(Jukebox jukebox){
        this.jukebox = jukebox;
    }

    /**
     * <p>Change la valeur du volume de bruitage du gestionnaire de son.</p>
     * @param e l'event du JSlider.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        jukebox.setSoundVolume(((float) source.getValue()) / 100);
    }

}
