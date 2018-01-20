package org.whstsa.library.gui.dialogs;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.CheckedInException;
import org.whstsa.library.api.exceptions.MemberMismatchException;
import org.whstsa.library.api.exceptions.NotEnoughMoneyException;
import org.whstsa.library.api.exceptions.OutstandingFinesException;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutMetaDialogs {

    private static final String BOOK = "Checkout Books";
    private static final String RETURN = "Return Books";
    private static final String PAYFINE = "Pay Fine";

    public static void checkoutMember(Callback<IMember> callback, IMember member, BorderPane mainContainer, TableView bookTable, ObservableReference<ILibrary> libraryReference) {

        Button checkoutButton = GuiUtils.createButton("Checkout", true, GuiUtils.defaultClickHandler());

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(checkoutButton, GuiUtils.createTextFlow("checkout", 14, "-fx-base: #000000;",
                "Checking out ",
                "0",
                " books to ",
                member.getName() + "."));
        mainContainer.setTop(toolBar);
        toolBar.setStyle("-fx-base: #d1e3ff;");
        checkoutButton.setStyle("fx-base: #dddddd;");
        bookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /*TableColumn<IBook, Boolean> selectionColumn = new TableColumn<>( "Loaded" );
        selectionColumn.setCellFactory(CheckBoxTableCell.forTableColumn(call -> {
            System.out.println("Event");
            return new ReadOnlyObjectWrapper<>(false);
        }));
        selectionColumn.setEditable(true);
        selectionColumn.setMinWidth(60);
        bookTable.getColumns().add(selectionColumn);

        bookTable.setEditable(true);*/

        bookTable.getSelectionModel().selectionModeProperty().addListener((obs, oldSelection, newSelection) -> {
            toolBar.getItems().set(1, GuiUtils.createTextFlow("checkout", 14, "-fx-base: #000000;",
                    "Checking out ",
                    bookTable.getSelectionModel().getSelectedIndices().size() + "",
                    " books to ",
                    member.getName() + "."));
        });

        mainContainer.setCenter(bookTable);

        checkoutButton.setOnMouseClicked(event -> {
            List<Integer> selectedRows = LibraryManagerUtils.fromObservableListInt(bookTable.getSelectionModel().getSelectedIndices());
            for (int i = 0; i < i; i++) {
                if (member.getFine() > 0) {
                    if (false) {
                        DialogUtils.createDialog("Couldn't pay fine. Member does not have enough money.", null, null, Alert.AlertType.ERROR).show();
                        return;
                    }
                    try {
                        member.getCheckouts().forEach(checkout -> checkout.getFine());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                IBook book = LibraryManagerUtils.getBookFromTitle("what", libraryReference.poll());
                try {
                    libraryReference.poll().reserveBook(member, book);
                    callback.callback(member);
                } catch (Exception ex) {
                    DialogUtils.createDialog("There was an error.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                }
            }
            mainContainer.setTop(null);
            bookTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        });


        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Checking out " + member.getName() + ".")
                .addChoiceBox(BOOK, LibraryManagerUtils.getBookTitles(libraryReference), true, -1)
                .addCheckBox(PAYFINE, false, true, member.getFine() <= 0)
                .build();
        if (member.getFine() > 0) {
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            LabelElement fineLabel = GuiUtils.createLabel("$" + member.getFine(), 12, Pos.CENTER_RIGHT);
            fineLabel.setTextFill(Color.RED);
            dialogPane.add(fineLabel, 1, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (member.getFine() > 0) {
                if (!results.get(PAYFINE).getBoolean()) {
                    DialogUtils.createDialog("Couldn't pay fine. Member does not have enough money.", null, null, Alert.AlertType.ERROR).show();
                    return;
                }
                try {
                    member.getCheckouts().forEach(checkout -> checkout.getFine());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            IBook book = LibraryManagerUtils.getBookFromTitle((String) results.get(BOOK).getResult(), libraryReference.poll());
            try {
                libraryReference.poll().reserveBook(member, book);
                callback.callback(member);
            } catch (Exception ex) {
                DialogUtils.createDialog("There was an error.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
            }
        });
    }

    public static void checkinMember(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Checking in " + member.getName() + ".")
                .addChoiceBox(RETURN, member.getCheckoutMap(), true, -1)
                .addCheckBox("Pay Fine", false, true, member.getFine() <= 0, event -> {
                    member.getCheckouts().stream().filter(checkout -> checkout.getFine() > 0).forEach(checkout -> {
                        try {
                            checkout.payFine();
                        } catch (NotEnoughMoneyException ex) {
                            DialogUtils.createDialog("Couldn't pay fine. Member does not have enough money.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                        }
                    });
                })
                .build();
        if (member.getFine() > 0) {
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            LabelElement fineLabel = GuiUtils.createLabel("$" + member.getFine(), 12, Pos.CENTER_RIGHT);
            fineLabel.setTextFill(Color.RED);
            dialogPane.add(fineLabel, 1, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(RETURN).getResult() != null) {
                IBook returnBook = LibraryManagerUtils.getBookFromTitle((String) results.get(RETURN).getResult(), libraryReference.poll());
                List<ICheckout> checkouts = member.getCheckouts(true);
                List<ICheckout> matches = checkouts.stream().filter(checkout -> checkout.getBook().equals(returnBook)).collect(Collectors.toList());//TODO checkout getter
                if (matches.size() == 0) {
                    DialogUtils.createDialog("Error.", "Checkout does not exist", null, Alert.AlertType.ERROR).show();
                    return;
                }
                ICheckout checkout = matches.get(0);
                try {
                    checkout.getOwner().checkIn(checkout);
                    member.removeBook(checkout.getBook());
                    callback.callback(member);
                } catch (OutstandingFinesException | MemberMismatchException | CheckedInException e) {
                    DialogUtils.createDialog("Error.", e.getMessage(), null, Alert.AlertType.ERROR).show();
                }
            }

        });
    }
}
