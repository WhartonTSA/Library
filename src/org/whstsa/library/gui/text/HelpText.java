package org.whstsa.library.gui.text;

import javafx.scene.layout.VBox;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;

import java.util.ArrayList;
import java.util.List;

public class HelpText {

    private List<String> titles = new ArrayList<>();

    public HelpText() {
        titles.addAll(LibraryManagerUtils.asList("Help",
                "Main Menu",
                "Library Table",
                "Person Table",
                "Library Manager",
                "Member Table",
                "Book Table"
        ));
    }

    public LabelElement getTitle(int pageIndex) {
        return GuiUtils.createTitle(titles.get(pageIndex));
    }

    public VBox getContent(int pageIndex) {
        LabelElement title = getTitle(pageIndex);

        return GuiUtils.createVBox(title);
    }

}
