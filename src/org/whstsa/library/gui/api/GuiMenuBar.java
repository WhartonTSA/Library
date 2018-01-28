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
        barElement.addSubMenuItem(0, 0, "New Person...", event -> PersonMetaDialogs.createPerson(person -> {}), KeyCombination.keyCombination("CTRL+P"));
        barElement.addSubMenuItem(0, 0, "New Library...", event -> LibraryMetaDialogs.createLibrary(library -> {}), KeyCombination.keyCombination("CTRL+L"), false);
        barElement.addSubMenuItem(0, 0, "New Book...", null, null, true);
        barElement.addSubMenuItem(0, 0, "New Membership...", null, null, true);
        barElement.addSubMenuItem(0, 0, "New Checkout...", null, null, true);
        barElement.addSubMenuItem(0, 0, "New Return...", null, null, true);
        barElement.addMenuItem(0, "_Save", event -> System.out.println("Saved."), KeyCombination.keyCombination("CTRL+S"));//TODO
        barElement.addMenuItem(0, "S_ettings...");
        barElement.addMenuSeparator(0);
        barElement.addMenuItem(0, "E_xit", event -> ExitMetaDialogs.exitConfirm(), null);
        barElement.addMenu("_Edit");
        barElement.addMenuItem(1, "_Edit JSON... (Dev)");//TODO
        barElement.addMenu("_Help");
        barElement.addMenuItem(2, "_About...");//TODO
        barElement.addMenuItem(2, "_Help...");//TODO

        this.mainMenuBar = barElement;
    }

    public GuiMenuBar(Table<IBook> bookTable, Table<IMember> memberTable, ObservableReference<ILibrary> libraryReference) {

        MenuBarElement barElement = new MenuBarElement();

        barElement.addMenu("_File");
        barElement.addSubMenu(0, MenuBarElement.createMenu("_New"));
        barElement.addSubMenuItem(0, 0, "New Person...", event -> PersonMetaDialogs.createPerson(person -> {}), KeyCombination.keyCombination("CTRL+P"));
        barElement.addSubMenuItem(0, 0, "New Library...", event -> LibraryMetaDialogs.createLibrary(library -> {}), null, true);
        barElement.addSubMenuItem(0, 0, "New Book...", event -> BookMetaDialogs.createBook(book -> {bookTable.refresh();}, libraryReference), KeyCombination.keyCombination("CTRL+B"), false);
        barElement.addSubMenuItem(0, 0, "New Membership...", event -> MemberMetaDialogs.createMember(member -> {memberTable.refresh();}, libraryReference), KeyCombination.keyCombination("CTRL+M"), false);
        barElement.addSubMenuItem(0, 0, "New Checkout...", event -> CheckoutMetaDialogs.checkOutPreMenu(checkout -> {memberTable.refresh();}, libraryReference), KeyCombination.keyCombination("CTRL+C"), false);
        barElement.addSubMenuItem(0, 0, "New Return...", event -> CheckoutMetaDialogs.checkInPreMenu(checkout -> {}, libraryReference), KeyCombination.keyCombination("CTRL+R"), false);
        barElement.addMenuItem(0, "Save", event -> System.out.println("Saved."), KeyCombination.keyCombination("CTRL+S"));
        barElement.addMenuItem(0, "Settings...");
        barElement.addMenuSeparator(0);
        barElement.addMenuItem(0, "_Exit", event -> ExitMetaDialogs.exitConfirm(), null);
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
