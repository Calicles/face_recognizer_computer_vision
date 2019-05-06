package com.antoine.vue.Test;

import com.antoine.afficheur.AfficheurLevel;
import com.antoine.contracts.IStructure;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;

import java.awt.*;
import java.util.Stack;

public class AfficheurLevelForTest extends AfficheurLevel implements com.antoine.contracts.IAfficheur {

    @Override
    protected void drawLevel4(IStructure levelToTest) {
        super.drawLevel4(levelToTest);

        Rectangle screen = levelToTest.getScreen();

        g.translate(-screen.getBeginX(), -screen.getBeginY());

        Stack<Coordinates> path = levelToTest.getPath();
        Coordinates current, next;

        Color old = g.getColor();

        current = Rectangle.findMiddleCoor(levelToTest.getBoss().getPosition());

        g.drawOval(current.getX() - 300, current.getY() - 300, 600, 600);
        if (!path.isEmpty()) {
            next = path.pop();

            g.setColor(Color.RED);

            g.drawLine(current.getX(), current.getY(), next.getX(), next.getY());

            current = next;
        }


        g.setColor(Color.YELLOW);

        while (!path.isEmpty()) {

            next = path.pop();

            g.drawLine(current.getX(), current.getY(), next.getX(), next.getY());

            current = next;

        }

        g.translate(screen.getBeginX(), screen.getBeginY());

        g.setColor(old);
    }
    @Override
    public void visit(IStructure structure) {
        super .visit(structure);
    }

    @Override
    public void setGraphics(Graphics g) {
        super.setGraphics(g);
    }

    @Override
    public void freeGraphics() {
        super.freeGraphics();
    }
}
