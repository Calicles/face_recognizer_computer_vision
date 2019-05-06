package com.antoine.events;


import com.antoine.structure_donnee.LevelState;

import java.util.Arrays;

/**
 * <b>Représente un évènememnt qui change l'état d'un niveau à afficher</b>
 * <p>Les états représentés sont: running (le niveau n'est pas fini), selected (le niveau est en affichage)</p>
 *
 * @author Antoine
 */
public class LevelChangeEvent {

    /**Entier dont les bits sont utilisées en sémantique booléenne**/
    private int booleanTable = 0;

    /**Nombre de niveaux contenu dans le jeu**/
    private int numberOfLevel;

    /**Nombre de niveau terminé par le joueur**/
    private int numberOfLevelFinshed;


    public LevelChangeEvent(){
        numberOfLevelFinshed = 0;
    }

    /**
     * <p>Cahnge la valeur d'un bits dans les octets de l'entier "booleanTable"</p>
     * Représente un changement d'état d'un niveau.
     * @param state L'énumération associé à un niveau et à un état,
     *             la valeur ordinal de l'Enum donne le niveau et l'état affecté dans celui-ci.
     * @param value L'état.
     */
    public void setBooleanTable(LevelState state, boolean value){
        if (value) {
            booleanTable |= (1 << state.ordinal());

            //Ce test est pour le dernier niveau, le joueur peut-être attrapé, mais le nieau n'est pas fini
            if (numberOfLevelFinshed == 6)
                numberOfLevelFinshed--;
        }else {
            if (state.ordinal() < 6 && valueOf(state))
              numberOfLevelFinshed++;
            booleanTable &= ~(1 << state.ordinal());
        }
    }

    /**
     * <p>Trouve le bit associé à l'Enum (qui représente un état d'un niveau).</p>
     * @param state l'Enum
     * @return retourne true si bit à 1, false sinon.
     */
    public boolean valueOf(LevelState state){
        return (0x1 & (booleanTable >> state.ordinal())) == 1 ? true : false;
    }

    public int getNumber_level_finished(){
        return numberOfLevelFinshed;
    }

    public int getNumberOfLevel() {
        return this.numberOfLevel;
    }

    public void setNumberOfLevel(int numberOfLevel) {
        this.numberOfLevel = numberOfLevel;
    }

    /**
     * <p>Pour débug.</p>
     * @return l'état des bits de l'entier booleanTable.
     */
    @Override
    public String toString(){
        byte[] bytes = new byte[4];


        for (int i= 0; i < bytes.length; i++){
            bytes[i] = (byte) (0xFF & (booleanTable >> 8 * i));
        }

        return Arrays.toString(bytes);
    }


}
