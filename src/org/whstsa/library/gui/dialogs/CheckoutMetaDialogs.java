package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.MemberMismatchException;
import org.whstsa.library.api.exceptions.OutstandingFinesException;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.TextFieldElement;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckoutMetaDialogs {

    private static String CHECKOUT = "Checkout";
    private static String PAYFINE = "Pay Fine";
    private static String BOOK = "Book";
    private static String RETURN = "Return";
    private static String QUANTITY = "Quantity";


    public static void checkoutMember(Callback<IMember> callback, IMember member, BorderPane mainContainer, Table<IBook> bookTable, ToggleButton viewBooks, ToggleButton viewMembers, ObservableReference<ILibrary> libraryReference) {

        viewBooks.setDisable(true);
        viewMembers.setDisable(true);

        LibraryManagerUtils.addTooltip(bookTable.getTable(), "CTRL + Click to select multiple books");

        Button checkoutButton = GuiUtils.createButton("Checkout", true, GuiUtils.defaultClickHandler());

        LabelElement spacer = null;
        TextFlow fineLabel = null;
        CheckBox payFine = null;
        final boolean fine = member.getFine() > 0;

        if (member.getFine() > 0) {//If member has an outstanding fine, will give user option to pay fine
            spacer = GuiUtils.createLabel("      ");
            fineLabel = GuiUtils.createTextFlow("Fine", 14, "", "Outstanding fine of ", "$" + member.getFine(), ". Pay fine?");
            ((Text) fineLabel.getChildren().get(1)).setFill(Color.RED);
            payFine = GuiUtils.createCheckBox("", false);
            payFine.selectedProperty().addListener((observable, oldValue, newValue) -> checkoutButton.setDisable(!newValue));
            checkoutButton.setDisable(true);
        }

        HBox mainSpacer = new HBox(new Label(""));
        HBox.setHgrow(mainSpacer, Priority.ALWAYS);//HBox that always grows to maximum width, keeps X button on right side of toolBar

        Button closeButton = GuiUtils.createButton("X", false, 5, Pos.CENTER_RIGHT, event -> {//TODO Ugly close button
            ((VBox) mainContainer.getTop()).getChildren().set(1, new HBox());
            bookTable.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            mainContainer.setCenter(bookTable.getTable());
            viewBooks.setDisable(true);
            viewBooks.setSelected(true);
            viewMembers.setDisable(false);
            viewMembers.setSelected(false);
        });

        ToolBar toolBar = new ToolBar();
        if (fine) {
            toolBar.getItems().addAll(checkoutButton,
                    GuiUtils.createTextFlow("checkout", 15, "-fx-base: #1e1e1e;", "Checking out ", "0", " books to ", member.getName() + "."),
                    spacer,
                    fineLabel,
                    payFine,
                    mainSpacer,
                    closeButton);
        } else {
            toolBar.getItems().addAll(checkoutButton,
                    GuiUtils.createTextFlow("checkout", 15, "-fx-base: #1e1e1e;", "Checking out ", "0", " books to ", member.getName() + "."),
                    mainSpacer,
                    closeButton);
        }
        toolBar.setId("toolbar");
        ((VBox) mainContainer.getTop()).getChildren().set(1, toolBar);
        toolBar.setBackground(new Background(new BackgroundFill(Color.web("#d1e3ff"), null, null)));
        checkoutButton.setStyle("-fx-base: #4fa9dd;");
        closeButton.setStyle("-fx-base: #ff8787;");

        bookTable.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        bookTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
                toolBar.getItems().set(1, GuiUtils.createTextFlow("checkout", 15, "-fx-base: #000000;",
                        "Checking out ",
                        bookTable.getTable().getSelectionModel().getSelectedIndices().size() + "",
                        " books to ",
                        member.getName() + "."))
        );

        mainContainer.setCenter(bookTable.getTable());

        checkoutButton.setOnMouseClicked(event -> {
            viewBooks.setDisable(true);
            viewMembers.setDisable(false);
            ObservableList<IBook> selectedBooks = bookTable.getTable().getSelectionModel().getSelectedItems();
            if (fine) {
                Stream<ICheckout> checkouts = member.getCheckouts().stream().filter(ICheckout::isOverdue).filter(checkout -> !checkout.isReturned());
                checkouts.forEach(ICheckout::payFine);
            }
            selectedBooks.forEach(book -> {
                try {
                    libraryReference.poll().reserveBook(member, book, 5);
                    Logger.DEFAULT_LOGGER.debug("Checking out " + book.getName() + " to " + member.getName() + ".");
                } catch (Exception ex) {
                    DialogUtils.createDialog("There was an error.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                }
            });
            callback.callback(member);
            bookTable.refresh();
            ((VBox) mainContainer.getTop()).getChildren().set(1, new HBox());
            bookTable.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        });
    }

    public static void checkOutPreMenu(Callback<IMember> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Choose a member to checkout")
                .addChoiceBox(CHECKOUT, LibraryManagerUtils.getMemberNames(libraryReference.poll()), true, 0)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(CHECKOUT).getResult() != null) {
                IMember selectedMember = LibraryManagerUtils.getMemberFromName((String) results.get(CHECKOUT).getResult(), libraryReference.poll());
                assert selectedMember != null;
                checkoutMemberDialog(member -> callback.callback(selectedMember), selectedMember, libraryReference);
            }
        });
    }

    private static void checkoutMemberDialog(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Checking out " + member.getName() + ".")
                .addChoiceBox(BOOK, LibraryManagerUtils.getBookTitles(libraryReference.poll()), true, -1)
                .addCheckBox(PAYFINE, false, true, member.getFine() <= 0)
                .build();
        if (member.getFine() > 0) {
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            LabelElement fineLabel = GuiUtils.createLabel("$" + member.getFine(), 12);
            fineLabel.setTextFill(Color.RED);
            dialogPane.add(fineLabel, 1, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (member.getFine() > 0) {
                if (!results.get(PAYFINE).getBoolean()) {
                    DialogUtils.createDialog("Couldn't pay fine.", null, null, Alert.AlertType.ERROR).show();
                    return;
                }
                try {
                    member.getCheckouts().forEach(ICheckout::getFine);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            IBook book = LibraryManagerUtils.getBookFromTitle((String) results.get(BOOK).getResult(), libraryReference.poll());
            Integer quantity = null;
            Element quantityElement = results.get(QUANTITY);
            if (quantityElement instanceof TextFieldElement) {
                TextFieldElement textFieldQuantityElement = (TextFieldElement) quantityElement;
                quantity = textFieldQuantityElement.getNumber();
            }
            if (quantity == null) {
                quantity = 1;
            }
            try {
                libraryReference.poll().reserveBook(member, book, quantity);
                callback.callback(member);
            } catch (Exception ex) {
                DialogUtils.createDialog("There was an error.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
            }
        });
    }

    public static void checkinMember(Callback<IMember> callback, IMember member, BorderPane mainContainer, Table<IBook> bookTable, ToggleButton viewBooks, ToggleButton viewMembers, ObservableReference<ILibrary> libraryReference) {

        viewBooks.setDisable(true);
        viewMembers.setDisable(true);

        Button checkinButton = GuiUtils.createButton("Return", true, GuiUtils.defaultClickHandler());

        LabelElement spacer = null;
        TextFlow fineLabel = null;
        CheckBox payFine = null;
        final boolean fine = member.getFine() > 0;

        if (member.getFine() > 0) {//If member has an outstanding fine, will give user option to pay fine
            spacer = GuiUtils.createLabel("      ");
            fineLabel = GuiUtils.createTextFlow("Fine", 14, "", "Outstanding fine of ", "$" + member.getFine(), ". Pay fine?");
            ((Text) fineLabel.getChildren().get(1)).setFill(Color.RED);
            payFine = GuiUtils.createCheckBox("", false);
        }

        Table<ICheckout> mainTable = new Table<>();

        LibraryManagerUtils.addTooltip(mainTable.getTable(), "CTRL + Click to select multiple books");

        mainTable.addColumn("Title", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getName()), true, TableColumn.SortType.DESCENDING, 200);
        mainTable.addColumn("Author", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getAuthorName()), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Genre", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getType().getGenre()), true, TableColumn.SortType.DESCENDING, 50);//TODO needs better fields for table columns
        ObservableReference<List<ICheckout>> observableReference = member::getCheckouts;
        mainTable.setReference(observableReference);

        HBox mainSpacer = new HBox(new Label(""));
        HBox.setHgrow(mainSpacer, Priority.ALWAYS);//HBox that always grows to maximum width, keeps X button on right side of toolBar

        Button closeButton = GuiUtils.createButton("X", false, 5, Pos.CENTER_RIGHT, event -> {//TODO Ugly close button
            ((VBox) mainContainer.getTop()).getChildren().set(1, new HBox());
            mainContainer.setCenter(bookTable.getTable());
            viewBooks.setDisable(true);
            viewBooks.setSelected(true);
            viewMembers.setDisable(false);
            viewMembers.setSelected(false);
        });

        ToolBar toolBar = new ToolBar();
        if (fine) {
            toolBar.getItems().addAll(checkinButton,
                    GuiUtils.createTextFlow("checkin", 15, "-fx-base: #1e1e1e;", "Returning ", "0", " books from ", member.getName() + "."),
                    spacer,
                    fineLabel,
                    payFine,
                    mainSpacer,
                    closeButton);
        } else {
            toolBar.getItems().addAll(checkinButton,
                    GuiUtils.createTextFlow("checkin", 15, "-fx-base: #1e1e1e;", "Returning ", "0", " books from ", member.getName() + "."),
                    mainSpacer,
                    closeButton);
        }
        toolBar.setId("toolbar");
        ((VBox) mainContainer.getTop()).getChildren().set(1, toolBar);
        toolBar.setBackground(new Background(new BackgroundFill(Color.web("#d1e3ff"), null, null)));
        checkinButton.setStyle("-fx-base: #4fa9dd;");
        closeButton.setStyle("-fx-base: #ff8787;");

        mainTable.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        mainTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
                toolBar.getItems().set(1, GuiUtils.createTextFlow("checkout", 15, "-fx-base: #000000;",
                        "Returning ",
                        mainTable.getTable().getSelectionModel().getSelectedIndices().size() + "",
                        " books from ",
                        member.getName() + "."))
        );
        mainTable.getTable().setId("returnTable");
        mainContainer.setCenter(mainTable.getTable());

        checkinButton.setOnMouseClicked(event -> {
            viewBooks.setDisable(true);
            viewMembers.setDisable(false);
            viewBooks.setSelected(true);
            viewMembers.setSelected(false);
            ObservableList<ICheckout> selectedCheckouts = mainTable.getTable().getSelectionModel().getSelectedItems();
            if (fine) {
                Stream<ICheckout> checkouts = member.getCheckouts().stream().filter(ICheckout::isOverdue).filter(checkout -> !checkout.isReturned());
                checkouts.forEach(ICheckout::payFine);
            }
            selectedCheckouts.forEach(returnBook -> {
                try {
                    member.returnCheckout(returnBook);
                } catch (OutstandingFinesException | MemberMismatchException e) {
                    DialogUtils.createDialog("Error.", e.getMessage(), null, Alert.AlertType.ERROR).show();
                }
            });
            callback.callback(member);
            mainContainer.setCenter(bookTable.getTable());
            ((VBox) mainContainer.getTop()).getChildren().set(1, new HBox());
        });
    }

    public static void checkInPreMenu(Callback<IMember> callback, ObservableReference<ILibrary> libraryReference) {

        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Choose a member to checkin")
                .addRequiredChoiceBox(RETURN, LibraryManagerUtils.getMemberNames(libraryReference.poll()), true, 0, false)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(RETURN).getResult() != null) {
                IMember selectedMember = LibraryManagerUtils.getMemberFromName((String) results.get(RETURN).getResult(), libraryReference.poll());
                checkinMemberDialog(member -> callback.callback(selectedMember), selectedMember, libraryReference);
            }
        });
    }

    private static void checkinMemberDialog(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Returning " + member.getName() + "'s books.")
                .addRequiredChoiceBox(RETURN, member.getCheckoutMap(), true, -1, false)
                .addCheckBox("Pay Fine", false, true, member.getFine() <= 0, event ->
                        member.getCheckouts().stream().filter(checkout -> checkout.getFine() > 0).forEach(ICheckout::payFine))
                .build();
        if (member.getFine() > 0) {
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            LabelElement fineLabel = GuiUtils.createLabel("$" + member.getFine(), 12);
            fineLabel.setTextFill(Color.RED);
            dialogPane.add(fineLabel, 1, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(RETURN).getResult() != null) {
                IBook returnBook = LibraryManagerUtils.getBookFromTitle((String) results.get(RETURN).getResult(), libraryReference.poll());
                List<ICheckout> checkouts = member.getCheckouts(true);
                List<ICheckout> matches = checkouts.stream().filter(checkout -> checkout.getBook().equals(returnBook)).collect(Collectors.toList());
                if (matches.size() == 0) {
                    DialogUtils.createDialog("Error.", "Checkout does not exist", null, Alert.AlertType.ERROR).show();
                    return;
                }
                ICheckout checkout = matches.get(0);
                try {
                    member.returnCheckout(checkout);
                    callback.callback(member);
                } catch (OutstandingFinesException | MemberMismatchException e) {
                    DialogUtils.createDialog("Error.", e.getMessage(), null, Alert.AlertType.ERROR).show();
                }
            }

        });
    }
}
