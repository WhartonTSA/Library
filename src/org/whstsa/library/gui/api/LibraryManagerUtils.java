package org.whstsa.library.gui.api;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;

public class LibraryManagerUtils {

    public static List<IPerson> returnPeople() {
        List<IPerson> people = new ArrayList<>();
        ObjectDelegate.getLibraries().get(0).getMembers().forEach(member -> people.add(member.getPerson()));//TODO Change getLibraries().get(0) to getLibrary(UUID)

        return people;
    }

    public static ObservableList<String>  getMemberNames(ILibrary library) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < library.getMembers().size(); i++) {
            list.add(library.getMembers().get(i).getName());
        }
        return toObservableList(list);
    }

    public static ObservableList<String> getBookTitles(ILibrary library) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i <library.getBooks().size(); i++) {
            list.add(library.getBooks().get(i).getTitle());
        }
        return toObservableList(list);
    }

    public static ObservableList<String> toObservableList(List<String> list) {
        return FXCollections.observableList(list);
    }

}
