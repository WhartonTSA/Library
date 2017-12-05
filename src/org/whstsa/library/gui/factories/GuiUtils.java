package org.whstsa.library.gui.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.gui.api.InputGroup;
import org.whstsa.library.util.ClickHandler;
import org.whstsa.library.util.Logger;

import java.util.List;

public class GuiUtils {
	
	public static final int COMPONENT_PADDING = 16;
	
	public static final Font TITLE_FONT = new Font(30.0);
	
	public static StackPane createSplitPane(Orientation orientation, Node ...nodes) {
		LibraryDB.LOGGER.debug("Assembling " + orientation.name + " StackPane with " + nodes.length + " nodes.");
		Pane container;
		switch (orientation) {
		case VERTICAL:
			container = new VBox(COMPONENT_PADDING, nodes);
			((VBox) container).setAlignment(Pos.CENTER);
			break;
		case HORIZONTAL:
			container = new HBox(COMPONENT_PADDING, nodes);
			((HBox) container).setAlignment(Pos.CENTER);
			break;
		default:
			LibraryDB.LOGGER.warn("Unknown orientation " + orientation.name + " - defaulting to horizontal.");
			container = new HBox(COMPONENT_PADDING, nodes);
			((HBox) container).setAlignment(Pos.CENTER);
			break;
		}
		return new StackPane(container);
	}
	
	public static StackPane createTitledSplitPane(String title, Orientation orientation, Node ...nodes) {
		StackPane pane = createSplitPane(orientation, nodes);
		Label label = createTitle(title);
		StackPane.setAlignment(label, Pos.TOP_CENTER);
		StackPane root = createSplitPane(Orientation.VERTICAL, pane);
		root.getChildren().add(label);
		return root;
	}
	
	public static Scene createScene(Parent parent) {
		return new Scene(parent, 512, 512);
	}
	
	// 1 4 7 10 13 16 19
	// 17 20 21 42 45 69 72 25 39
	
	public static Button createButton(String title, ClickHandler clickHandler) {
		LibraryDB.LOGGER.debug("Assembling button with title " + title + " (CLICK HANDLER: " + Logger.assertion(clickHandler != null) + ")");
		Button button = new Button(title);
		if (clickHandler != null) {
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					clickHandler.onclick(button);
				}
			});
		}
		return button;
	}
	
	public static InputGroup createInputGroup(String label, boolean inline) {
		TextField field = new TextField();
		Label inputLabel = null;
		Node group;
		if (inline) {
			field.setPromptText(label);
			group = field;
		} else {
			inputLabel = GuiUtils.createLabel(label);
			group = GuiUtils.createSplitPane(Orientation.HORIZONTAL, inputLabel, field);
		}
		return new InputGroup(group, inputLabel, field);
	}
	
	public static InputGroup createInputGroup(String label) {
		return GuiUtils.createInputGroup(label, true);
	}
	
	public static Label createLabel(String text) {
		LibraryDB.LOGGER.debug("Assembling label with text " + text);
		Label label = new Label(text);
		return label;
	}
	
	public static Label createTitle(String title) {
		LibraryDB.LOGGER.debug("Assembling title label with text " + title);
		Label label = createLabel(title);
		label.setFont(TITLE_FONT);
		return label;
	}

//	public static TableView<T> createTableView(T dataStore, List<String> columns) {
//
//	}
	
	public static enum Float {
		TOP, LEFT, BOTTOM, RIGHT;
		
		public final String name = this.name().toLowerCase();
	}
	
	public static enum Orientation {
		VERTICAL, HORIZONTAL;
		
		public final String name = this.name().toLowerCase();
	};
}
