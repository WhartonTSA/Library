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
    private List<TextFlow> pages;

    public HelpText() {
        titles.addAll(LibraryManagerUtils.asList("Help",
                "Library Manager 1.0",
                "Main Menu",
                "Library Table",
                "Person Table",
                "Menu Bar",
                "Library Manager",
                "Member Table",
                "Book Table",
                "Simulate"
        ));
        this.pages = createPages();
    }

    private static List<TextFlow> createPages() {
        List<TextFlow> textFlows = new ArrayList<>();

        textFlows.add(toTextFlow(toText("" +
                "This help menu is the place to look for correct usages, possible fixes, or a tutorial for beginners to this application. \n")));
        textFlows.add(toTextFlow(toText("" +
                "Library Manager 1.0 is the first release of a tool designed for the administration of a library's members and books, " +
                "and handling of most of the member-to-staff actions. This application can display details of a books " +
                "title, author, copies, status, due date, and possessor, and display a member's name, rank, fines, and checked out books. " +
                "This application first requires the user to open a .json file, containing all the user's data. This application should have " +
                "already come packaged with one starting .json file (If you are reading this you have already found that file), " +
                "but you may create as many as you like. However, be careful, because deleting those files will " +
                "permanently delete all data contained in them. \n")));
        textFlows.add(toTextFlow(toText("" +
                "This is the main menu, where you can edit, open, or create a new library, or manage your local database of people. " +
                "The names you see in the right table are people who may have memberships to the libraries displayed on the left, and " +
                "to see these libraries you can select a library from the table and press the \"Open Library\" button, or simply " +
                "double click on the selected library. For more information see the following pages on the library and people tables. \n")));
        textFlows.add(toTextFlow(toText("" +
                "The library table on the left displays all of the libraries created by the user. This application distinguishes between a " +
                "library and a data file. A .json data file stores all or some the user's data, including one or many libraries, while " +
                "a library contains a single library's-worth of members and books. A new library can be created with the left topmost button, " +
                "and this library have its name edit or be deleted, with respective buttons. Note: Deleting a library is permanent. The bottommost " +
                "button allows you to open the library and change its members, books and more discussed in page 7. \n")));
        textFlows.add(toTextFlow(toText("" +
                "The person table on the right displays all of the people that are referenced in the libraries on the left. You can create, edit " +
                "or delete a person using the respective button on the right. Double-clicking on a selected member is a faster way to edit a person's name. \n")));
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
                "The Library Manager, this application's namesake, provides an interface to control the more complicated management of members and books, " +
                "and view detailed information on both. \n" +
                "\n Toolbar: \n" +
                "The vertical toolbar on the left contains all of the actions to manipulate your library. From top to bottom, the functions include: " +
                "returning to the main menu, switching between viewing the book table and the member table, checking out books to members, returning " +
                "books from members, creating, editing, filtering, and deleting members, creating, editing, filtering, and deleting books. \n" +
                "\n Tables: \n" +
                "The library manager contains two tables, the member table and book table, and displays one of the tables in the center of the window. " +
                "The viewed table can be switched with the toggle button at the top of the toolbar. Each table displays necessary information on each " +
                "book or member, and more detailed information can be found by double clicking on the selected book or member. More information on the tables can " +
                "be found on pages 8 and 9. \n" +
                "\n Checking out: \n" +
                "Books can be checked out to a member by selecting the intended member, clicking the Checkout button, selecting the desired books " +
                "from the table, and pressing the second checkout button in the upper blue bar. When selecting books, sometimes the number of books " +
                "displayed in the blue bar may be inaccurate. As long as you have selected the correct books, those books will be checked out " +
                "to the member regardless of the number displayed. \n" +
                "\n Returning books: \n" +
                "Books can be returned in a similar way to checking out books. Select the member, press the return button, select the books " +
                "the member is returning, and hit the second return button in the upper blue bar. The return interface may have the same problem " +
                "as the checkout interface, where the incorrect number of selected books appears in the upper blue bar. This is superficial, " +
                "and selecting the correct books will work regardless of the number displayed. \n")));
        textFlows.add(toTextFlow(toText("" +
                "The member lists information on the members of your library, such as name, books, rank and fines. The rank detail is for " +
                "determining if a member is a teacher or not. Name and rank can be changed with the Edit Member button in the toolbar. " +
                "A teacher is allowed to hold more books for longer than a regular member. If a member has an outstanding fine for failing to " +
                "return a book before its due date, that member cannot check out any new books before paying their fine and returning the book " +
                "or checking it out again. Double clicking on a selected member will display the member's individual books.\n")));
        textFlows.add(toTextFlow(toText("" +
                "The book member lists information on the books of your library such as title, author, genre, library copies, checked-out status, " +
                "and soonest due date. Title, author, and genre can be changed with the Edit Book button in the toolbar. Copies if the number of copies of " +
                "the same book the library has, and members can check out multiple copies, but a copy can be held by one member only. The checked out " +
                "column indicated whether all book copies in the library have been checked out, and as long as there is at least one copy of a book left, the cell will " +
                "display false. The due date column will display the soonest due date for one copy, and will display \"N/A\" if no copies are checked out. " +
                "Double clicking on a selected book will display detailed information for each copy in the library. In this view each copy will display " +
                "it's status, owner, and due date. The status will color the row of each copy. If available, the row will be green and, respectively, " +
                "checked out as yellow, checked out and reserved as orange, and unavailable as red. The unavailable status is reserved for situations " +
                "where a copy may be lost or damaged. \n")));
        textFlows.add(toTextFlow(toText("" +
                "The Simulate feature found inside the edit menu can be used for generating random data and actions for the library, such as members " +
                "checking out or returning books, fines being paid, new members or books being added, or books or members being removed. This is great " +
                "for testing as well as displaying the full potential of the application. NOTE: You may not want to save the simulated data, and there " +
                "is no undo button, so be careful to keep you data unsaved or turn off autosave and restart the application before simulating data. \n" +
                "\nSimulate Days\n" +
                "The Simulate Days menu item is the general simulator, and conducts almost all actions available in the application randomly for a " +
                "specified number of days. You can choose a library to simulate or simulate in all libraries. Simulate will never create new libraries. \n" +
                "\nAdvance Days\n" +
                "The advance days tool is mostly for simulating overdue dates and fee. Only the date will change, nothing else.\n" +
                "\nPopulate Members/Books\n" +
                "The populate tools can create a specified number of randomly generated members or books in a specified library.")));

        return textFlows;
    }

    private static TextFlow toTextFlow(Text... text) {
        return new TextFlow(text);
    }

    private static Text toText(String text) {
        return new Text(text);
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

}
