package org.whstsa.library.gui.text;

import javafx.scene.text.Text;

public class AboutText {

    private static final Text aboutTextContent = new Text("" +
            "Library Manager is an application designed for the administration of a library's members and books, " +
            "and handling of most of the member-to-personnel actions. This application can display details of a books " +
            "title, author, copies, status, due date, and possessor, and display a member's name, rank, fines, and checked out books. " +
            "If you are having trouble with the application, you can find assistance in the Help > Help... menu.");

    public static Text getText() {
        return aboutTextContent;
    }

}
