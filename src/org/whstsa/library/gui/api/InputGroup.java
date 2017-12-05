package org.whstsa.library.gui.api;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class InputGroup {
	
	private Node finalElement;
	private Label label;
	private TextField field;
	
	public InputGroup(Node finalElement, Label label, TextField field) {
		this.finalElement = finalElement;
		this.label = label;
		this.field = field;
	}
	
	public Node getNode() {
		return this.finalElement;
	}
	
	public StackPane getStackPane() {
		if (this.finalElement instanceof StackPane) {
			return (StackPane) this.finalElement;
		} else return null;
	}
	
	public Label getLabel() {
		return this.label;
	}
	
	public TextField getField() {
		return this.field;
	}
}
