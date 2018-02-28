package org.whstsa.library.gui.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.Callback;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogUtils {

    private static final int ELEMENT_PADDING = 10;
    private static final Logger LOGGER = new Logger("DialogFactory");

    public static Alert createDialog(String title, String body, String header, AlertType type, Callback<Callback<Boolean>> onClose) {
        Alert alert;
        try {
            alert = new Alert(type);
        } catch (IllegalStateException ex) {
            Logger.DEFAULT_LOGGER.log("Shutting down TardyDB - We are no longer in a JavaFX Application Thread - Did the user close the program?");
            System.exit(-1);
            return null;
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        final Node cancelNode = alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        if (cancelNode != null && cancelNode instanceof Button) {
            cancelNode.addEventFilter(ActionEvent.ACTION, event -> alert.getDialogPane().getScene().getWindow().hide());
        }
        if (onClose != null) {
            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent arg0) {
                    onClose.callback((Boolean arg1) -> {
                        if (arg1) {
                            alert.getDialogPane().getScene().getWindow().hide();
                        }
                    });
                }
            });
        }
        return alert;
    }

    public static Alert createDialog(String title, String body, String header, AlertType type) {
        return createDialog(title, body, header, type, null);
    }

    public static Alert createDialog(String title, String body, String header) {
        return createDialog(title, body, header, AlertType.NONE);
    }

    public static Alert createDialog(String title, String body) {
        return createDialog(title, body, null);
    }

    public static Alert createDialog(String title) {
        return createDialog(title, null);
    }

    public static Alert createDialog() {
        return createDialog(null);
    }

    public static void getDialogResults(Dialog<Map<String, Element>> dialog, Callback<Map<String, Element>> callback) {
        dialog.show();
        dialog.getDialogPane().getButtonTypes().stream().filter(buttonType -> buttonType != ButtonType.CANCEL && buttonType != ButtonType.CLOSE).forEach(buttonType -> {
            Node button = dialog.getDialogPane().lookupButton(buttonType);
            button.onMouseReleasedProperty().set(event -> callback.callback(dialog.getResult()));
        });
    }

    private static void notify(String title, String message, AlertType type) {
        Alert alert = DialogUtils.createDialog(title, message, title, type);
        //Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        //alertStage.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    private static void addNode(Map<String, Element> valueMap, Node node) {
        if (node instanceof Element) {
            valueMap.put(((Element) node).getID(), (Element) node);
        } else if (node instanceof Pane) {
            ((Pane) node).getChildren().forEach((node1) -> addNode(valueMap, node1));
        }
    }

    public static void addInputFieldMatcher(Dialog<Map<String, Element>> dialog) {
        dialog.setResultConverter((buttonType) -> {
            Map<String, Element> valueMap = new HashMap<>();
            dialog.getDialogPane().getChildren().forEach(node -> {
                if (node instanceof GridPane) {
                    GridPane grid = (GridPane) node;
                    grid.getChildren().forEach((child) -> {
                        addNode(valueMap, child);
                    });
                }
            });
            return valueMap;
        });
    }

    public static GridPane buildGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(ELEMENT_PADDING);
        gridPane.setVgap(ELEMENT_PADDING);
        return gridPane;
    }

    public static ButtonType[] createButtonList(boolean cancelButton, ButtonType... buttons) {
        ArrayList<ButtonType> buttonList = new ArrayList<>();
        for (ButtonType button : buttons) {
            buttonList.add(button);
        }
        if (cancelButton) {
            buttonList.add(ButtonType.CANCEL);
        }
        return buttonList.toArray(new ButtonType[0]);
    }

    public static void closeDialog(Alert dialog) {
        LibraryDB.LOGGER.debug("Closing dialog");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        dialog.close();
    }
}
