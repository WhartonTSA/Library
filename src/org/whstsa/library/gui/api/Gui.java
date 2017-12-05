package org.whstsa.library.gui.api;

import javax.swing.JFrame;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public interface Gui {
	public Scene draw();
	
	public String getUUID();
}
