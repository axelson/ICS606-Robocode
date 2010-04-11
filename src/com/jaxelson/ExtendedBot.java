package com.jaxelson;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import robocode.AdvancedRobot;

public class ExtendedBot extends AdvancedRobot {

    public void drawCenteredCircle(Graphics2D g, Integer radius) {
    	double x = getX() - radius;
        double y = getY() - radius;
		Shape circle = new Ellipse2D.Double(x, y, radius*2, radius*2);
        g.draw(circle);
    }
    
    public Double getDistanceToRightWall() {
    	double botRightEdge = getX();
    	double rightWallLocation = getBattleFieldWidth();
    	return rightWallLocation - botRightEdge;
    }
}
