package pobj.pinboard.document;

import java.io.Serializable;

import javafx.scene.paint.Color;

public abstract class AbstractClip implements Clip, Serializable {
	private double left, top, right, bottom;
	private MyColor color;
	
	protected AbstractClip (double left,double top,double right,double bottom, MyColor color) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.color = color;
	}
	
	protected AbstractClip (double left,double top,double right,double bottom, Color color) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.color = new MyColor(color);
	}
	
	public double getTop() {
		return top;
	}

	public double getLeft() {
		return left;
	}

	public double getBottom() {
		return bottom;
	}

	public double getRight() {
		return right;
	}
	
	public void setGeometry(double left, double top, double right, double bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	public void move(double x, double y) {
		left += x;
		right += x;
		top += y;
		bottom += y;
	}

	public boolean isSelected(double x, double y) {
		return x >= left && x <= right && y >= top && y <= bottom;
	}

	public void setColor(MyColor c) {
		this.color = c;
	}
	
	public MyColor getMyColor() {
		return color;
	}
	
	public void setColor(Color c) {
		this.color = new MyColor(c.getRed(), c.getGreen(), c.getBlue());
	}

	public Color getColor() {
		return color.getFXColor();
	}
}
