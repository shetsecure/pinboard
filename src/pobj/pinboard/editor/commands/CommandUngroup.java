package pobj.pinboard.editor.commands;

import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

public class CommandUngroup extends AbstractCommand implements Command {
	private ClipGroup clipgroup;
	
	public CommandUngroup(EditorInterface editor, ClipGroup group) {
		this.editor = editor;
		clipgroup = group;
	}

	@Override
	public void execute() {
		// remove the grouped clips (ClipGroup) from the board, and add the clips as independent.
		
//		for (Clip c : clipgroup.getClips()) {
//			if (c instanceof ClipGroup)
//				((ClipGroup) c).destroy();
//			else
//				clipgroup.removeClip(c); // not sure if we will ever hit this case
//		}
//	
//		editor.getBoard().addClip(clipgroup);
		
		for (Clip c : clipgroup.getClips()) 
			editor.getBoard().addClip(c);
		
		editor.getBoard().removeClip(clipgroup);
	}

	@Override
	public void undo() {
		// remove clips from the board and add a Group that groups the former independent clips.
		
		for (Clip c : clipgroup.getClips()) 
			editor.getBoard().removeClip(c);
		
		editor.getBoard().addClip(clipgroup);
		
	}
}
