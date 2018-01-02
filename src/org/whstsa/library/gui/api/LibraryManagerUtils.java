package org.whstsa.library.gui.api;

import javafx.collections.FXCollections;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;

public class LibraryManagerUtils {
    public static List<IPerson> returnPeople() {
        List<IPerson> people = new ArrayList<>();
        ObjectDelegate.getLibraries().get(0).getMembers().forEach(member -> people.add(member.getPerson()));

        return people;
    }
}
