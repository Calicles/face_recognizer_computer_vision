package com.antoine.modele;

import com.antoine.contracts.*;
import com.antoine.events.LevelChangeEvent;
import com.antoine.manager.musique.Jukebox;
import com.antoine.services.Assembler;
import com.antoine.geometry.Rectangle;
import com.antoine.contracts.IJeu;
import com.antoine.structure_donnee.LevelState;

import java.awt.Dimension;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * <b>Le modèle du programme</b>
 * <p>Contient l'ensemble des niveaux du jeu, des sons, bruitages...</p>
 *
 * @author Antoine
 */
public class Game implements IJeu {

    /**Les niveaux du jeu**/
    private ILevel levelApple, levelRarity, levelRainbow, levelFlutter, levelPinky, levelTwilight;

    /**Le niveau en court**/
    private ILevel levelRunning;

    /**L'ensemble des interfaces modèle/joueurs qui doivent être mis à jour**/
    private ArrayList<LevelListener> listeners;

    /**Le container qui assemble les niveaux paramétrés**/
    private Assembler assembler;

    /**Le gestionnaire de son, musique et bruitage**/
    private Jukebox jukebox;

    /**L'évènement qui prévient les listeners d'un chagement d'état**/
    private LevelChangeEvent event;

    /**
     * <p>Initialise les premiers niveaux.</p>
     */
    public Game()  {
        Path programmePath = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String configPath= Paths.get(programmePath.getParent().toString(), "config", "config_labyrinthe", "conf.xml").toString();

        listeners= new ArrayList<>();
        assembler= new Assembler(configPath);
        levelApple= (ILevel) assembler.newInstance("levelApple");
        levelRarity= (ILevel) assembler.newInstance("levelRarity");
        levelRainbow= (ILevel) assembler.newInstance("levelRainbow");
        levelRunning= levelApple;
        levelApple.selected();
        levelFlutter= null;
        levelPinky= null;
        jukebox = (Jukebox) assembler.newInstance("jukebox");
        jukebox.switchTo("apple");
        event = new LevelChangeEvent();
        event.setNumberOfLevel(6);
        setEventSelected(true, false, false);
        setEventRunning(true, true, true);
    }


    /**
     * @see IJeu#getMapWidth()
     * @return la longueur de la carte du niveau en court.
     */
    public int getMapWidth() {return levelRunning.getMapWidth();}

    /**
     * @see IJeu#getMapHeight()
     * @return la hauteur de la carte du niveau en court.
     */
    public int getMapHeight() {return levelRunning.getMapHeight();}

    /**
     * <p>Change le niveau en court et prévient le jukebox</p>
     */
    private void switchLevel4(){

        jukebox.switchTo("twilight");

        levelPinky= null;
        levelRunning= levelTwilight= (ILevel) assembler.newInstance("levelTwilight");
        assembler = null;

        event.setBooleanTable(LevelState.PINKY_RUNNING, false);
        event.setBooleanTable(LevelState.TWILIGHT_RUNNING, true);

        levelTwilight.setListeners(listeners);
        levelTwilight.setEvent(event);
        levelTwilight.start();
    }

    /**
     * @see this#switchLevel4()
     */
    private void switchLevel3() {

        jukebox.switchTo("pinky");

        levelFlutter= null;
        levelRunning= levelPinky= (ILevel) assembler.newInstance("levelPinky");

        event.setBooleanTable(LevelState.FLUTTER_RUNNING, false);
        event.setBooleanTable(LevelState.PINKY_RUNNING, true);
    }

    /**
     * @see this#switchLevel4()
     */
    private void switchLevel2() {

        jukebox.switchTo("flutter");

        levelApple = levelRarity = levelRainbow = null;
        levelRunning= levelFlutter= (ILevel) assembler.newInstance("levelFlutter");

        event.setBooleanTable(LevelState.FLUTTER_RUNNING, true);
    }

    /**
     * @see this#switchLevel4()
     */
    public void switchLeveApple() {
        jukebox.switchTo("apple");
        levelRunning= levelApple;
        levelApple.selected();
        levelRarity.deselected();
        levelRainbow.deselected();
        setEventSelected(true, false, false);
        this.fireUpdate();
    }

