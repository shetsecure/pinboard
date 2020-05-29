package pobj.pinboard.document;

import java.io.File;
import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ClipImage extends AbstractClip implements Serializable {

	private Image image;
	private File filename;

	public  ClipImage(double left, double top, File filename){
		super(left,top,0,0,new MyColor(Color.WHITE));
		image = new Image("file://" + filename.getAbsolutePath());
		double bottom = image.getHeight();
		double right = image.getWidth();
		setGeometry(left, top, right, bottom);
		
	}

	public void draw(GraphicsContext ctx) {
		ctx.drawImage(image, getLeft(), getTop());
		
	}

	public ClipImage copy() {
		return new ClipImage(getLeft(), getTop(), filename);
	}
	
	public Image getImage() {
		return image;
	}

}