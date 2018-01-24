package org.whstsa.library.util;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.whstsa.library.gui.components.CheckBoxElement;

public interface ClickHandler {
	void onclick(Button button);
	void onclick(Menu menu);
    void onclick(MenuItem menuItem);
}
