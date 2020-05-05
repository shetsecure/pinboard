package pobj.pinboard.editor;

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
import javafx.stage.Stage;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.tools.Tool;
import pobj.pinboard.editor.tools.ToolEllipse;
import pobj.pinboard.editor.tools.ToolNull;
import pobj.pinboard.editor.tools.ToolRect;
import pobj.pinboard.editor.tools.ToolSelection;

public class EditorWindow implements EditorInterface, ClipboardListener  {
	private Board board = new Board();
	private Canvas canvas;
	private Label label;
	private MenuItem edit_paste_item;
	private ColorPicker colorPicker = new ColorPicker();
	private ToggleButton change_color_button;
	
	private Tool current_tool = new ToolNull();
	private Color current_color = Color.BLUE;
	
	
	private Selection selection = new Selection();
	
	public EditorWindow(Stage stage) {
		stage.setTitle("Test Title");
		
		// Menus
		
		// File Menu
		Menu file_menu = new Menu("File");
		MenuItem file_new = new MenuItem("New");
		MenuItem file_close = new MenuItem("Close");
		
		file_new.setOnAction( (e) -> new EditorWindow(new Stage()));
		file_close.setOnAction( (e) -> { Clipboard.getInstance().removeListener(this); stage.close(); });
		
		file_menu.getItems().add(file_new);
		file_menu.getItems().add(file_close);
		
		// Edit menu ( copy, paste and delete )
		Menu edit_menu = new Menu("Edit");
		MenuItem edit_copy_item = new MenuItem("Copy");
		edit_copy_item.setOnAction(e -> Clipboard.getInstance().copyToClipboard(selection.getContents()));
		
		edit_paste_item = new MenuItem("Paste");
		edit_paste_item.setOnAction(e -> { board.addClip(Clipboard.getInstance().copyFromClipboard()); update(); });
		
		MenuItem edit_delete_item = new MenuItem("Delete");
		edit_delete_item.setOnAction(e -> { board.removeClip(selection.getContents()); update(); });
		
		edit_menu.getItems().addAll(edit_copy_item, edit_paste_item, edit_delete_item);
		
		
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
//		change_color_button.setOnAction((e) -> {
//			System.out.println("brkna 3la change color. Ha ch7al kayn f selection");
//			System.out.println(selection.getContents().size());
//			updateColorOfSelectedClips();
//		});
		
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
		// TODO Auto-generated method stub
		return null;
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
