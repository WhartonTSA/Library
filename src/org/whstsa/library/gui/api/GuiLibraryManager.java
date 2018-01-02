package org.whstsa.library.gui.api;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.factories.GuiUtils;

import java.awt.*;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static org.whstsa.library.gui.components.tables.DatabaseManagementTables.bookManagerTable;
import static org.whstsa.library.gui.components.tables.DatabaseManagementTables.memberManagerTable;

public class GuiLibraryManager implements Gui {

    @Override
    public Scene draw() {
        //Toggle Button Group
        ToggleGroup viewButtons = new ToggleGroup();

        ToggleButton viewMembers = GuiUtils.createToggleButton("Members");
            viewMembers.setUserData(true);
            viewMembers.setToggleGroup(viewButtons);
            viewMembers.setSelected(true);
        ToggleButton viewBooks = GuiUtils.createToggleButton("Books");
            viewBooks.setToggleGroup(viewButtons);
            viewBooks.setUserData(false);


        HBox viewSwitch = GuiUtils.createHBox(2, viewMembers, viewBooks);

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

        VBox buttonGroup = GuiUtils.createVBox(15, viewSwitch, membersLabel, memberNew, memberList, memberSearch, memberDelete, booksLabel, bookAdd, bookList, bookDelete, bookSearch, settingsButton);
        buttonGroup.setSpacing(5.0);

        BorderPane mainContainer = GuiUtils.createBorderPane(GuiUtils.Direction.LEFTHAND, memberManagerTable().getTable(), buttonGroup);

        viewButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    return;
                }
                else {
                    if ((boolean) viewButtons.getSelectedToggle().getUserData()) {
                        mainContainer.getChildren().remove(0);
                        mainContainer.getChildren().add(memberManagerTable().getTable());
                    }
                    else {
                        mainContainer.getChildren().remove(0);
                        mainContainer.getChildren().add(bookManagerTable().getTable());
                    }
                }

            }
        });

        return new Scene(mainContainer, 1200, 800);
    }

    @Override
    public String getUUID() {
        return "GUI_LIBRARY_MANAGER";
    }

}