    /**
     * @see this#switchLevel4()
     */
    public void switchLevelRarity() {
        jukebox.switchTo("rarity");
        levelRunning= levelRarity;
        levelRarity.selected();
        levelApple.deselected();
        levelRainbow.deselected();
        setEventSelected(false, true, false);
        this.fireUpdate();

    }

    /**
     * @see this#switchLevel4()
     */
    public void switchLevelRainbow() {
        jukebox.switchTo("rainbow");
        levelRunning= levelRainbow;
        levelRainbow.selected();
        levelApple.deselected();
        levelRarity.deselected();
        setEventSelected(false, false, true);
        this.fireUpdate();
    }

    /**
     * @see IJeu#playerMovesLeft()
     */
    public void playerMovesLeft(){
        if(isLevelRunning()){
            levelRunning.playerMovesLeft();
            jukebox.makeSound();
        }
        event.setBooleanTable(LevelState.get(levelRunning.getId()), levelRunning.isRunning());
        this.fireUpdate();
    }

    /**
     * @see IJeu#playerMovesRight()
     */
    public void playerMovesRight(){
        if(isLevelRunning()){
            levelRunning.playerMovesRight();
            jukebox.makeSound();
        }
        event.setBooleanTable(LevelState.get(levelRunning.getId()), levelRunning.isRunning());
        this.fireUpdate();
    }

    /**
     * @see IJeu#playerMovesUp()
     */
    public void playerMovesUp(){
        if(isLevelRunning()){
            levelRunning.playerMovesUp();
            jukebox.makeSound();
        }
        event.setBooleanTable(LevelState.get(levelRunning.getId()), levelRunning.isRunning());
        this.fireUpdate();
    }

    /**
     * @see IJeu#playerMovesDown()
     */
    public void playerMovesDown(){
        if(isLevelRunning()){
            levelRunning.playerMovesDown();
            jukebox.makeSound();
        }
        event.setBooleanTable(LevelState.get(levelRunning.getId()), levelRunning.isRunning());
        this.fireUpdate();
    }

    /**
     * <p>Calcule si le niveau en court est fini.</p>
     * Change de niveau si niveau terminé.
     * @return true si niveau non fini, false sinon.
     */
    private boolean isLevelRunning(){

        if(isLevelPinkyNull() && isLevelFlutterNull() && levelTwilight == null &&
                !levelApple.isRunning()
                && !levelRarity.isRunning()
                && !levelRainbow.isRunning()) {
            switchLevel2();
            return false;

        }else if(isLevelsNull() && !isLevelFlutterNull() &&
                !levelFlutter.isRunning()) {
            switchLevel3();
            return false;
        }else if(isLevelsNull() && isLevelFlutterNull() && !isLevelPinkyNull() && !levelPinky.isRunning()){
            switchLevel4();
            return false;
        }
        return true;
    }

    /**
     * @see IJeu#playerMovesReleased()
     */
    public void playerMovesReleased(){
        levelRunning.playerMovesReleased();
        this.fireUpdate();
    }

    /**
     * <p>Prends la dimension du niveau en court</p>
     * @return sa dimension
     */
    public Dimension getDimension() {
        return levelRunning.getDimension();
    }

    /**
     * @see IJeu#addListener(LevelListener)
     * @param listener l'interface à ajouter à la liste des prévenus en cas de changement.
     */
    @Override
    public void addListener(LevelListener listener) {
        listeners.add(listener);
        fireUpdate();
    }

    /**
     * <p>Supprime une interface de la liste des écoutants.</p>
     * @see IJeu#removeListener(LevelListener)
     * @param listener
     */
    @Override
    public void removeListener(LevelListener listener) {
        listeners.remove(listener);
    }

    /**
     * <p>Prévient lensemble de la liste des écoutants d'un changement d'état du modèle.</p>
     */
    private void fireUpdate() {
        for(LevelListener l:listeners)
            l.update(event);
    }

    /**
     * <p>Calcul si le premier niveau est null.</p>
     * Dans ce cas les trois premiers niveaux sont finis, on charge le suivant.
     * @return true si premier level égal à null, false sinon.
     */
    private boolean isLevelsNull() {
        return levelApple == null;
    }

