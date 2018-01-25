package org.whstsa.library.gui.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import org.whstsa.library.api.Callback;
import org.whstsa.library.util.ClickHandler;
import org.whstsa.library.util.ClickHandlerMenu;
import org.whstsa.library.util.ClickHandlerMenuItem;

import java.util.*;

public class MenuBarElement {

    private List<Menu> menus;
    private List<Map<MenuItem, ClickHandlerMenuItem>> menuItems;

    public MenuBarElement() {
        this.menus = new ArrayList<>();
        this.menuItems = new ArrayList<>();
    }

    public int getIndexFromMenu(Menu menu) {
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).equals(menu)) {
                return i;
            }
        }
        return -1;
    }

    public void addMenu(String title, ClickHandlerMenu clickHandler) {//Doesn't really need a clickHandler
        Menu menu = new Menu(title);
        menu.setOnAction(event -> clickHandler.onclick(menu));
        menus.add(menu);
        menuItems.add(new LinkedHashMap<>());
    }

    public void addMenu(String title) {
        addMenu(title, defaultClickHandlerMenu());
    }

    public void addMenus(String ...titles) {
        for (String title : titles) {
            addMenu(title);
        }
    }

    public void addMenuItem(int menuIndex, String title, ClickHandlerMenuItem clickHandler, KeyCombination keyCombo) {
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction(event -> clickHandler.onclick(menuItem));
        if (keyCombo != null) {
            menuItem.setAccelerator(keyCombo);
        }
        menuItems.get(menuIndex).put(menuItem, clickHandler);
    }

    public void addMenuItem(int menuIndex, String title) {
        addMenuItem(menuIndex, title, defaultClickHandlerMenuItem(), null);
    }

    public void addMenuItems(int menuIndex, String ...titles) {//Mostly for making placeholder MenuItems
        for (String title : titles) {
            addMenuItem(menuIndex, title);
        }
    }

    public MenuBar getMenuBar() {
        MenuBar mainMenuBar = new MenuBar();
        for (int i = 0; i < menuItems.size(); i++) {
            Menu menu = menus.get(i);
            for (int j = 0; j < menuItems.get(i).size(); j++) {
                int k = j, l = i;//Re-instantiating variables for lambda
                MenuItem menuItem = (MenuItem) menuItems.get(i).keySet().toArray()[j];
                menuItem.setOnAction(event -> ((ClickHandlerMenuItem) menuItems.get(l).values().toArray()[k]).onclick(menuItem));
                menu.getItems().add(menuItem);
            }
            mainMenuBar.getMenus().add(menu);
        }
        return mainMenuBar;
    }

    public static Menu createMenu(String title) {
        return new Menu(title);
    }

    public static MenuItem createMenuItem(String title, ClickHandlerMenuItem clickHandler) {
        MenuItem menuItem = new MenuItem("title");
        if (clickHandler != null) {
            menuItem.setOnAction(event -> clickHandler.onclick(menuItem));
        }
        else {
            menuItem.setOnAction(event -> defaultClickHandlerMenuItem().onclick(menuItem));
        }
        return menuItem;
    }

    private static ClickHandlerMenuItem defaultClickHandlerMenuItem() {
        return menuItem -> {};
    }

    private static ClickHandlerMenu defaultClickHandlerMenu() {
        return menu -> {};
    }
}
