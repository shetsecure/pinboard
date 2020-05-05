package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;

public class Clipboard {
	private static Clipboard instance;
	private List<Clip> contents = new ArrayList<Clip>();
	private List<ClipboardListener> observers = new ArrayList<ClipboardListener>();
	
	private Clipboard() {}
	
	public static Clipboard getInstance() {
		
		synchronized (Clipboard.class) {
			if (instance == null)
				instance = new Clipboard();	
		}
		
		return instance;
	}
	
	public void copyToClipboard(List<Clip> clips) {
		contents.clear();
		// deep copy
		for (Clip c : clips)
			contents.add(c.copy());
		
		notifyObservers();
	}
	
	public List<Clip> copyFromClipboard() {
		// deep copy
		List<Clip> copy = new ArrayList<Clip>();
		
		for (Clip c : contents)
			copy.add(c.copy());
		
		return copy;
	}
	
	public void clear() {
		contents.clear();
		notifyObservers();
	}
	
	public boolean isEmpty() {
		return contents.size() == 0;
	}
	
	public void addListener(ClipboardListener listener) {
		observers.add(listener);
	}
	
	public void removeListener(ClipboardListener listener) {
		if (observers.contains(listener))
			observers.remove(listener);
	}
	
	private void notifyObservers() {
		for (ClipboardListener observer : observers)
			observer.clipboardChanged();
	}
}
