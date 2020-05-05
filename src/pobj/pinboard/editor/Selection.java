package pobj.pinboard.editor;

import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Selection {
	List<Clip> selected_clips = new ArrayList<Clip>();
	
	public void select(Board board, double x, double y) {
		selected_clips.clear();
		
		for (Clip c : board.getContents()) {
			if (c.isSelected(x, y)) {
				selected_clips.add(c);
				
				// le premier élément de la planche
				break;
			}
		}
	}
	
	public void toggleSelect(Board board, double x, double y) {
		for (Clip c : board.getContents()) {
			if (c.isSelected(x, y)) {
				if (selected_clips.contains(c))
					selected_clips.remove(c);
				else
					selected_clips.add(c);
				
				// le premier élément de la planche
				break;
			}
		}
	}
	
	public void toogleSelect(Board board, double x, double y) {
		// to pass the test
		toggleSelect(board, x, y);
	}
	
	public void clear() {
		selected_clips.clear();
	}
	
	public List<Clip> getContents() {
		return selected_clips;
	}
	
	public void drawFeedback(GraphicsContext gc) {
		gc.setStroke(Color.BLUE);
		for (Clip c : selected_clips)
			gc.strokeRect(Math.min(c.getLeft(), c.getRight()), 
						  Math.min(c.getBottom(), c.getTop()),
						  Math.abs(c.getRight() - c.getLeft()),
						  Math.abs(c.getTop() - c.getBottom()));
	}
}
