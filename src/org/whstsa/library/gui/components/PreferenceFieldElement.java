package org.whstsa.library.gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.gui.Config;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.FieldProperty;

public class PreferenceFieldElement extends HBox implements Element{

    private LabelElement labelElement;
    private String id;
    private Config config;
    private String key;
    private FieldProperty property;

    private TextFieldElement textField;
    private CheckBoxElement checkBox;
    private SpinnerElement spinner;
    private ChoiceBoxElement choiceBox;

    public PreferenceFieldElement(String label, FieldProperty property, String key, Config config, int lowerLimit, int upperLimit) {
        //Creates an HBox of the label and field, which could be a textfield, checkbox, or other depending on the field property
        super();
        this.id = key;
        this.property = property;
        this.key = key;
        this.config = config;
        LibraryDB.LOGGER.debug();
        switch (this.property) {
            case STRING:
                this.labelElement = GuiUtils.createLabel(label);
                this.textField = GuiUtils.createTextField("", true, config.getProperty(key));
                super.getChildren().addAll(this.labelElement, this.textField);
                break;
            case BOOLEAN:
                this.labelElement = GuiUtils.createLabel(label);
                this.checkBox = GuiUtils.createCheckBox("", config.getProperty(key).equals("true"));
                super.getChildren().addAll(this.labelElement, this.checkBox);
                break;
            case INT:
                this.labelElement = GuiUtils.createLabel(label);
                this.spinner = GuiUtils.createSpinner("", true, lowerLimit, upperLimit, Integer.parseInt(config.getProperty(key)));
                super.getChildren().addAll(this.labelElement, this.spinner);
                break;
            case CHOICE:
                //I don't wanna
                break;
            default:
                break;
        }
        this.setAlignment(Pos.CENTER);
        this.setSpacing(4);
    }

    public PreferenceFieldElement(String label, FieldProperty property, String key, Config config) {
        this(label, property, key, config, -1, -1);
    }

    public void save() {
        switch (this.property) {
            case STRING:
                this.config.setProperty(this.key, textField.getResult());
                break;
            case BOOLEAN:
                this.config.setProperty(this.key, checkBox.getResult() + "");
                break;
            case INT:
                this.config.setProperty(this.key, spinner.getResult() + "");
                break;
            case CHOICE:
                this.config.setProperty(this.key, choiceBox.getResult().toString());
                break;
            default:
                break;
        }
    }

    public Node getComputedElement() {
        if (this.labelElement == null) {
            return this;
        }
        return GuiUtils.createHBox(10, this);
    }

    public String getID() {
        return this.id;
    }

    public Object getResult() { return null; }

    @Override
    public String getString() {
        return this.labelElement.toString();
    }

    @Override
    public boolean getBoolean() {
        return false;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }
}
