package com.antoine.structure_donnee;

/**
 * <b>Enumération utilisée dans les évènements d'un niveau de jeu.</b>
 * @see com.antoine.events.LevelChangeEvent
 *
 * @author Antoine
 */
public enum LevelState {

    APPLE_RUNNING,
    RARITY_RUNNING,
    RAINBOW_RUNNING,
    FLUTTER_RUNNING,
    PINKY_RUNNING,
    TWILIGHT_RUNNING,
    APPLE_SELECTED,
    RARITY_SELECTED,
    RAINBOW_SELECTED;

    private LevelState() {
    }

    public static LevelState get(int ordinal){
        switch (ordinal){
            case 0 : return APPLE_RUNNING;
            case 1 : return RARITY_RUNNING;
            case 2 : return RAINBOW_RUNNING;
            case 3 : return FLUTTER_RUNNING;
            case 4 : return PINKY_RUNNING;
            case 5 : return TWILIGHT_RUNNING;
            case 6 : return APPLE_SELECTED;
            case 7 : return RARITY_SELECTED;
            case 8 : return RAINBOW_SELECTED;
            default: throw new IllegalArgumentException("numéro non compris dans l'énumération");

        }
    }

}
