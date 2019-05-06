package com.antoine.son;

import java.io.ByteArrayOutputStream;

/**
 * <b>Lecteur de bruitage.</b>
 * Les octets du flux sont copiés dans tableau (en cas de réutilisation probable).
 */
public class SoundEffect extends SoundMaker {

    /**Contient les octets du flux après copie*/
    private byte[] samples;

    /**
     * <p>Constructeur.</p>
     * @param musicPath le path du fichier.
     * @param volume le volume à appliquer.
     */
    public SoundEffect(String musicPath, float volume) {
        super(musicPath, volume);
        samples = getAudioFileData();
    }

    /**
     * @see SoundMaker#implementRun()
     * @return une Thread implémenté.
     */
    @Override
    protected Thread implementRun() {
        return new Thread(()->{
            int bytesRead;
            byte[] buf;
            while (using) {
                bytesRead = 0;
                while ((bytesRead <= samples.length) && using) {
                    buf = adjustVolume(bytesRead, 128);
                    bytesRead += buf.length;
                    line.write(buf, 0, buf.length);
                }
                sleep();
            }
        });
    }

    /**
     * <p>Place le Thread en état de pause.</p>
     */
    private synchronized void sleep() {
        try{
            wait();
        }catch (InterruptedException ignored){}
    }

    /**
     * <p>Copie les octets du flux d'entrée vers l'array samples</p>
     * @return
     */
    private byte[] getAudioFileData() {
        byte[] data = null;
        try {
            final ByteArrayOutputStream baout = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int c;
            while ((c = ais.read(buffer, 0, buffer.length)) != -1) {
                baout.write(buffer, 0, c);
            }
            ais.close();
            baout.close();
            data = baout.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * <p>Ajuste le volume d'une partie des octets contenu dans l'array samples.</p>
     * La portion est délibérément réduite pour simuler l'ajustement du son en temps réel.
     *
     * @param start le premier octet à devoir être copié.
     *
     * @param len la longueur de la portion à copier à partir de start.
     *
     * @return un array copié à partir de la portion de l'array samples.
     */
    protected byte[] adjustVolume(int start, int len) {
        byte[] array = new byte[len];

        len = start + len;

        if (len >= samples.length)
            len = samples.length ;

        for (int i = start; i < len; i+=2)
        {
            // convert byte pair to int
            short buf1 = samples[i+1];
            short buf2 = samples[i];

            buf1 = (short) ((buf1 & 0xff) << 8);
            buf2 = (short) (buf2 & 0xff);

            short res= (short) (buf1 | buf2);
            res = (short) (res * volume);

            // convert back
            array[i - start] = (byte) res;
            array[(i+1) - start] = (byte) (res >> 8);

        }
        return array;
    }
}