    /**
     * <p>Test si ce niveau est null</p>
     * Utilisé dans la gestion des chargement de niveau.
     * @return true si égal à null, false sinon.
     */
    private boolean isLevelFlutterNull() {
        return levelFlutter == null;
    }

    /**
     * @see this#isLevelFlutterNull()
     * @return
     */
    private boolean isLevelPinkyNull() {
        return levelPinky == null;
    }

    /**
     * @see IJeu#getScreenX()
     * @return la position X de l'écran sur la carte.
     */
    public int getScreenX() {
        return levelRunning.getScreenX();
    }

    /**
     * @see IJeu#getScreenY()
     * @return la position Y de l'écran sur la carte.
     */
    public int getScreenY() {
        return levelRunning.getScreenY();
    }

    /**
     * @see IJeu#getMapWidth()
     * @return la longueur de l'écran du jeu.
     */
    public int getScreenWidth() {
        return levelRunning.getScreenWidth();
    }

    /**
     * @see IJeu#getScreenHeight()
     * @return la hauteur de l'écran de jeu.
     */
    public int getScreenHeight() {
        return levelRunning.getScreenHeight();
    }

    /**
     * @see IJeu#getPlayerX()
     * @return position X du joueur sur la carte.
     */
    public int getPlayerX() {
        return levelRunning.getPlayerX();
    }

    /**
     * @see IJeu#getPlayerY()
     * @return la position Y du joueur sur la carte.
     */
    public int getPlayerY() {
        return levelRunning.getPlayerY();
    }

    /**
     * <p>Accepte et est visité par un visiteur, principalement pour affichage.</p>
     * @param visiteur qui visite cette structure.
     */
    @Override
    public void accept(IAfficheur visiteur) {
        visiteur.visit((IStructure) this.levelRunning);
    }

    /**
     * @return Retourne l'instance qui représente la carte de jeu.
     */
    @Override
    public IMap getMap() {
        return levelRunning.getMap();
    }

    /**
     *
     * @return Le joueur.
     */
    @Override
    public IEntity getPlayer() {
        return levelRunning.getPlayer();
    }

    /**
     * <p>Calcule si le niveau en court est terminé.</p>
     * @return true si niveau fini, false sinon.
     */
    @Override
    public boolean isRunning() {
        return levelRunning.isRunning();
    }

    /**
     * @return l'image de fin du niveau en court.
     */
    @Override
    public String getEndImageUrl() {
        return levelRunning.getEndImageUrl();
    }

    /**
     * @return le rectangle qui représente l'écran de jeu du niveau en court.
     */
    @Override
    public Rectangle getScreen() {
        return levelRunning.getScreen();
    }

    /**
     * @return le boss du niveau en court.
     */
    @Override
    public IEntity getBoss() {
        return levelRunning.getBoss();
    }

    /**
     * <p>Utilisé dans la gestion du son.</p>
     * @return le jukebox.
     */
    @Override
    public Jukebox getJukebox() {
        return jukebox;
    }

    /**
     * <p>change les états des trois premiers niveaux.</p>
     * @param level1Selected état de selection du niveau 1.
     * @param level2Selected du niveau 2.
     * @param level3Selected du niveau 3.
     */
    private void setEventSelected(boolean level1Selected, boolean level2Selected, boolean level3Selected){
        event.setBooleanTable(LevelState.APPLE_SELECTED, level1Selected);
        event.setBooleanTable(LevelState.RARITY_SELECTED, level2Selected);
        event.setBooleanTable(LevelState.RAINBOW_SELECTED, level3Selected);
    }

    /**
     * <p>Change l'état de fin des trois premiers niveaux.</p>
     * @param level1Running état de fin du niveau 1.
     * @param level2Running du niveau 2.
     * @param level3Running du niveau 3.
     */
    private void setEventRunning(boolean level1Running, boolean level2Running, boolean level3Running){
        event.setBooleanTable(LevelState.APPLE_RUNNING, level1Running);
        event.setBooleanTable(LevelState.RARITY_RUNNING, level2Running);
        event.setBooleanTable(LevelState.RAINBOW_RUNNING, level3Running);
    }
}

