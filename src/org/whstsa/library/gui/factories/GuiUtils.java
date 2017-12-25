package org.whstsa.library.gui.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
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

    public static HBox createHBox(double padding, Node ...nodes) {
        HBox container = new HBox(nodes);
        container.setPadding(new Insets(padding, padding, padding, padding));
        return container;
    }
    public static HBox createHBox(Node ...nodes) {
        return createHBox(5.0, nodes);
    }

    public static VBox createVBox(double padding, Node ...nodes) {
        VBox container = new VBox(nodes);
        container.setPadding(new Insets(padding, padding, padding, padding));
        return container;
    }

    public static VBox createVBox(Node ...nodes) {
        return createVBox(5.0, nodes);
    }

	public static BorderPane createBorderPane(Direction direction, Node mainNode, Node asideNode) {
		BorderPane pane = new BorderPane();
		pane.setCenter(mainNode);
		switch (direction) {
        case LEFTHAND:
            pane.setLeft(asideNode);
            break;
        case RIGHTHAND:
            pane.setRight(asideNode);
            break;
        default:
            pane.setLeft(asideNode);
            break;
        }
        return pane;
	}

	public static BorderPane createBorderPane(Node mainNode, Node asideNode) {
	    return createBorderPane(Direction.LEFTHAND, mainNode, asideNode);
    }

	public static Scene createScene(Parent parent) {
		return new Scene(parent, 512, 512);
	}

	public static Button createButton(String title, ClickHandler clickHandler) {
		LibraryDB.LOGGER.debug("Assembling button with title " + title + " (CLICK HANDLER: " + Logger.assertion(clickHandler != null) + ")");
		Button button = new Button(title);
		if (title.length() < 10) {
		    button.setPrefWidth(80.0);
        }
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

	public static Label createLabel(String text, double size) {
		LibraryDB.LOGGER.debug("Assembling label with text " + text);
		Label label = new Label(text);
		label.setFont(Font.font(size));
		return label;
	}

	public static Label createLabel(String text) {
	    return createLabel(text, 16);
    }
	
	public static Label createTitle(String title) {
		LibraryDB.LOGGER.debug("Assembling title label with text " + title);
		Label label = createLabel(title);
		label.setFont(TITLE_FONT);
		return label;
	}
	public static void defaultCloseOperation(ActionEvent event) {
		LibraryDB.LOGGER.debug("Window closed");
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
	
	public static enum Direction {
		LEFTHAND, RIGHTHAND;
		
		public final String name = this.name().toLowerCase();
	}
	
	public static enum Orientation {
		VERTICAL, HORIZONTAL;
		
		public final String name = this.name().toLowerCase();
	};
}
