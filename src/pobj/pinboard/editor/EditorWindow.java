package pobj.pinboard.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.commands.CommandGroup;
import pobj.pinboard.editor.commands.CommandUngroup;
import pobj.pinboard.editor.tools.Tool;
import pobj.pinboard.editor.tools.ToolEllipse;
import pobj.pinboard.editor.tools.ToolNull;
import pobj.pinboard.editor.tools.ToolRect;
import pobj.pinboard.editor.tools.ToolSelection;

public class EditorWindow implements EditorInterface, ClipboardListener {
	private Board board = new Board();
	private Canvas canvas;
	private Label label;
	private MenuItem edit_paste_item;
	private ColorPicker colorPicker = new ColorPicker();
	private ToggleButton change_color_button;
	
	private Tool current_tool = new ToolNull();
	private Color current_color = Color.BLUE;
	private ClipGroup clipgroup = new ClipGroup();
	private CommandStack undoStack = new CommandStack();
	
	private FileChooser file_chooser = new FileChooser();
	private Stage stage;
	private Selection selection = new Selection();
	
	public EditorWindow(Stage stage) {
		this.stage = stage;
		stage.setTitle("Test Title");
		
		// Menus
		
		// File Menu
		Menu file_menu = new Menu("File");
		MenuItem file_new = new MenuItem("New");
		MenuItem file_open = new MenuItem("Open / Load");
		MenuItem file_save = new MenuItem("Save");
		MenuItem file_close = new MenuItem("Close");

		file_new.setOnAction( e -> new EditorWindow(new Stage()));
		file_open.setOnAction( e -> { open_board_from_file(); update(); });
		file_save.setOnAction( e -> save_board_to_file());
		file_close.setOnAction( e -> { Clipboard.getInstance().removeListener(this); stage.close(); });

		file_menu.getItems().addAll(file_new, file_open, file_save, file_close);

		// Edit menu ( copy, paste and delete )

		Menu edit_menu = new Menu("Edit");
		MenuItem edit_copy_item = new MenuItem("Copy");
		edit_copy_item.setOnAction(e -> Clipboard.getInstance().copyToClipboard(selection.getContents()));
		edit_paste_item = new MenuItem("Paste");
		edit_paste_item.setOnAction(e -> { board.addClip(Clipboard.getInstance().copyFromClipboard()); update(); });
		MenuItem edit_delete_item = new MenuItem("Delete");
		edit_delete_item.setOnAction(e -> { deleteSelectedClips(); update(); });
		
		
		MenuItem edit_group_item = new MenuItem("Group");
		edit_group_item.setOnAction(e -> { groupClips(); update(); });
		MenuItem edit_ungroup_item = new MenuItem("Ungroup");
		edit_ungroup_item.setOnAction(e -> { ungroupClips(); update(); });
		
		
		MenuItem edit_undo_item = new MenuItem("Undo");
		edit_undo_item.setOnAction(e -> { undoStack.undo(); update(); });
		MenuItem edit_redo_item = new MenuItem("Redo");
		edit_redo_item.setOnAction(e -> { undoStack.redo(); update(); });
		
		edit_menu.getItems().addAll(edit_copy_item, edit_paste_item, edit_delete_item, edit_group_item,
									edit_ungroup_item, edit_undo_item, edit_redo_item);
		
		
		// Tools
		
		Menu tools_menu = new Menu("Tools");
		
		// MenuBar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(file_menu, edit_menu, tools_menu);
		
		// Label
		label = new Label("Label");
		
		// colorPicker in the ToolBar
		colorPicker.setOnAction((e) -> { current_color = colorPicker.getValue(); update();});
		
		// buttons in the ToolBar
		Button selection_button = new Button("Select");
		selection_button.setOnAction((e) -> {
		    current_tool = new ToolSelection();
		});
		
		change_color_button = new ToggleButton("Change Color");
		
		Button box_button = new Button("Box");
		box_button.setOnAction((e) -> {
		    current_tool = new ToolRect();
		});
		Button ellipse_button = new Button("Ellipse");
		ellipse_button.setOnAction((e) -> {
	        current_tool = new ToolEllipse(); 
		});
		Button img_button = new Button("Img...");
		img_button.setOnAction((e) ->  {
		        label.setText("Image Tool");
		});
		
		// toolBar
		ToolBar toolBar = new ToolBar();
		colorPicker.setValue(Color.BLUE);
		toolBar.getItems().addAll(selection_button, colorPicker, change_color_button, box_button, ellipse_button, img_button);
		
		// Canvas 
		canvas = new Canvas(500,500);
		canvas.setOnMousePressed(e -> { current_tool.press(this, e); update(); });
		canvas.setOnMouseDragged(e -> { current_tool.drag(this, e); update(); });
		canvas.setOnMouseReleased(e -> { current_tool.release(this, e); update(); });
		
		// Separator
		Separator separator = new Separator();		
		
		// grouping all of the above in a VBox
		
		VBox vBox = new VBox();
		vBox.getChildren().add(menuBar);
		vBox.getChildren().add(toolBar);
		vBox.getChildren().add(canvas);
		vBox.getChildren().add(separator);
		vBox.getChildren().add(label);
		
		// paste button should be disabled at first
		edit_paste_item.setDisable(true);
		
		// creating the Scene
		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.show();
		
		// subscribing to ClipBoard
		Clipboard.getInstance().addListener(this);
	}
	
