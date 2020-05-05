package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public abstract class AbstractCommand {
	protected List<Clip> clips = new ArrayList<Clip>();
	protected EditorInterface editor;
	
	protected AbstractCommand() {}
	
	protected AbstractCommand(EditorInterface editor, Clip toAdd) {
		this.editor = editor;
		clips.add(toAdd);
	}
	
	protected AbstractCommand(EditorInterface editor, List<Clip> toAdd) {
		this.editor = editor;
		clips.addAll(toAdd);		
	}
}
