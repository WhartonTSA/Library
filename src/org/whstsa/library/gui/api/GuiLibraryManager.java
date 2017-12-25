package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.gui.factories.GuiUtils;

public class GuiLibraryManager implements Gui {

    @Override
    public Scene draw() {

        TableView mainTable = new TableView();

        Label membersLabel = GuiUtils.createLabel("Members", 16);
        Button memberNew = GuiUtils.createButton("New", GuiUtils.defaultClickHandler());
        Button memberList = GuiUtils.createButton("List", GuiUtils.defaultClickHandler());
        Button memberSearch = GuiUtils.createButton("Search", GuiUtils.defaultClickHandler());
        Button memberDelete = GuiUtils.createButton("Delete", GuiUtils.defaultClickHandler());


        Label booksLabel = GuiUtils.createLabel("Books", 16);
        Button bookAdd = GuiUtils.createButton("Add", GuiUtils.defaultClickHandler());
        Button bookList = GuiUtils.createButton("List", GuiUtils.defaultClickHandler());
        Button bookDelete = GuiUtils.createButton("Delete", GuiUtils.defaultClickHandler());
        Button bookSearch = GuiUtils.createButton("Search", GuiUtils.defaultClickHandler());

        Button settingsButton = GuiUtils.createButton("Settings", GuiUtils.defaultClickHandler());

        VBox buttonGroup = GuiUtils.createVBox(15, membersLabel, memberNew, memberList, memberSearch, memberDelete, booksLabel, bookAdd, bookList, bookDelete, bookSearch, settingsButton);
        buttonGroup.setSpacing(5.0);

        BorderPane mainContainer = GuiUtils.createBorderPane(GuiUtils.Direction.LEFTHAND, mainTable, buttonGroup);

        return new Scene(mainContainer, 1200, 800);
    }

    @Override
    public String getUUID() {
        return "GUI_LIBRARY_MANAGER";
    }

}
