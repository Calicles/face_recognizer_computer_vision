package com.antoine.vue.listeners;

import com.antoine.manager.musique.Jukebox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <b>Classe de listener pour un Jslider</b>
 * Adapte le volume de la musique à la valeur du JSlider.
 */
public class SliderChangeMusicListener implements ChangeListener {

    /**Le gestionnaire de son*/
    private Jukebox jukebox;

    public SliderChangeMusicListener(Jukebox jukebox){
        this.jukebox = jukebox;
    }

    /**
     * <p>Change la valeur du volume du gestionnaire de son pour la musique.</p>
     * @param e l'évènement du JSlider.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        jukebox.setMusicVolume( ((float) source.getValue() / 100));
    }

}
