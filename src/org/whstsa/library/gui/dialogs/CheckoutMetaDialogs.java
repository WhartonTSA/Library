package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

import java.util.Map;

public class CheckoutMetaDialogs {

    private static final String NAME = "Name";
    private static final String BOOK = "Books";

    public static void checkoutMember(Callback<IPerson> callback) {//TODO Add optional IMember
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Checkout")
                .addChoiceBox(NAME, LibraryManagerUtils.getMemberNames(ObjectDelegate.getLibraries().get(0)), true)
                .addChoiceBox(BOOK, LibraryManagerUtils.getBookTitles(ObjectDelegate.getLibraries().get(0)), true)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String name = results.get(NAME).getString();
            String bookTitle = results.get(BOOK).getString();
            ObjectDelegate.getLibraries().get(0).getMembers();
            ObjectDelegate.getBooks();
        }, NAME, BOOK);
    }

}
