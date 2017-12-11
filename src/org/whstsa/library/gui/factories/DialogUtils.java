package org.whstsa.library.gui.factories;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.Callback;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	public static Dialog<Map<String, Element>> createInputDialog(ButtonType button, boolean cancelButton, String title, Element ...elements) {
		Dialog<Map<String, Element>> dialog = new Dialog<>();
		dialog.setTitle(title);

		dialog.getDialogPane().getButtonTypes().addAll(DialogUtils.createButtonList(cancelButton, button));

		GridPane grid = DialogUtils.buildGridPane();

		int incr = 0;
		for (Element element : elements) {
			grid.add(element.getComputedElement(), 0, incr);
			incr++;
		}

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> grid.getChildren().get(0).requestFocus());

		DialogUtils.addInputFieldMatcher(dialog);

		return dialog;
	}
	
	public static void getDialogResults(Dialog<Map<String, Element>> dialog, Callback<Map<String, Element>> callback, boolean requireContent, String[] expectedKeys) {
		Stage alertStage = (Stage) dialog.getDialogPane().getScene().getWindow();
		final List<Boolean> isClosable = new ArrayList<>();
		isClosable.add(false);
		alertStage.setOnCloseRequest((event) -> {
			if (!isClosable.get(0)) {
				event.consume();
			}
		});
		dialog.show();
		dialog.setOnCloseRequest((event) -> {
			Map<String, Element> responses = dialog.getResult();
			List<String> missingKeys = new ArrayList<>();
			for (String key : expectedKeys) {
				if (!responses.containsKey(key)) {
					missingKeys.add(key);
				}
			}
			isClosable.set(0, false);
			if (missingKeys.size() >= 1) {
				event.consume();
				StringBuilder message = new StringBuilder();
				for (String missing : missingKeys) {
					message.append("- " + missing + "\n");
				}
				DialogUtils.notify("Please answer all required fields", message.toString(), AlertType.ERROR);
				return;
			}
			isClosable.set(0, true);
			callback.callback(dialog.getResult());
		});
	}
	
	public static void getDialogResults(Dialog<Map<String, Element>> dialog, Callback<Map<String, Element>> callback) {
		DialogUtils.getDialogResults(dialog, callback, false, new String[] {});
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
	
	private static void addInputFieldMatcher(Dialog<Map<String, Element>> dialog) {
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
	
	private static GridPane buildGridPane() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(ELEMENT_PADDING);
		gridPane.setVgap(ELEMENT_PADDING);
		return gridPane;
	}
	
	private static ButtonType[] createButtonList(boolean cancelButton, ButtonType ...buttons) {
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
