package com.antoine.vue.panel;

import java.awt.*;
import javax.swing.JPanel;

import com.antoine.afficheur.AfficheurMiniMap;
import com.antoine.contracts.IAfficheur;
import com.antoine.contracts.Presentateur;
import com.antoine.contracts.LevelListener;
import com.antoine.events.LevelChangeEvent;

/**
 * <b>Classe qui affiche la mini-map du jeu.</b>
 *
 * @author Antoine
 */
public class JMiniMap extends JPanel implements LevelListener {

	/**Le présentateur du modèle*/
	private Presentateur presentateur;

	/**L'afficheur qui se charge d'organiser l'affichage de la mini-map*/
	private IAfficheur afficheurMiniMap;

	//======================    Constructeurs   ========================
	public JMiniMap(Presentateur presentateur) {
		this.presentateur= presentateur;
		this.presentateur.AddListener(this);
		this.afficheurMiniMap= new AfficheurMiniMap();
	}

	public JMiniMap(){

	}

	//==================================================================

	public int getHeight() {return presentateur.getMapHeight() / AfficheurMiniMap.SCALE;}


	/**
	 * @see JPanel#paintComponent(Graphics)
	 * @param g l'instande de Graphics chargée de l'affichage.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawLevel(g);
	}

	/**
	 * <p>Dessine la mini-map.</p>
	 * @param g le graphics fourni.
	 */
	private void drawLevel(Graphics g) {
		int x, y;

		x = (this.getWidth() / 2);
		y = this.getY();

		g.translate(x, y);

		afficheurMiniMap.setGraphics(g);
		presentateur.accept(afficheurMiniMap);
		afficheurMiniMap.freeGraphics();

		g.translate(-x, -y);
	}

	/**
	 * @see LevelListener#update(LevelChangeEvent)
	 * @param lve l'évènement contenant des information pour guider la mise à jour.
	 */
	@Override
	public void update(LevelChangeEvent lve) {
		this.repaint();
	}
}