	private void save_board_to_file() {
		file_chooser.setTitle("Save the current board. Choose destination.");
		file_chooser.getExtensionFilters().addAll(new ExtensionFilter("Board Files", "*.board"));
		File selectedFile = file_chooser.showSaveDialog(this.stage);
		
		if (selectedFile != null) {
			ObjectOutputStream objectOutputStream;
		    try {
		    	objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile.getAbsolutePath() + ".board"));
		    	objectOutputStream.writeObject(this.board);
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void open_board_from_file() {
		file_chooser.setTitle("Load a board. Choose a file");
		file_chooser.getExtensionFilters().addAll(new ExtensionFilter("Board Files", "*.board"));
		File selectedFile = file_chooser.showOpenDialog(this.stage);
		
		if (selectedFile != null) {
			ObjectInputStream objectInputStream;
			try {
				objectInputStream = new ObjectInputStream(new FileInputStream(selectedFile.getAbsolutePath()));
				Board loaded_board = (Board) objectInputStream.readObject();
				objectInputStream.close();
				
				// remove the current board
				this.board.getContents().clear();
				
				this.board = loaded_board;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void deleteSelectedClips() {
		List<Clip> to_remove = selection.getContents();
		
		for (Clip c : to_remove) {
			if (c instanceof ClipGroup) {
				board.removeClip(((ClipGroup) c).getClips());
				((ClipGroup) c).destroy();
			}
			
			board.removeClip(c);
		}
		
	}
	
	public void setClipGroup(ClipGroup c) {
		clipgroup = c;
	}

	private void ungroupClips() {
		for (Clip c : selection.getContents())
			if (c instanceof ClipGroup) {
				clipgroup = (ClipGroup) c;
				break;
			}
			
		CommandUngroup group_cmd = new CommandUngroup(this, clipgroup);
		group_cmd.execute();
		undoStack.addCommand(group_cmd);
		
//		for (Clip c : selection.getContents()) {
//			if (c instanceof ClipGroup)
//				((ClipGroup) c).destroy();
//			else
//				clipgroup.removeClip(c); // not sure if we will ever hit this case
//		}
//		
//		board.addClip(clipgroup);
	}

	private void groupClips() {
		CommandGroup group_cmd = new CommandGroup(this, selection.getContents());
		group_cmd.execute();
		undoStack.addCommand(group_cmd);
		
//		clipgroup = new ClipGroup();
//		
//		for (Clip c : selection.getContents())
//			clipgroup.addClip(c);
//		
//		board.addClip(clipgroup);
	}

	private void update() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		updateColorOfSelectedClips();
		board.draw(gc);
		current_tool.drawFeedback(this, gc);
		label.setText(current_tool.getName(this));
	}
	
	private void updateColorOfSelectedClips() {
		if (change_color_button.isSelected())
			for (Clip c : selection.getContents()) 
				c.setColor(current_color);
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public Selection getSelection() {
		return selection;
	}

	@Override
	public CommandStack getUndoStack() {
		return undoStack;
	}

	@Override
	public void clipboardChanged() {
		edit_paste_item.setDisable(Clipboard.getInstance().isEmpty());
	}

	@Override
	public Color getCurrentColor() {
		return current_color;
	}
}
