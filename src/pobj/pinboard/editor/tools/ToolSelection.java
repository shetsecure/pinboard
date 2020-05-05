package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.Selection;

public class ToolSelection implements Tool {
	private double x,y;
	
	@Override
	public void press(EditorInterface i, MouseEvent e) {
		Selection selection = i.getSelection();
		x = e.getX();
		y = e.getY();
		
		if (e.isShiftDown())
			selection.toggleSelect(i.getBoard(), x, y);
		else
			selection.select(i.getBoard(), x, y);
		
	}

	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		for (Clip c : i.getSelection().getContents())
			c.move(e.getX() - x, e.getY() - y);
		
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		i.getSelection().drawFeedback(gc);
		
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Selection Tool";
	}

}
