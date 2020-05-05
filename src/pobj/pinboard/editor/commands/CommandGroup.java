package pobj.pinboard.editor.commands;

import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.EditorWindow;

public class CommandGroup extends AbstractCommand implements Command {
	private ClipGroup clipgroup = new ClipGroup();
	
	public CommandGroup(EditorInterface editor, Clip toAdd) {
		super(editor, toAdd);
	}
	
	public CommandGroup(EditorInterface editor, List<Clip> toAdd) {
		super(editor, toAdd);		
	}

	@Override
	public void execute() {
		clipgroup = new ClipGroup();
		
		// remove clips from the board and add a Group that groups the former independent clips.
		for (Clip c : clips) {
			clipgroup.addClip(c);
			editor.getBoard().removeClip(c);
		}
		
		editor.getBoard().addClip(clipgroup);
//		((EditorWindow) editor).setClipGroup(clipgroup);
	}

	@Override
	public void undo() {
		
		// remove the grouped clips from the board, and add the clips as independent.
		
		for (Clip c : clips) {
			//clipgroup.removeClip(c); 
			editor.getBoard().addClip(c);
		}
		
		editor.getBoard().removeClip(clipgroup);
	}

}
