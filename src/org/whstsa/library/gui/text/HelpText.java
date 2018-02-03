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
    private  List<TextFlow> pages;

    public HelpText() {
        titles.addAll(LibraryManagerUtils.asList("Help",
                "Library Manager 1.0",
                "Main Menu",
                "Library Table",
                "Person Table",
                "Menu Bar",
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

        textFlows.add(toTextFlow(toText("" +
                "This help menu is the place to look for correct usages, possible fixes, or a tutorial for beginners to this application. ")));
        textFlows.add(toTextFlow(toText("" +
                "Library Manager 1.0 is the first release of a tool designed for the administration of a library's members and books, " +
                "and handling of most of the member-to-staff actions. This application can display details of a books " +
                "title, author, copies, status, due date, and possessor, and display a member's name, rank, fines, and checked out books. " +
                "This application first requires the user to open a .json file, containing all the user's data. This application should have " +
                "already come packaged with one starting .json file (If you are reading this you have already found that file), " +
                "but you may create as many as you like. However, be careful, because deleting those files will " +
                "permanently delete all data contained in them.")));
        textFlows.add(toTextFlow(toText("" +
                "This is the main menu, where you can edit, open, or create a new library, or manage your local database of people. " +
                "The names you see in the right table are people who may have memberships to the libraries displayed on the left, and " +
                "to see these libraries you can select a library from the table and press the \"Open Library\" button, or simply " +
                "double click on the selected library. For more information see the following pages on the library and people tables. ")));
        textFlows.add(toTextFlow(toText("" +
                "The library table on the left displays all of the libraries created by the user. This application distinguishes between a " +
                "library and a data file. A .json data file stores all or some the user's data, including one or many libraries, while " +
                "a library contains a single library's-worth of members and books. A new library can be created with the left topmost button, " +
                "and this library have its name edit or be deleted, with respective buttons. Note: Deleting a library is permanent. The bottommost " +
                "button allows you to open the library and change its members, books and more discussed in page 7.")));
        textFlows.add(toTextFlow(toText("" +
                "The person table on the right displays all of the people that are referenced in the libraries on the left. You can create, edit " +
                "or delete a person using the respective button on the right. Double-clicking on a selected member is a faster way to edit a person's name. ")));
        textFlows.add(toTextFlow(toText("" +
                "The Menu Bar is displayed above both the main menu and the library manager (See page 7) and allows a simple interface " +
                "to conduct most actions already available in the main interface. The Menu Bar also includes options such as save, exit, " +
                "simulate,and the about and help pages under the help menu. \n" +
                "\n  File: \n" +
                "The File menu lists options for saving and exiting the application, as well as some actions for creating a library or person. " +
                "Many of the File > New options are disabled, and only functional inside the Library Manager window, discussed in page 7. \n" +
                "\n  Edit: \n" +
                "The Edit menu lists options mainly geared towards developers or testers. They allow the user to directly edit the .json " +
                "data file or simulate a day of library-member interaction. The simulate button is only useable inside the Library Manager " +
                "window, discussed in page 7. \n" +
                "\n  Help: \n" +
                "The Help menu lists options that allow you to understand the application better. The About page gives a quick overview of the " +
                "purpose of this application, and lists information about the creators and licensing. The Help menu (which you have already found) " +
                "gives tips and tutorials to users. \n")));
        textFlows.add(toTextFlow(toText("" +
                "The Library Manager, this application's namesake, allows you to manage the more complicated management of members and books, " +
                "and view detailed information on both.")));
        textFlows.add(toTextFlow(toText("" +
                "Member")));
        textFlows.add(toTextFlow(toText("" +
                "Book")));

        return textFlows;
    }

    private static TextFlow toTextFlow(Text ...text) {
        return new TextFlow(text);
    }

    private static Text toText(String text) {
        return new Text(text);
    }

}
