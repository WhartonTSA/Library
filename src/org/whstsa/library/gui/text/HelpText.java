package org.whstsa.library.gui.text;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;

import java.util.ArrayList;
import java.util.List;

public class HelpText {

    private List<String> titles = new ArrayList<>();
    private  List<TextFlow> pages = new ArrayList<>();

    public HelpText() {
        titles.addAll(LibraryManagerUtils.asList("Help",
                "Main Menu",
                "Library Table",
                "Person Table",
                "Library Manager",
                "Member Table",
                "Book Table"
        ));
        this.pages = createPages();
    }

    public LabelElement getTitle(int pageIndex) {
        return GuiUtils.createTitle(titles.get(pageIndex));
    }

    public VBox getContent(int pageIndex) {
        LabelElement title = getTitle(pageIndex);
        TextFlow text = getPage(pageIndex);
        return GuiUtils.createVBox(5, title, text);
    }

    private TextFlow getPage(int pageIndex) {
        return this.pages.get(pageIndex);
    }

    public int getPageAmount() {
        return this.titles.size();
    }

    private static List<TextFlow> createPages() {
        List<TextFlow> textFlows = new ArrayList<>();
        textFlows.add(toTextFlow(toText("Help")));
        textFlows.add(toTextFlow(toText("Main")));
        textFlows.add(toTextFlow(toText("Library")));
        textFlows.add(toTextFlow(toText("Person")));
        textFlows.add(toTextFlow(toText("Manager")));
        textFlows.add(toTextFlow(toText("Member")));
        textFlows.add(toTextFlow(toText("Book")));
        return textFlows;
    }

    private static TextFlow toTextFlow(Text ...text) {
        return new TextFlow(text);
    }

    private static Text toText(String text) {
        return new Text(text);
    }

}
