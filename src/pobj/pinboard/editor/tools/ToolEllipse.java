package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pobj.pinboard.document.ClipEllipse;
import pobj.pinboard.editor.EditorInterface;

public class ToolEllipse implements Tool {
	private double x1,y1, x2,y2;

	@Override
	public void press(EditorInterface i, MouseEvent e) { 
		//if (e.getButton() == MouseButton.PRIMARY) {
			x1 = x2 = e.getX();
			y1 = y2 = e.getY();
		//}
	}

	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		//if (e.getButton() == MouseButton.PRIMARY) {
			x2 = e.getX();
			y2 = e.getY();
		//}
	}

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		ClipEllipse clip = new ClipEllipse(Math.min(x1, x2), Math.min(y1, y2), 
				Math.max(x1, x2), Math.max(y1, y2), i.getCurrentColor());

		i.getBoard().addClip(clip);
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		gc.setStroke(Color.DARKRED);
		gc.strokeRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2-x1), Math.abs(y2-y1));
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Filled Ellipse Tool";
	}

}
