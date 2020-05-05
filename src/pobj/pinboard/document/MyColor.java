package pobj.pinboard.document;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class MyColor implements Serializable {
	private final double r, g, b;
	
	public MyColor(Color color) {
	        this.r = color.getRed();
	        this.g = color.getGreen();
	        this.b = color.getBlue();
    }
	
	public MyColor(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public double getRed() {
		return r;
	}
	
	public double getGreen() {
		return g;
	}

	public double getBlue() {
		return b;
	}
	
	public Color getFXColor() {
		return new Color(r, g, b, 1);
	}
}
