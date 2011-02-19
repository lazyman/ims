package org.tingoo.client.datapick;

import java.awt.Point;

import javax.swing.JLabel;

public class DayLabel extends JLabel {
	private Point gridLocation = new Point();
	
	public DayLabel() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
	}

	public Point getGridLocation() {
		return gridLocation;
	}

	public void setGridLocation(Point point) {
		this.gridLocation = point;
	}
}
