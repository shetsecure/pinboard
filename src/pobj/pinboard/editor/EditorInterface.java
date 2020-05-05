package pobj.pinboard.editor;

import javafx.scene.paint.Color;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.ClipGroup;

public interface EditorInterface {
	public Board  getBoard();
	public Selection getSelection();
	public CommandStack getUndoStack();
	public Color getCurrentColor();
}
