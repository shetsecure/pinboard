package pobj.pinboard.editor.commands;

import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandAdd extends AbstractCommand implements Command {
	
	public CommandAdd(EditorInterface editor, Clip toAdd) {
		super(editor, toAdd);
	}
	
	public CommandAdd(EditorInterface editor, List<Clip> toAdd) {
		super(editor, toAdd);		
	}

	@Override
	public void execute() {
		editor.getBoard().addClip(clips);
	}

	@Override
	public void undo() {
		editor.getBoard().removeClip(clips);
	}

}
