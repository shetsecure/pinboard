package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandMove extends AbstractCommand implements Command {
	private double x,y;
	
	public CommandMove(EditorInterface editor, Clip toAdd, double x, double y) {
		super(editor, toAdd);
		this.x = x;
		this.y = y;
	}
	
	public CommandMove(EditorInterface editor, List<Clip> toAdd, double x, double y) {
		super(editor, toAdd);
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute() {		
		for (Clip c : clips)
			c.move(x, y);
	}

	@Override
	public void undo() {
		for (Clip c : clips)
			c.move(-x, -y);
	}

}