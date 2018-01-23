package org.whstsa.library.gui.factories;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.gui.components.*;
import org.whstsa.library.util.CheckBoxClickHandler;
import org.whstsa.library.util.ClickHandler;
import org.whstsa.library.util.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public static HBox createHBox(double padding, String css, Node ...nodes) {
        HBox container = new HBox(nodes);
        container.setPadding(new Insets(padding, padding, padding, padding));
        container.setAlignment(Pos.CENTER);
        container.setStyle(css);
        return container;
    }

    public static HBox createHBox(double padding, Node ...nodes) {
	    return createHBox(5.0, "", nodes);
    }
    public static HBox createHBox(Node ...nodes) {
        return createHBox(5.0, nodes);
    }

    public static VBox createVBox(double padding, Node ...nodes) {
        VBox container = new VBox(nodes);
        container.setPadding(new Insets(padding, padding, padding, padding));
        container.setAlignment(Pos.TOP_CENTER);
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

    public static TextFlowElement createTextFlow(String id, double size, String css, List<String> fields) {
        return new TextFlowElement(id, size, css, fields);
    }

	public static TextFlowElement createTextFlow(String id, double size, String css, String ...fields) {
		return new TextFlowElement(id, size, css, Arrays.asList(fields));
	}

	public static void createSearchBar(String id, String label, ObservableList<String> items, BorderPane container, ObservableReference<ILibrary> libraryReference, Table<IBook> table) {
        container.setTop(new SearchBarElement(id, label, items, container, table));
    }

    public static void createSearchBar(String id, String label, ObservableList<String> items, BorderPane container, ObservableReference<ILibrary> libraryReference, Table<IMember> table, String whatever) {
        container.setTop(new SearchBarElement(id, label, items, container, table, whatever));
    }

    public static Separator createSeparator() {
		return new Separator();
	}

	public static ToggleGroup createToggleButtonGroup(String ...titles) {//Doesn't work, will probably make a new class for this
	    ToggleGroup toggleGroup = new ToggleGroup();
	    for (int i = 0; i < titles.length; i++) {
	        createToggleButton(titles[i]).setToggleGroup(toggleGroup);
        }
	    return toggleGroup;
    }

    public static ToggleButton createToggleButton(String title) {
	    return new ToggleButton(title);
    }

	public static Button createButton(String title, boolean nativeWidth, double width, Pos pos, ClickHandler clickHandler) {
		LibraryDB.LOGGER.debug("Assembling button with title " + title + " (CLICK HANDLER: " + Logger.assertion(clickHandler != null) + ")");
		Button button = new Button(title);
		if (title.length() < 10 && width == 80.0 && !nativeWidth) {//If title is more than 10 characters or nativeWidth == true, default to the automatic button width. Otherwise, set width to 80
		    button.setPrefWidth(80.0);
        }
        else if (!nativeWidth){
			button.setPrefWidth(width);
		}
		if (clickHandler != null) {
			button.setOnAction(event -> clickHandler.onclick(button));
		}
		if (pos != null) {
		    button.setAlignment(pos);
        }
		return button;
	}

    public static Button createButton(String title, boolean nativeWidth, double width, ClickHandler clickHandler) {
        return createButton(title, false, width, null, clickHandler);
    }

	public static Button createButton(String title, double width, ClickHandler clickHandler) {
		return createButton(title, false, width, clickHandler);
	}

    public static Button createButton(String title, boolean nativeWidth, ClickHandler clickHandler) {
        return createButton(title, nativeWidth, 80.0, clickHandler);
    }

	public static Button createButton(String title, ClickHandler clickHandler) {
	    return createButton(title, false, clickHandler);
    }

	public static Button createButton(String title) {
		return createButton(title,null);
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

	public static CheckBoxElement createCheckBox(String prompt, boolean selected, boolean inline, boolean disabled, CheckBoxClickHandler clickHandler) {
        CheckBoxElement checkBoxElement = new CheckBoxElement(prompt, prompt, inline, disabled);
        checkBoxElement.setSelected(selected);
        if (clickHandler != null) {
        	checkBoxElement.setOnAction(event -> clickHandler.onclick(checkBoxElement));
		}
        return checkBoxElement;
    }

	public static CheckBoxElement createCheckBox(String prompt, boolean selected, boolean inline) {
		return createCheckBox(prompt, selected, inline, false, null);
	}

	public static CheckBoxElement createCheckBox(String prompt, boolean selected) {
	    return createCheckBox(prompt, selected, false);
    }

    public static CheckBoxElement createCheckBox(String prompt) { return createCheckBox(prompt, false); }

	public static ChoiceBoxElement createChoiceBox(String label, ObservableList<String> items, boolean useLabel, int selected) {
		return new ChoiceBoxElement(label, label, items, useLabel, selected);
	}

	public static ChoiceBoxElement createChoiceBox(String label, Map<IBook,List<ICheckout>> items, boolean useLabel, int selected) {
		return new ChoiceBoxElement(label, label, items, useLabel, selected);
	}

    public static ChoiceBoxElement createChoiceBox(ObservableList<String> items) {
        return new ChoiceBoxElement("", "", items, false, -1);
    }

	public static ComboBox createComboBox(ObservableList<String> items, boolean editable) {
		ComboBox comboBox = new ComboBox();
		comboBox.editableProperty().setValue(editable);
		return comboBox;
	}

	public static LabelElement createLabel(String text, double size, Pos pos) {//TODO add functionality for boolean inline
		LibraryDB.LOGGER.debug("Assembling label with text " + text);
		LabelElement label = new LabelElement(text, text);
		label.setFont(Font.font(size));
		label.setAlignment(pos);
		return label;
	}

	public static LabelElement createLabel(String text, double size) {
	    return createLabel(text, size, Pos.CENTER);
	}

	public static LabelElement createLabel(String text) {
	    return createLabel(text, 16, Pos.CENTER);
    }
	
	public static LabelElement createTitle(String title) {
		LibraryDB.LOGGER.debug("Assembling title label with text " + title);
		LabelElement label = createLabel(title);
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
