package com.antoine.vue.frame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.Border;

import com.antoine.contracts.Presentateur;
import com.antoine.manager.niveau.LevelManager;
import com.antoine.vue.ProgressBar;
import com.antoine.vue.listeners.SliderChangeMusicListener;
import com.antoine.vue.listeners.SliderChangeSoundListener;
import com.antoine.vue.panel.*;

/**
 * <b>Classe de fenêtre graphique.</b>
 *
 * @author Antoine
 */
public class Frame extends JFrame {

	/**Le panel d'affichage des niveaux du jeu*/
	private SpecialPanel panel;
	
	public Frame() {
		init();
	}

	/**
	 * <p>Initialise l'ensemble des composants de la fenêtre.</p>
	 */
	private void init(){

		Border lowered, raised, bevel;
		lowered = BorderFactory.createLoweredBevelBorder();
		raised = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
		bevel = BorderFactory.createRaisedBevelBorder();

		Container container = this.getContentPane();

		Presentateur presentateur= new LevelManager();

		ButtonPanel buttons= new ButtonPanel(presentateur);

		JMiniMap miniMapPane = new JMiniMap(presentateur);
		miniMapPane.setBackground(Color.PINK);

		JCardPane panelBas = new JCardPane("boutons", buttons, "miniMap", miniMapPane);
		presentateur.AddListener(panelBas);

		ProgressBar barre = new ProgressBar(0, 6);
		barre.setBorder(bevel);

		presentateur.AddListener(barre);



		JPanel innerGauche = new JPanel(new BorderLayout());

		JPanel innerDroit = new JPanel(new BorderLayout());


		JLabel music = new JLabel("music");
		music.setHorizontalAlignment(JLabel.CENTER);
		music.setVerticalAlignment(JLabel.CENTER);
		music.setBackground(Color.PINK);

		JLabel bruitage = new JLabel("bruitage");
		bruitage.setHorizontalAlignment(JLabel.CENTER);
		bruitage.setVerticalAlignment(JLabel.CENTER);
		bruitage.setBackground(Color.PINK);

		innerGauche.add(music, BorderLayout.SOUTH);

		JSliderPanel musicSlider = new JSliderPanel("/ressources/images/slide/celestiaSlide.png",
				0, 50, (int) (presentateur.getJukebox().getMusicVolume() *100), true);
		musicSlider.addChangeListener(new SliderChangeMusicListener(presentateur.getJukebox()));


		JSliderPanel soundSlider = new JSliderPanel("/ressources/images/slide/lunaSlide.png",
				0, 50, (int) (presentateur.getJukebox().getSoundVolume() *100), true);
		soundSlider.addChangeListener(new SliderChangeSoundListener(presentateur.getJukebox()));


		innerGauche.add(musicSlider, BorderLayout.CENTER);


		innerGauche.setBackground(Color.PINK);

		innerDroit.add(soundSlider, BorderLayout.CENTER);
		innerDroit.add(bruitage, BorderLayout.SOUTH);
		innerDroit.setBackground(Color.PINK);

		panelBas.setBorder(raised);

		panel= new SpecialPanel(presentateur);
		panel.setBorder(lowered);
		container.setLayout(new BorderLayout());
		container.add(barre, BorderLayout.NORTH);
		container.add(panel, BorderLayout.CENTER);
		container.add(panelBas, BorderLayout.SOUTH);

		container.add(innerGauche, BorderLayout.WEST);
		container.add(innerDroit, BorderLayout.EAST);

		this.addKeyListener(new InternImageListener());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setTitle("My Little Pony - Le Labyrinthe de Discord -");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * <b>Classe de listener interne.</b>
	 * Définit les actions selon les touches préssées par le joueur.
	 * Les quatres touches directionnelles.
	 */
	private class InternImageListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent keyEvent) {

		}

		@Override
		public void keyPressed(KeyEvent e) {

			this.moves(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if ((isKeyRight(e) || isKeyLeft(e)||
					isKeyUp(e) || isKeyDown(e)))
				panel.playerMovesReleased();
		}

		private void moves(KeyEvent e) {
			if (isKeyRight(e))
				panel.playerMovesRight();

			else if (isKeyLeft(e)) {
				panel.playerMovesLeft();

			} else if (isKeyUp(e))
				panel.playerMovesUp();

			else if (isKeyDown(e))
				panel.playerMovesDown();

		}

		private boolean isKeyLeft(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_LEFT;
		}

		private boolean isKeyRight(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_RIGHT;
		}

		private boolean isKeyUp(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_UP;
		}

		private boolean isKeyDown(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_DOWN;
		}
		
	}
	
}
