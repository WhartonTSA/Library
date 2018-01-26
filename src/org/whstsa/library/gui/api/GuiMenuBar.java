package org.whstsa.library.gui.api;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.gui.components.MenuBarElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.dialogs.*;
import org.whstsa.library.util.ClickHandler;

public class GuiMenuBar {

    private MenuBar mainMenuBar;

    public GuiMenuBar() {

        MenuBarElement barElement = new MenuBarElement();

        barElement.addMenu("_File");
        barElement.addSubMenu(0, MenuBarElement.createMenu("_New"));
        barElement.addSubMenuItem(0, 0, "New Person...", event -> PersonMetaDialogs.createPerson(person -> {}), null);
        barElement.addSubMenuItem(0, 0, "New Library...", event -> LibraryMetaDialogs.createLibrary(library -> {}), null, false);
        barElement.addSubMenuItem(0, 0, "New Book...", null, null, true);
        barElement.addSubMenuItem(0, 0, "New Membership...", null, null, true);
        barElement.addSubMenuItem(0, 0, "New Checkout...", null, null, true);
        barElement.addSubMenuItem(0, 0, "New Return...", null, null, true);
        barElement.addMenuItem(0, "Save", event -> System.out.println("Saved."), KeyCombination.keyCombination("CTRL+S"));
        barElement.addMenuItem(0, "Settings...");
        barElement.addMenuSeparator(0);
        barElement.addMenuItem(0, "_Exit", event -> System.out.println("Exiting..."), null);
        barElement.addMenu("_Edit");
        barElement.addMenuItem(1, "Edit JSON... (Dev)");
        barElement.addMenu("_Help");
        barElement.addMenuItem(2, "About...");
        barElement.addMenuItem(2, "Help...");

        this.mainMenuBar = barElement;
    }

    public GuiMenuBar(Table<IBook> bookTable, Table<IMember> memberTable, ObservableReference<ILibrary> libraryReference) {

        MenuBarElement barElement = new MenuBarElement();

        barElement.addMenu("_File");
        barElement.addSubMenu(0, MenuBarElement.createMenu("_New"));
        barElement.addSubMenuItem(0, 0, "New Person...", event -> PersonMetaDialogs.createPerson(person -> {}), null);
        barElement.addSubMenuItem(0, 0, "New Library...", event -> LibraryMetaDialogs.createLibrary(library -> {}), null, true);
        barElement.addSubMenuItem(0, 0, "New Book...", event -> BookMetaDialogs.createBook(book -> {bookTable.refresh();}, libraryReference), null, false);
        barElement.addSubMenuItem(0, 0, "New Membership...", event -> MemberMetaDialogs.createMember(member -> {memberTable.refresh();}, libraryReference), null, false);
        barElement.addSubMenuItem(0, 0, "New Checkout...", event -> CheckoutMetaDialogs.checkOutPreMenu(checkout -> {memberTable.refresh();}, libraryReference), null, false);
        barElement.addSubMenuItem(0, 0, "New Return...", event -> CheckoutMetaDialogs.checkInPreMenu(checkout -> {}, libraryReference), null, false);
        barElement.addMenuItem(0, "Save", event -> System.out.println("Saved."), KeyCombination.keyCombination("CTRL+S"));
        barElement.addMenuItem(0, "Settings...");
        barElement.addMenuSeparator(0);
        barElement.addMenuItem(0, "_Exit", event -> System.out.println("Exiting..."), null);
        barElement.addMenu("_Edit");
        barElement.addMenuItem(1, "Edit JSON... (Dev)");
        barElement.addMenu("_Help");
        barElement.addMenuItem(2, "About...");
        barElement.addMenuItem(2, "Help...");

        this.mainMenuBar = barElement;
    }

    public MenuBar getMenu() {
        return this.mainMenuBar;
    }

}
