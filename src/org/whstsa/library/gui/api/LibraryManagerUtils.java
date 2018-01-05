package org.whstsa.library.gui.api;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Table;

import java.util.ArrayList;
import java.util.List;

public class LibraryManagerUtils {
    public static List<IPerson> returnPeople() {
        List<IPerson> people = new ArrayList<>();
        ObjectDelegate.getLibraries().get(0).getMembers().forEach(member -> people.add(member.getPerson()));//TODO Change getLibraris().get(0) to getLibrary(UUID)

        return people;
    }
    public static ObservableList<String> toObservableList(List<String> list) {
        return FXCollections.observableList(list);
    }

}
