package pobj.pinboard.editor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pobj.pinboard.document.Board;
import pobj.pinboard.editor.tools.Tool;
import pobj.pinboard.editor.tools.ToolEllipse;
import pobj.pinboard.editor.tools.ToolNull;
import pobj.pinboard.editor.tools.ToolRect;

public class EditorWindow implements EditorInterface {
	Board board;
	Canvas canvas;
	Label label;
	Tool current_tool = new ToolNull();
	
	public EditorWindow(Stage stage) {
		board = new Board();
		stage.setTitle("Test Title");
		
		// Menus
		
		// File Menu
		Menu file_menu = new Menu("File");
		MenuItem file_new = new MenuItem("New");
		MenuItem file_close = new MenuItem("Close");
		
		file_new.setOnAction( (e) -> new EditorWindow(new Stage()));
		file_close.setOnAction( (e) -> stage.close());
		
		file_menu.getItems().add(file_new);
		file_menu.getItems().add(file_close);
		
		
		Menu edit_menu = new Menu("Edit");
		Menu tools_menu = new Menu("Tools");
		
		// MenuBar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(file_menu);
		menuBar.getMenus().add(edit_menu);
		menuBar.getMenus().add(tools_menu);
		
		// Label
		label = new Label("Label");
		
		// buttons in the ToolBar
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
		toolBar.getItems().add(box_button);
		toolBar.getItems().add(ellipse_button);
		toolBar.getItems().add(img_button);
		
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
		
		// creating the Scene
		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.show();
	}
	
	private void update() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		board.draw(gc);
		current_tool.drawFeedback(this, gc);
		label.setText(current_tool.getName(this));
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public Selection getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandStack getUndoStack() {
		// TODO Auto-generated method stub
		return null;
	}
}
