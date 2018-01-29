package org.whstsa.library.gui.factories;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.Operator;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.util.ClickHandlerCheckBox;

import java.util.*;

public class DialogBuilder {

    private List<Element> elementList;
    private String title;
    private boolean isCancellable;
    private List<ButtonType> buttons;
    private Map<ButtonType, Callback<Event>> buttonActionMap;
    private List<ButtonType> closingButtons;

    private Operator<GridPane, List<Element>, GridPane> gridPaneOperator;

    {
        this.elementList = new ArrayList<>();
        this.title = null;
        this.isCancellable = true;
        this.setFormatAssembler((grid, data) -> {
            int increment = 0;
            for (Element element : this.elementList) {
                grid.add(element.getComputedElement(), 0, increment);
                increment++;
            }
            return grid;
        });
        this.buttons = new ArrayList<>();
        this.buttonActionMap = new HashMap<>();
        this.closingButtons = new ArrayList<>();
    }

    public DialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public DialogBuilder addElement(Element element) {
        return this.addAllElements(element);
    }

    public DialogBuilder addAllElements(List<Element> elements) {
        this.elementList.addAll(elements);
        return this;
    }

    public DialogBuilder addAllElements(Element ...elements) {
        return this.addAllElements(Arrays.asList(elements));
    }

    public DialogBuilder addLabel(String text) {
        return this.addElement(GuiUtils.createLabel(text));
    }

    public DialogBuilder addTextField(String prompt, String placeholder, boolean inline) {
        return this.addElement(GuiUtils.createTextField(prompt, inline, placeholder));
    }

    public DialogBuilder addTextField(String prompt, String placeholder) {
        return this.addTextField(prompt, placeholder, false);
    }

    public DialogBuilder addTextField(String prompt) {
        return this.addTextField(prompt, null);
    }

    public DialogBuilder addCheckBox(String prompt, boolean selected, boolean inline, boolean disabled, ClickHandlerCheckBox clickHandler) {
        return this.addElement(GuiUtils.createCheckBox(prompt, selected, inline, disabled, clickHandler));
    }

    public DialogBuilder addCheckBox(String prompt, boolean selected, boolean inline, boolean disabled) {
        return this.addCheckBox(prompt, selected, inline, disabled, null);
    }

    public DialogBuilder addCheckBox(String prompt, boolean selected, boolean inline) {
        return this.addCheckBox(prompt, selected, inline, false, null);
    }

    public DialogBuilder addCheckBox(String prompt, boolean selected) {
        return this.addCheckBox(prompt, selected, false);
    }

    public DialogBuilder addCheckBox(String prompt) {
        return this.addCheckBox(prompt, false);
    }

    public DialogBuilder addChoiceBox(String label, ObservableList<String> items, boolean useLabel, int selected) {
        return this.addElement(GuiUtils.createChoiceBox(label, items, true, selected));
    }

    public DialogBuilder addChoiceBox(String label, Map<IBook,List<ICheckout>> items, boolean useLabel, int selected) {
        return this.addElement(GuiUtils.createChoiceBox(label, items, true, selected));
    }

    public DialogBuilder addChoiceBox(ObservableList<String> items) {
        return addChoiceBox("", items, false, -1);
    }

    public DialogBuilder setIsCancellable(boolean cancellable) {
        this.isCancellable = cancellable;
        return this;
    }

    public DialogBuilder addButton(ButtonType button, boolean closer, Callback<Event> clickConsumer) {
        this.addButton(button);
        return this.onClick(button, clickConsumer);
    }

    public DialogBuilder addButton(ButtonType button) {
        return this.addButtons(button);
    }

    public DialogBuilder addButtons(ButtonType ...buttons) {
        return this.addButtons(Arrays.asList(buttons));
    }

    public DialogBuilder addButtons(List<ButtonType> buttons) {
        this.buttons.addAll(buttons);
        return this;
    }

    public DialogBuilder onClick(ButtonType button, Callback<Event> clickConsumer) {
        this.buttonActionMap.put(button, clickConsumer);
        return this;
    }

    public List<ButtonType> getButtons() {
        return this.buttons;
    }

    public DialogBuilder setFormatAssembler(Operator<GridPane, List<Element>, GridPane> gridPaneOperator) {
        this.gridPaneOperator = gridPaneOperator;
        return this;
    }

    public boolean isCancellable() {
        return this.isCancellable;
    }

    public List<Element> getElementList() {
        return this.elementList;
    }

    public Dialog<Map<String, Element>> build() {
        SimpleBooleanProperty isCancelled = new SimpleBooleanProperty(false);
        Dialog<Map<String, Element>> dialog = new Dialog<>();
        dialog.setTitle(this.title);

        dialog.getDialogPane().getButtonTypes().addAll(this.getButtonList());

        GridPane grid = this.gridPaneOperator.mutate(DialogUtils.buildGridPane(), this.elementList);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> {
            if (grid.getChildren().size() == 0) {
                return;
            }
            grid.getChildren().get(0).requestFocus();
        });

        DialogUtils.addInputFieldMatcher(dialog);

        this.buttonActionMap.forEach((button, action) -> {
            Node buttonNode = dialog.getDialogPane().lookupButton(button);
            if (buttonNode != null) {
                buttonNode.addEventFilter(ActionEvent.ACTION, event -> {
                    if (isCancelled.get()) {
                        return;
                    }
                    action.callback(event);
                    if (this.closingButtons.contains(button)) {
                        buttonNode.getScene().getWindow().hide();
                    }
                });
            }
        });

        this.closingButtons.stream().filter(button -> !this.buttonActionMap.containsKey(button)).forEach(button -> {
            Node buttonNode = dialog.getDialogPane().lookupButton(button);
            if (buttonNode != null) {
                buttonNode.addEventFilter(ActionEvent.ACTION, event -> {
                    buttonNode.getScene().getWindow().hide();
                });
            }
        });

        final Node cancelNode = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        if (cancelNode != null) {
            cancelNode.addEventFilter(ActionEvent.ACTION, event -> {
                isCancelled.set(true);
                dialog.getDialogPane().getScene().getWindow().hide();
                event.consume();
            });
        }

        return dialog;
    }

    protected final List<ButtonType> getRawButtonList() {
        List<ButtonType> buttonList = this.buttons;
        if (buttonList.size() == 0) {
            buttonList.add(ButtonType.OK);
        }
        return buttonList;
    }

    protected final List<ButtonType> getButtonList() {
        if (!this.isCancellable) {
            return this.getRawButtonList();
        }
        List<ButtonType> buttonList = new ArrayList<>();
        buttonList.add(ButtonType.CANCEL);
        buttonList.addAll(this.getRawButtonList());
        return buttonList;
    }

}
