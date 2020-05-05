package pobj.pinboard.document;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipGroup extends AbstractClip implements Composite {
	
	private List<Clip> clips;
	
	public ClipGroup(double left, double top, double right, double bottom, MyColor color) {
		super(left, top, right, bottom, color);
		clips = new ArrayList<Clip>();
	}
	
	public ClipGroup() {
		this(0,0,0,0, new MyColor(Color.BLUE));
	}

	@Override
	public void draw(GraphicsContext ctx) {
		for (Clip c : clips)
			c.draw(ctx);
	}

	@Override
	public void setColor(Color c) {
		for (Clip clip : clips)
			clip.setColor(c);
	}
	
	@Override
	public Clip copy() {
		ClipGroup grp_copy = new ClipGroup(getLeft(), getTop(), getRight(), getBottom(), getMyColor());
		
		for (Clip c : clips)
			grp_copy.addClip(c.copy());
		
		return grp_copy;
	}

	@Override
	public List<Clip> getClips() {
		return clips;
	}

	@Override
	public void addClip(Clip toAdd) {
		clips.add(toAdd);
		updateCoords();
	}

	@Override
	public void removeClip(Clip toRemove) {
		if (clips.contains((toRemove)))
				clips.remove(toRemove);
		
		if (clips.size() > 0)
			updateCoords();
	}
	
	@Override
	public void move(double x, double y) {
		for (Clip c : clips)
			c.move(x, y);
		
		updateCoords();
	}
	
	public void destroy() {
		clips.clear();
		setGeometry(0, 0, 0, 0);
	}
	
	private void updateCoords() {
		double left, right, top, bottom;
		left = clips.get(0).getLeft();
		right = clips.get(0).getRight();
		top = clips.get(0).getTop();
		bottom = clips.get(0).getBottom();
		
		if (clips.size() > 1) {	
			for (int i = 1; i < clips.size(); i++) {
				if (clips.get(i).getLeft() < left)
					left = clips.get(i).getLeft();
			
				if (clips.get(i).getRight() > right)
					right = clips.get(i).getRight();

				if (clips.get(i).getTop() < top)
					top = clips.get(i).getTop();

				if (clips.get(i).getBottom() > bottom)
					bottom = clips.get(i).getBottom();
			}
		}
		
		setGeometry(left, top, right, bottom);
	}

}
