package com.antoine.manager.musique;

import com.antoine.son.MusicPlayer;
import com.antoine.son.SoundEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <b>Classe de gestion du son.</b>
 * Gère les musiques ainsi que le bruitage.
 * La gestion comprend: jouer la musique ou un bruitage, mettre sur pause ou effacer.
 *
 * @author Antoine
 */
public class Jukebox {

    /**Associe un id à une instance de classe qui joue la musique*/
    private Map<String, MusicPlayer> musics;

    /**Associe un id à une bruitage*/
    private Map<String, SoundEffect> sounds;

    /**Enregistre l'id qui est joué*/
    private String currentPlaying = "";

    /**Coefficents qui multiplient les octets d'un flux de son pour gérer le volume*/
    private float musicVolume, soundVolume;

    public Jukebox(){
        musicVolume = 0.1f;
        soundVolume = 0.5f;
        musics = new HashMap<>();
        sounds = new HashMap<>();
    }

    /**
     * <p>Enregistre un changement de volume pour la musique.</p>
     * @param volume le nouveau volume de la musique.
     */
    public void setMusicVolume(final float volume){
        this.musicVolume = volume;
        Set<String > musicSet = musics.keySet();
        for (String s: musicSet){
            musics.get(s).setVolume(volume);
        }
    }

    /**
     * <p>Enregistre un changement de volume pour le bruitage.</p>
     * @param volume le nouveau volume du bruitage.
     */
    public void setSoundVolume(final float volume){
        this.soundVolume = volume;
        Set<String> soundSet = sounds.keySet();
        for (String s: soundSet){
            sounds.get(s).setVolume(volume);
        }
    }

    public void setMusic(String idMusicPath){
        String[] buf = idMusicPath.split(",");
        musics.put(buf[0], new MusicPlayer(buf[1], musicVolume));
        currentPlaying = buf[0];
    }

    public void setSound(String idSoundPath){
        String[] buf = idSoundPath.split(",");
        sounds.put(buf[0], new SoundEffect(buf[1], musicVolume));
    }

    /**
     * <p>Mets sur pause les objets en cours qui jouent musique et bruitage pour lancer
     * ceux associés à l'id</p>
     * Place currentPlaying sur le paramètre name.
     * @param name l'id des objets musique et bruitage qui doivent jouer.
     */
    public void switchTo(String name){
        if (!currentPlaying.equals(name))
        {
            if (musics.containsKey(name))
            {
                musics.get(currentPlaying).pause();
                musics.get(name).play();

                currentPlaying = name;
            }
        }
    }

    /**
     * <p>Produit le bruitage associé à l'id en cours (currentPlaying).</p>
     */
    public void makeSound(){
        sounds.get(currentPlaying).play();
    }

    /**
     * <p>Stop une musique et l'efface de la mémoire.</p>
     * @param name l'id à stopper.
     */
    public void stop(String name){
        musics.get(name).arret();
        free(name);
    }

    /**
     * <p>Efface les players associés à un id.</p>
     * @param name l'id des players à supprimer.
     */
    public void free(String name){
        musics.remove(name);
        sounds.remove(name);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume(){
        return soundVolume;
    }
}
