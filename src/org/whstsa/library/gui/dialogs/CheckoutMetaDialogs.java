package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.NotEnoughMoneyException;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.impl.library.Checkout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

import java.util.Map;

public class CheckoutMetaDialogs {

    private static final String BOOK = "Books";

    public static void checkoutMember(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Checkout")
                .addChoiceBox(BOOK, LibraryManagerUtils.getBookTitles(libraryReference), true, -1)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            IBook book = LibraryManagerUtils.getBookFromTitle((String) results.get(BOOK).getResult(), libraryReference.poll());
            libraryReference.poll().reserveBook(member, book);//TODO Add try/catch

        });
        callback.callback(member);
    }

}
