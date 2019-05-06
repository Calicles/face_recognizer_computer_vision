package com.antoine.modele.level;

import com.antoine.contracts.IEnnemi;
import com.antoine.contracts.ILevel;
import com.antoine.contracts.LevelListener;
import com.antoine.entity.Boss;
import com.antoine.events.LevelChangeEvent;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.DoubleBoxes;
import com.antoine.geometry.Rectangle;
import com.antoine.manager.musique.Jukebox;
import com.antoine.services.ImageReader;
import com.antoine.structure_donnee.LevelState;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Stack;


/**
 * <b>Type de niveau qui gère le scrolling et possède un Thread pour sa propre boucle logique.</b>
 *
 * @author antoine
 */
public class Level4 extends Level3 implements ILevel {

    /**Un ennemi sur la carte*/
    private IEnnemi boss;

    /**Thread de boucle de déroulée du jeu*/
    private Thread gameLoop;

    /**Les écoutant du niveau (gérer en interne)*/
    private List<LevelListener> listeners;

    /**Les évènement (gérer en interne)*/
    private LevelChangeEvent event;

    /**Les coordonnées de départ pour replacer joueur, ennemi et écran, en début de boucle*/
    private Coordinates startPlayerPosition, startBossPosition, startScreenPoisiton, startScrollPosition;

    /**Si le joueur est attrapé*/
    private boolean over;

    /**Pour étalonner la gestion du temps des cycles processeur sur machine rapide ou lente*/
    private long before, after;

    /**Choix du temps minimum écoulé entre chaque boucle*/
    private final long SLEEP= 1000 / 24;

    /**image affichée lorsque le joueur est attrapé*/
    private String loseImagePath;

    /**Path d'image de fin de jeu.*/
    private int numberEndAnimationImages;


    public Level4(){
        super();
        init();
        over= false;
    }

    /**
     * <p>Doit être appelée après avoir injecté player et map</p>
     * @param boss ennemi du niveau
     */
    public void setBoss(IEnnemi boss){
        this.boss= boss;
        this.boss.setAttributes(player.getPosition(), map);
        startBossPosition= new Coordinates(boss.getX(), boss.getY());
        startPlayerPosition= new Coordinates(player.getX(), player.getY());
        startScreenPoisiton= new Coordinates(boxes.getScreenBeginX(), boxes.getScreenBeginY());
        startScrollPosition= new Coordinates(boxes.getScrollBeginX(), boxes.getScrollBeginY());
        boss.startThinking();
    }

    @Override
    public Boss getBoss(){return (Boss) boss;}

    @Override
    public void setListeners(List<LevelListener> listeners){
        this.listeners= listeners;
    }

    @Override
    public void setEvent(LevelChangeEvent lve){
        event = lve;
    }

    public void setLoseImagePath(String loseImagePath)
    {
        this.loseImagePath= loseImagePath;

    }

    public void setNumberEndAnimationImages(String numberEndAnimationImages)
    {
        this.numberEndAnimationImages = Integer.parseUnsignedInt(numberEndAnimationImages);
    }

    /**
     * @see Level3#initBoxes()
     */
    @Override
    protected void initBoxes() {
        Rectangle screen= new Rectangle(10 * tile_width, 30*tile_width, 0,
                20* tile_height);
        Rectangle scrollBox= new Rectangle(858, 950,
                290, 350);
        this.boxes= new DoubleBoxes(screen, scrollBox);

    }

    /**
     * <p>Construit le cycle de jeu.</p>
     */
    private void init() {
        gameLoop = new Thread(() -> {

            boolean firstLoop = true;

            sleep(2000);

            //Tant que le joueur n'est pas sortie
            while (running) {

                if(!firstLoop){
                    gameLose();
                    //On replace l'état du jeu à running dans l'event
                    event.setBooleanTable(LevelState.get(id), true);
                }

                setAll();
                startAnimation();

                before = System.currentTimeMillis();

                //Tant que le joueur n'est pas attrapé ni sortie
                while (!over && running) {
                    loop();

                    //Sert au calcule du temps qu'il a fallut pour arriver là.
                    after = System.currentTimeMillis();
                    sleep();
                    before = System.currentTimeMillis();
                }

                if(firstLoop){
                    firstLoop = false;
                }
            }
            //On place l'état du jeu à fini dans l'event
            event.setBooleanTable(LevelState.get(id), false);

            end();
        });
    }

    /**
     * <p>Replace les constituants à leur état de départ.</p>
     */
    private void setAll() {
        boss.translateTo(startBossPosition);
        player.translateTo(startPlayerPosition);
        boxes.getScreen().setCoordinates(startScreenPoisiton);
        boxes.getScroll().setCoordinates(startScrollPosition);
        over= false;
    }

    /**
     * <p>Opère l'animation de début de niveau.</p>
     */
    private void startAnimation(){
        Coordinates oldScreenPosition= new Coordinates(boxes.getScreenBeginX(), boxes.getScreenBeginY());
        animescreen(0,0, 8);
        animeBoss();
        animescreen(oldScreenPosition.getX(), oldScreenPosition.getY(), 3);

    }

