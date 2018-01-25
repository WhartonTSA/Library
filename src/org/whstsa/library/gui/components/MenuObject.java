package org.whstsa.library.gui.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MenuObject {

    private Menu menu;
    private MenuItem menuItem;

    public MenuObject(Menu menu) {
        this.menu = menu;
    }

    public MenuObject(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
}
