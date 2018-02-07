package org.whstsa.library.gui.factories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.Identifiable;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.books.IBookContainerReadonly;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.util.ListUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LibraryManagerUtils {

    public static HBox createTitleBar(String title) {
        HBox box = new HBox();
        box.getChildren().add(GuiUtils.createLabel(title, 20));
        box.setId("titlebar");
        box.setAlignment(Pos.CENTER);
        return box;
    }

    public static void addTooltip(Node node, String text) {
        Tooltip tooltip = new Tooltip(text);
        Tooltip.install(node, tooltip);
    }

    public static List<String> asList(String ...items) {
        return Arrays.asList(items);
    }

    public static ObservableList<String> getMemberNames(ILibrary library) {
        return toObservableList(getNames(library.getMembers()));
    }

    public static ObservableList<String> getPeopleNames() {
        return toObservableList(getNames(ObjectDelegate.getPeople()));
    }

    public static List<String> getNames(List<? extends Identifiable> people) {
        return people.stream().map(Identifiable::getName).collect(Collectors.toList());
    }

    public static IPerson getPersonFromName(String name) {
        return ListUtils.findIdentifiable(ObjectDelegate.getPeople(), name);
    }

    public static List<IPerson> getPeopleWithoutLibrary(ILibrary library) {
        return ObjectDelegate.getPeople().stream().filter(person -> !library.hasMember(person)).collect(Collectors.toList());
    }

    public static ObservableList<String> getBookTitlesFromList(ObservableList<IBook> books) {
        return toObservableList(books.stream().map(IBook::getName).collect(Collectors.toList()));
    }

    public static IMember getMemberFromName(String name, ILibrary library) {
        IPerson person = getPersonFromName(name);
        return library.getMember(person);
    }

    public static IBook getBookFromTitle(String title, ILibrary library) {
        return ListUtils.findIdentifiable(library.getBooks(), title);
    }

    public static ObservableList<String> getBookTitles(IBookContainerReadonly bookContainer) {
        return toObservableList(bookContainer.getBooks().stream().map(IBook::getName).collect(Collectors.toList()));
    }

    public static ObservableList<String> toObservableList(Collection<String> list) {
        return FXCollections.observableArrayList(list);
    }

    public static <T> List<T> fromObservableList(ObservableList<T> list) {
        return list.stream().collect(Collectors.toList());
    }

}