    /**
     * <p>Anime l'ennemi.</p>
     */
    private void animeBoss() {
        while (boss.getY() < 12){
            boss.movesDown();
            fireUpdate();
            sleep(50);
        }
    }

    /**
     * <p>Translate l'écran jusquà l'ennemi.</p>
     * @param x coordonnée que l'écran doit atteindre en axe des X.
     * @param y coordonnée que l'écran doit atteindre en axe des Y.
     * @param speed la vitesse de déplacement de l'écran.
     *             Plus le chiffre est haut moins la vitesse est rapide.
     */
    private void animescreen(int x, int y, long speed) {
        Coordinates vector;
        int xDirection= 0, yDirection= 0;

        if (x != boxes.getScreenBeginX()) {

            xDirection = ((x < boxes.getScreenBeginX()) ? -1 : 1);

        } else if (y != boxes.getScreenBeginY()) {

            yDirection = ((y < boxes.getScreenBeginY()) ? -1 : 1);
        }

        vector = new Coordinates(xDirection, yDirection);

        boxes.getScreen().translate(vector);

        fireUpdate();

        sleep(speed);

        if (!boxes.getScreen().equalsCoordinates(new Coordinates(x, y))) {

            animescreen(x, y, speed);
        }

    }

    /**
     * <p>Remplace l'image de fin par celle qui symbolise la perte du joueur.</p>
     */
    private void gameLose(){
        String tampon= endImageUrl;

        endImageUrl= loseImagePath;

        fireUpdate();

        sleep(2500);

        endImageUrl= tampon;
    }

    /**
     * <p>Démarre le Thread.</p>
     */
    public void start(){
        gameLoop.start();
    }

    /**
     * <p>Crée une pause pour le Thread.</p>
     * @param sleep le temps de pause.
     */
    private void sleep(long sleep){
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ignored) {}
    }

    /**
     * <p>Créer une pause selon un calcule de temps de boucle.</p>
     */
    private void sleep() {
        sleep(fixSleep());
    }

    /**
     * <p>Calcule le temps qu'il a fallut pour une boucle et met en pause le Thread pour
     * étalonner la rapidité d'exécution sur différents processeur.</p>
     * @return le temps qu'il faut pour que un cylcle de boucle égale à SLEEP.
     */
    private long fixSleep() {
        long delta= after - before;

        if(delta < SLEEP)
            return SLEEP - delta;
        else
            return 0;
    }

    /**
     * <p>Succession d'instruction qui incarne le déroulement logique d'une boucle de jeu.</p>
     * On vérifie les collisions entre joueur et boss.
     * On enregistre le déplcement du joueur (si évènement sur une touche).
     * On fais suivre l'écran si besoins.
     * On enregistre déplacement boss.
     * On lance sa phase de réflexion pour trouver un chemin.
     * On informe la vue.
     * Puis on vérifie si le joueur est sortie.
     */
    private synchronized void loop() {

        if (!checkCollision()) {

            Coordinates vector = player.memorizeMoves(map);

            scroll(vector);

            boss.memorizeMoves();

            boss.think();

            fireUpdate();

            checkRunning();
        }
    }

    /**
     * <p>Opère le scrolling.</p>
     * @param vector vecteur de déplacement du joueur.
     */
    private void scroll(Coordinates vector) {
        if (!vector.isZero()){

            if (vector.getX() < 0){

                scrollLeft(vector);

            }else if (vector.getX() > 0){

                scrollRight(vector);

            }else if (vector.getY() < 0){

                scrollUp(vector);

            }else

                scrollDown(vector);
        }
    }

    /**
     * <p>Calcule si le boss touche le joueur.</p>
     * @return true si le boss attrape le joueur, false sinon.
     */
    private boolean checkCollision() {
        if(Rectangle.isTouching(boss.getPosition(), player.getPosition())){
            over= true;
            sleep(50);
            return true;
        }
        return false;
    }

    /**
     * <p>Active l'animation de fin.</p>
     */
    private void end()
    {
        String[] path = endImageUrl.split("0");

        sleep(2000);

        for (int i = numberEndAnimationImages; i > 0; i--)
        {
            endImageUrl = path[0] + i + path[1];

            fireUpdate();

            sleep(50);
        }

        endImageUrl = path[0] + path[1];

        fireUpdate();
    }
    @Override
    public void playerMovesLeft() {
        player.movesLeft();
    }

    @Override
    public void playerMovesRight() {
        player.movesRight();
    }

    @Override
    public void playerMovesUp() {
        player.movesUp();
    }

    @Override
    public void playerMovesDown() {
        player.movesDown();
    }

    /**
     * <p>Préviens les écoutants d'un changement.</p>
     */
    private void fireUpdate(){
        for (LevelListener l:listeners){
            l.update(event);
        }
    }

    /**
     * <p>Teste l'état du jeu (enc ours ou terminé) et ajoute l'état de joueur attrapé.</p>
     * @see AbstractLevel#isRunning()
     * @return true si le joueur est entrain de jouer, false si fin de niveau ou joueur attrapé.
     */
    @Override
    public boolean isRunning(){
        if(running && over){
            return false;
        }
        return running;
    }


    //TODO Remove after Test
    @Override
    public Stack<Coordinates> getPath() {
        return boss.getPath();
    }

}
