package org.whstsa.library.gui.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import org.whstsa.library.util.ClickHandlerMenuItem;

public class MenuBarElement extends MenuBar {

    public MenuBarElement() {
    }

    public void addMenu(String title) {
        super.getMenus().add(new Menu(title));
    }

    public void addSubMenu(int menuIndex, Menu menu) {
        super.getMenus().get(menuIndex).getItems().add(menu);
    }

    public void addMenuItem(int menuIndex, String title, ClickHandlerMenuItem clickHandler, KeyCombination keyCombo, boolean disabled) {
        MenuItem menuItem = new MenuItem(title);
        if (clickHandler != null) {
            menuItem.setOnAction(event -> clickHandler.onclick(menuItem));
        }
        if (keyCombo != null) {
            menuItem.setAccelerator(keyCombo);
        }
        if (disabled) {
            menuItem.setDisable(true);
        }
        super.getMenus().get(menuIndex).getItems().add(menuItem);
    }

    public void addMenuItem(int menuIndex, String title, ClickHandlerMenuItem clickHandler, KeyCombination keyCombo) {
        addMenuItem(menuIndex, title, clickHandler, keyCombo, false);
    }

    public void addMenuItem(int menuIndex, String title, boolean disabled) {
        addMenuItem(menuIndex, title, null, null, disabled);
    }

    public void addMenuItem(int menuIndex, String title) {
        addMenuItem(menuIndex, title, null, null, false);
    }

    public void addMenuItem(int menuIndex, String title, KeyCombination keyCombo) {
        addMenuItem(menuIndex, title, null, keyCombo, false);
    }

    public void addSubMenuItem(int menuIndex, int subMenuIndex, String title, ClickHandlerMenuItem clickHandler, KeyCombination keyCombo, boolean disabled) {
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction(event -> clickHandler.onclick(menuItem));
        if (keyCombo != null) {
            menuItem.setAccelerator(keyCombo);
        }
        if (disabled) {
            menuItem.setDisable(true);
        }
        ((Menu) super.getMenus().get(menuIndex).getItems().get(subMenuIndex)).getItems().add(menuItem);
    }
    public void addSubMenuItem(int menuIndex, int subMenuIndex, String title, ClickHandlerMenuItem clickHandler, KeyCombination keyCombo) {
        addSubMenuItem(menuIndex, subMenuIndex, title, clickHandler, keyCombo, false);
    }

    public void addMenuSeparator(int menuIndex) {
        super.getMenus().get(menuIndex).getItems().add(new SeparatorMenuItem());
    }

    public void addSubMenuSeparator(int menuIndex, int subMenuIndex) {
        ((Menu )super.getMenus().get(menuIndex).getItems().get(subMenuIndex)).getItems().add(new SeparatorMenuItem());
    }

    public static Menu createMenu(String title) {
        return new Menu(title);
    }
}
