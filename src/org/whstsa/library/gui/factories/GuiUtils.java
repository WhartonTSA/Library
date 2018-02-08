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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.gui.components.*;
import org.whstsa.library.util.ChoiceBoxProperty;
import org.whstsa.library.util.ClickHandlerCheckBox;
import org.whstsa.library.util.ClickHandler;
import org.whstsa.library.util.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GuiUtils {
	
	private static final int COMPONENT_PADDING = 16;
	private static final Font TITLE_FONT = new Font(30.0);
	private static final Font LABEL_FONT = new Font(16.0);
	
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

    public static HBox createHBox(double padding, String css, double spacing, Node ...nodes) {
        HBox container = new HBox(nodes);
        container.setPadding(new Insets(padding, padding, padding, padding));
        container.setAlignment(Pos.CENTER);
        container.setStyle(css);
        container.setSpacing(spacing);
        return container;
    }

    public static HBox createHBox(double padding, Node ...nodes) {
	    return createHBox(padding, "", 5, nodes);
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
		((VBox) container.getTop()).getChildren().set(1, new SearchBarElement(id, label, items, container, table));
    }

    public static void createSearchBar(String id, String label, ObservableList<String> items, BorderPane container, ObservableReference<ILibrary> libraryReference, Table<IMember> table, String whatever) {
		((VBox) container.getTop()).getChildren().set(1, new SearchBarElement(id, label, items, container, table, whatever));
    }

    public static Separator createSeparator() {
		return new Separator();
	}

	public static HBox createSpacer() {
		HBox spacer = new HBox(new Label(""));
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
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
        return createButton(title, nativeWidth, width, null, clickHandler);
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

	public static CheckBoxElement createCheckBox(String prompt, boolean selected, boolean inline, boolean disabled, ClickHandlerCheckBox clickHandler) {
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

	public static ChoiceBoxElement createChoiceBox(String label, ObservableList<String> items, boolean useLabel, int selected, boolean disabled) {
		return new ChoiceBoxElement(label, label, items, useLabel, selected, disabled);
	}

	public static ChoiceBoxElement createChoiceBox(String label, Map<IBook,List<ICheckout>> items, ChoiceBoxProperty<String> property, boolean useLabel, int selected) {
		return new ChoiceBoxElement(label, label, items, property, useLabel, selected);
	}

    public static ChoiceBoxElement createChoiceBox(String label, Map<IBook,List<ICheckout>> items, boolean useLabel, int selected) {
        return new ChoiceBoxElement(label, label, items, null, useLabel, selected);
    }

    public static ChoiceBoxElement createChoiceBox(ObservableList<String> items) {
        return new ChoiceBoxElement("", "", items, false, -1, false);
    }

    public static SpinnerElement createSpinner(String label, boolean useLabel, int start, int end, int selectedIndex) {
		return new SpinnerElement(label, label, useLabel, start, end, selectedIndex);
	}

	public static SpinnerElement createSpinner(String label, boolean useLabel, int start, int end) {
		return new SpinnerElement(label, label, useLabel, start, end, start);
	}

	public static ComboBox createComboBox(ObservableList<String> items, boolean editable) {
		ComboBox comboBox = new ComboBox();
		comboBox.editableProperty().setValue(editable);
		return comboBox;
	}

	public static LabelElement createLabel(String text, Font font, Color color) {
		LibraryDB.LOGGER.debug("Assembling label with text " + text);
		LabelElement label = new LabelElement(text, text);
		label.setFont(font);
		label.setTextFill(color);
		return label;
	}

	public static LabelElement createLabel(String text, double size, Color color) {
		return createLabel(text, new Font(size), Color.BLACK);
	}

	public static LabelElement createLabel(String text, double size) {
	    return createLabel(text, LABEL_FONT, Color.BLACK);
	}

	public static LabelElement createLabel(String text) {
	    return createLabel(text, LABEL_FONT, Color.BLACK);
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
	    return button -> {};
    }
	
	public enum Direction {
		LEFTHAND, RIGHTHAND;
		
		public final String name = this.name().toLowerCase();
	}
	
	public enum Orientation {
		VERTICAL, HORIZONTAL;
		
		public final String name = this.name().toLowerCase();
	};
}
