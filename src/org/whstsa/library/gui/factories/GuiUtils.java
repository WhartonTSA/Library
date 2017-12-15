package org.whstsa.library.gui.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.gui.components.CheckBoxElement;
import org.whstsa.library.gui.components.TextFieldElement;
import org.whstsa.library.util.ClickHandler;
import org.whstsa.library.util.Logger;

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
  
	public static Button createButton(String title) {
		return createButton(title, null);
	}
	
	public static TextFieldElement createTextField(String prompt, boolean inline, String placeholder, String id) {
		TextFieldElement element = new TextFieldElement(id, prompt, inline);
		element.setText(placeholder);
		return element;
	}

	public static TextFieldElement createTextField(String prompt, boolean inline, String placeholder) {
		return createTextField(prompt, inline, placeholder, prompt);
	}

	public static TextFieldElement createTextField(String prompt, boolean inline) {
		return createTextField(prompt, inline, null);
	}

	public static TextFieldElement createTextField(String prompt) {
		return createTextField(prompt, false);
	}

	public static CheckBoxElement createCheckBox(String prompt, boolean selected, boolean inline) {
        CheckBoxElement checkBoxElement = new CheckBoxElement(prompt, prompt, inline);
        checkBoxElement.setSelected(selected);
        return checkBoxElement;
    }

	public static CheckBoxElement createCheckBox(String prompt, boolean selected) {
	    return createCheckBox(prompt, selected, false);
    }

    public static CheckBoxElement createCheckBox(String prompt) {
	    return createCheckBox(prompt, false);
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
	public static void defaultCloseOperation(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
	}

	public static ClickHandler defaultClickHandler() {
	    return new ClickHandler() {
	        @Override
	        public void onclick(Button button) {}
        };
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
