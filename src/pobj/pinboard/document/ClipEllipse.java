package pobj.pinboard.document;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipEllipse extends AbstractClip {
	
	public ClipEllipse(double left,double top,double right,double bottom, MyColor color) {
		super(left, top, right, bottom, color);
	}
	
	public ClipEllipse(double left,double top,double right,double bottom, Color color) {
		super(left, top, right, bottom, color);
	}
	
	@Override
	public boolean isSelected(double x, double y) {
		double cx = (getLeft() + getRight()) / 2;
		double cy = (getBottom() + getTop()) / 2;
		double rx = (getRight() - getLeft()) / 2;
		double ry = (getBottom() - getTop()) / 2;
		
		double f = ((x - cx) / rx) * ((x - cx) / rx);
		f += ((y -cy) / ry) * ((y -cy) / ry);
				
		return f <= 1;
	}

	@Override
	public void draw(GraphicsContext ctx) {
		ctx.setFill(getColor());
		ctx.fillOval(getLeft(), getTop(), getRight() - getLeft(), getBottom() - getTop());
	}

	@Override
	public Clip copy() {
		return new ClipEllipse(getLeft(), getTop(), getRight(), getBottom(), getMyColor());
	}

}
