package com.antoine.vue.panel;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.antoine.afficheur.AfficheurLevel;
import com.antoine.contracts.IAfficheur;
import com.antoine.contracts.IJeu;
import com.antoine.contracts.Presentateur;
import com.antoine.contracts.LevelListener;
import com.antoine.events.LevelChangeEvent;

/**
 * <b>Panel qui est chargé d'afficher les niveaux.</b>
 *
 * @author Antoine
 */
public class SpecialPanel extends JPanel implements LevelListener {

	/**Présentateur du modèl*/
	private Presentateur presentateur;

	/**L'afficheur qui organise l'affichage du niveau en cours*/
	private IAfficheur afficheur;


	//==================    Constructeurs   ==================
	public SpecialPanel(Presentateur model) {
		this.presentateur= model;
		this.presentateur.AddListener(this);
		this.afficheur= new AfficheurLevel();
	}

	public SpecialPanel(){ }

	//========================================================

	/**
	 * @see JPanel#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return presentateur.getDimension();
	}

	/**
	 * @see JPanel#paintComponent(Graphics)
	 * @param g graphics chargé de l'affichage.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		afficheur.setGraphics(g);
		presentateur.accept(afficheur);
		afficheur.freeGraphics();
	}

	/**
	 * @see LevelListener#update(LevelChangeEvent)
	 * @param lve l'évènement contenant des information pour guider la mise à jour.
	 */
	@Override
	public void update(LevelChangeEvent lve) {
		this.repaint();
	}

	/**
	 * @see IJeu#playerMovesLeft()
	 */
	public void playerMovesLeft(){
		presentateur.playerMovesLeft();
	}

	/**
	 * @see IJeu#playerMovesRight()
	 */
	public void playerMovesRight(){
		presentateur.playerMovesRight();
	}

	/**
	 * @see IJeu#playerMovesUp()
	 */
	public void playerMovesUp(){
		presentateur.playerMovesUp();
	}

	/**
	 * @see IJeu#playerMovesDown()
	 */
	public void playerMovesDown(){
		presentateur.playerMovesDown();
	}

	/**
	 * @see IJeu#playerMovesReleased()
	 */
	public void playerMovesReleased(){
		presentateur.playerMovesReleased();
	}
}
