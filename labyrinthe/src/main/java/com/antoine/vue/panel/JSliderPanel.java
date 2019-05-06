package com.antoine.vue.panel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/**
 * <b>Classe de slider customisé avec une image.</b>
 *
 * @author Antoine
 */
public  class JSliderPanel extends JSlider {

    /**L'image personnalisée pour le slide*/
    Icon knobImage;

    public JSliderPanel(String iconPath, int minValue, int maxValue, int value, boolean tickPaintable){
        super(SwingConstants.VERTICAL, minValue, maxValue, (maxValue / 2));
        setBackground(Color.PINK);
        super.setMajorTickSpacing(10);
        super.setMinorTickSpacing(1);
        super.setValue(value);

        knobImage = new ImageIcon(getClass().getResource(iconPath));
        setUI(new ThumbIconSliderUI(this));

        super.setPaintTicks(tickPaintable);
        super.setPaintLabels(tickPaintable);
        super.setPaintTrack(true);
        super.setFocusable(false);
    }

    /**
     * @see JSliderPanel#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize(){

        return super.getPreferredSize();
    }


    /**
     * <b>Classe interne au slider personnaliée pour gérer l'UI qui affiche le slide.</b>
     *
     * @author Antoine
     */
    private class ThumbIconSliderUI extends BasicSliderUI {

        /**
         * <p>Fournit le slider.</p>
         * @param aSlider le slider.
         */
        public ThumbIconSliderUI( JSlider aSlider )
        {
            super( aSlider );
        }

        /**
         * <p>Peint l'image personnaliée sur le slider.</p>
         * @see BasicSliderUI#paintThumb(Graphics)
         * @param g graphics qui produit l'affichage.
         */
        @Override
        public void paintThumb(Graphics g)
        {
            Rectangle knobRect = thumbRect;
            g.translate(knobRect.x, knobRect.y);

            knobImage.paintIcon(slider, g, 0, 0);

            g.translate(-knobRect.x, -knobRect.y);
        }

        /**
         * <p>Adapte la taille du slider à l'image personnalisée.</p>
         * @see BasicSliderUI#getThumbSize()
         * @return les dimensions du slider adaptée à l'image personnalisée.
         */
        @Override
        protected Dimension getThumbSize()
        {
            return new Dimension(knobImage.getIconWidth(), knobImage.getIconHeight());
        }

    }
}
