package org.whstsa.library.gui.api;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import org.whstsa.library.gui.components.MenuBarElement;
import org.whstsa.library.util.ClickHandler;

public class GuiMenuBar {

    private MenuBar mainMenuBar;

    public GuiMenuBar() {
        MenuBarElement barElement = new MenuBarElement();
        barElement.addMenus("File", "Edit", "Help");

        barElement.addMenuItems(0, "New", "Save", "Settings...", "Exit");
        barElement.addMenuItems(1, "Edit JSON... (Developer)");
        barElement.addMenuItems(2, "About...", "Help...");

        this.mainMenuBar = barElement.getMenuBar();
    }

    public MenuBar getMenu() {
        return this.mainMenuBar;
    }

}
