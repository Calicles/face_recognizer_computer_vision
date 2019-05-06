package com.antoine.son;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * <b>Sert de cadre à un lecteur de fichiers son (.wav).</b>
 * Gère plusieurs formats de compression.
 *
 * Possède une gestion du son "temps réel".
 *
 * Amorçe le flux de lecture du fichier.
 */
public abstract class SoundMaker {

    /**Le Thread qui poursuit la lecture*/
    protected Thread thread;

    /**Le flux d'entrée*/
    protected AudioInputStream ais=null;

    /**Le flux "d'écriture" vers les périphérique de sons*/
    protected SourceDataLine line;

    /**Etat qui simule si l'utilisateur du lecteur est toujours d'actualité*/
    boolean using;

    /**Coefficient servant à ajuster le volume sonore doit être compris entre 1 et 0.
     * 1 pour état inchangé, 0 pour supression total du son.
     * Le volume sert uniquement à atténuer le son dans un souci de pseudo étalonnage sonore.
     * Evite qu'un volume trop fort ne gêne l'utilisateur au démarrage.
     */
    float volume;


    /**
     * <p>Initialise les états</p>
     * @param musicPath du fichier .wav
     */
    public SoundMaker(String musicPath, float volume){
        checkVolumeinRange(volume);
        thread = implementRun();
        thread.setDaemon(true);
        using = true;
        this.volume= volume;
        init(musicPath);
    }

    /**
     * <p>Doit créer le Thread avec son implémentation.</p>
     * @return un Thread prêt à être démarré;
     */
    protected abstract Thread implementRun();

    public void setVolume(float volume){
        checkVolumeinRange(volume);
        this.volume = volume;
    }

    /**
     * <P>Vérifie si volume est dans la bonne plage (le volume ne dépasse pas l'état initiale des octets du flux.</P>
     * Uniquement pour atténuer le volume sonore.
     * @param volume le volume (coefficient) à appliquer.
     */
    private void checkVolumeinRange(float volume){
        if(volume < 0 && volume > 1)
            throw new IllegalArgumentException("volume doit être compris entre 0 et 1 :"+volume);
    }

    /**
     * <p>Démarre le Thread ou le réveille, selon son état actuel.</p>
     */
    public void play(){
        if (!thread.isAlive()){
            thread.start();
        }else{
            synchronized(this) {
                notify();
            }
        }
    }

    /**
     * <p>Ouvre le flux en lecture AudioInputStrem et écriture (SourceDataLine)</p>
     * Créé un effet de pipeline selon l'API soundSystem.
}
     * @param musicPath le path du fichier son.
     */
    private void init(String musicPath){
        URL url = this.getClass().getResource(musicPath);

        try {

            AudioFileFormat format = AudioSystem.getAudioFileFormat(url);

            ais = AudioSystem.getAudioInputStream(url);

            AudioFormat audioFormat = ais.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            if (AudioSystem.isLineSupported(info)) {

                line = (SourceDataLine) AudioSystem.getLine(info);

            }else{

                AudioFormat targetAudioFormat;

                targetAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        audioFormat.getSampleRate(),
                        audioFormat.getSampleSizeInBits(),
                        audioFormat.getChannels(),
                        audioFormat.getFrameSize(),
                        audioFormat.getFrameRate(),
                        false);

                ais = AudioSystem.getAudioInputStream(targetAudioFormat, ais);

                info = new DataLine.Info(SourceDataLine.class, targetAudioFormat);

                line = (SourceDataLine) AudioSystem.getLine(info);



                audioFormat = targetAudioFormat;
            }

            line.open(audioFormat);

            line.start();

        }catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("erreur de lecture du fichier de musique");
        }
    }
}

