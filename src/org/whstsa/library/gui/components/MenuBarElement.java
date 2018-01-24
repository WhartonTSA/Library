package org.whstsa.library.gui.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.whstsa.library.api.Callback;
import org.whstsa.library.util.ClickHandler;

import java.util.List;

public class MenuBarElement {

    private List<Menu> menus;
    private List<List<MenuItem>> menuItems;

    public MenuBarElement() {
    }

    public int getIndexFromMenu(Menu menu) {
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).equals(menu)) {
                return i;
            }
        }
        return -1;
    }

    public void addMenu(String title, ClickHandler clickHandler) {
        Menu menu = new Menu(title);
        menu.setOnAction(event -> clickHandler.onclick(menu));
        menus.add(menu);
    }

    public void addMenu(String title) {
        addMenu(title, null);
    }

    public void addMenus(String ...titles) {
        for (String title : titles) {
            addMenu(title);
        }
    }

    public void addMenuItem(int menuIndex, String title, ClickHandler clickHandler) {
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction(event -> clickHandler.onclick(menuItem));
        menuItems.get(menuIndex).add(menuItem);
    }

    public void addMenuItem(int menuIndex, String title) {
        addMenuItem(menuIndex, title, null);
    }

    public void addMenuItems(int menuIndex, String ...titles) {
        for (String title : titles) {
            addMenuItem(menuIndex, title);
        }
    }

    public MenuBar getMenuBar() {
        MenuBar mainMenuBar = new MenuBar();
        for (int i = 0; i < menuItems.size(); i++) {
            Menu menu = menus.get(i);
            for (int j = 0; j < menuItems.get(i).size(); j++) {
                menu.getItems().add(menuItems.get(i).get(j));
            }
            mainMenuBar.getMenus().add(menu);
        }
        return mainMenuBar;
    }


}
