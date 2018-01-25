package org.whstsa.library.gui.api;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SeparatorMenuItem;
import org.whstsa.library.gui.components.MenuBarElement;
import org.whstsa.library.util.ClickHandler;

public class GuiMenuBar {

    private MenuBar mainMenuBar;

    public GuiMenuBar(boolean disableLibMgrButtons) {
        MenuBarElement barElement = new MenuBarElement();
        barElement.addMenus("File", "Edit", "Help");

        barElement.addMenuItems(0, "[Placeholder]", "Save", "Settings...", "Separator", "Exit");//Use a placeholder whenever you want to add a submenu and replace it later
        barElement.addMenuItems(1, "Edit JSON... (Developer)");
        barElement.addMenuItems(2, "About...", "Help...");

        MenuBar mainMenuBar = barElement.getMenuBar();

        Menu newSubMenu = MenuBarElement.createMenu("New");
        newSubMenu.getItems().addAll(MenuBarElement.createMenuItem("New Library...", null),
                MenuBarElement.createMenuItem("New Person...", null),
                MenuBarElement.createMenuItem("New Book...", null),
                MenuBarElement.createMenuItem("New Membership...", null),
                MenuBarElement.createMenuItem("New Checkout...", null),
                MenuBarElement.createMenuItem("New Return...", null));

        mainMenuBar.getMenus().get(0).getItems().set(0, newSubMenu);
        mainMenuBar.getMenus().get(0).getItems().set(3, new SeparatorMenuItem());
        if (disableLibMgrButtons) {
            Menu disableMenu1 = (Menu) mainMenuBar.getMenus().get(0).getItems().get(0);
            for (int i = 2; i < 6 ; i++) {
                disableMenu1.getItems().get(i).setDisable(true);
            }
        }
        this.mainMenuBar = mainMenuBar;
    }

    public MenuBar getMenu() {
        return this.mainMenuBar;
    }

}
