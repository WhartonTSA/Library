package org.whstsa.library.gui.factories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.util.Logger;

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

    public static List<IPerson> returnPeople(ObservableReference<ILibrary> libraryReference) {
        List<IPerson> people = new ArrayList<>();
        libraryReference.poll().getMembers().forEach(member -> people.add(member.getPerson()));

        return people;
    }

    public static List<String> asList(String ...items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    public static ObservableList<String>  getMemberNames(ObservableReference<ILibrary> libraryReference) {
        List<String> members = new ArrayList<>();
        libraryReference.poll().getMembers().forEach(member -> members.add(member.getName()));

        return toObservableList(members);
    }

    public static ObservableList<String>  getPeopleNames() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < ObjectDelegate.getPeople().size(); i++) {
            list.add(ObjectDelegate.getPeople().get(i).getName());
        }
        return toObservableList(list);
    }

    public static List<String> getNamesFromPeople(List<IPerson> people) {
        List<String> names = new ArrayList<>();
        people.forEach(person -> names.add(person.getName()));
        return names;
    }

    public static List<String> getNamesFromMembers(List<IMember> member) {
        List<String> names = new ArrayList<>();
        member.forEach(memberI -> names.add(memberI.getName()));
        return names;
    }

    public static IPerson getPersonFromName(String name) {
        Map<Integer, IPerson> scores = new HashMap<>();
        ObjectDelegate.getPeople().forEach(person -> {
            int score = 0;
            String comparingName = person.getName();
            if (name.equals(comparingName)) {
                score += 10;
            } else if (name.equalsIgnoreCase(comparingName)) {
                score += 8;
            }

            if (name.contains(comparingName)) {
                score += 3;
            } else if (name.toLowerCase().contains(comparingName.toLowerCase())) {
                score += 2;
            }

            if (score > 0) {
                scores.put(score, person);
            }
        });

        Optional<Map.Entry<Integer, IPerson>> personEntry = scores.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).findFirst();
        if (personEntry.isPresent()) {
            return personEntry.get().getValue();
        }

        return null;
    }

    public static List<IPerson> getPeopleWithoutLibrary(ILibrary library) {
        return ObjectDelegate.getPeople().stream().filter(person -> !library.hasMember(person)).collect(Collectors.toList());
    }

    public static List<IPerson> getPeopleWithoutLibrary(ObservableReference<ILibrary> libraryReference) {//returns a list of people that do not have a membership with the library referenced
        return getPeopleWithoutLibrary(libraryReference.poll());
    }

    public static ObservableList<String>  getBookTitlesFromList(ObservableList<IBook> books) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            list.add(books.get(i).getTitle());
        }
        return toObservableList(list);
    }

    public static IMember getMemberFromLibrary(IPerson person, ILibrary library) {
        for (int i = 0; i < library.getMembers().size(); i++) {
            if (library.getMembers().get(i).getPerson().equals(person)) {
                return library.getMembers().get(i);
            }
        }
        return null;
    }

    public static IMember getMemberFromName(String name, ILibrary library) {
        for (int i = 0; i < library.getMembers().size(); i++) {
            if (library.getMembers().get(i).getName().equals(name)) {
                return library.getMembers().get(i);
            }
        }
        return null;
    }

    public static IBook getBookFromTitle(String title, ILibrary library) {
        for (int i = 0; i < library.getBooks().size(); i++) {
            if (library.getBooks().get(i).getTitle().equals(title)) {
                return library.getBooks().get(i);
            }
        }
        return null;
    }

    public static ObservableList<String> getBookTitlesFromMember(IMember member) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < member.getBooks().size(); i++) {
            list.add(member.getBooks().get(i).getTitle());
        }
        return toObservableList(list);
    }

    public static ObservableList<String> getBookTitles(ObservableReference<ILibrary> libraryReference) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < libraryReference.poll().getBooks().size(); i++) {
            list.add(libraryReference.poll().getBooks().get(i).getTitle());
        }
        return toObservableList(list);
    }

    public static ObservableList<String> toObservableList(List<String> list) {
        return FXCollections.observableList(list);
    }

    public static List<Integer> fromObservableListInt(ObservableList<Integer> list) {
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

    public static List<IBook> fromObservableListIBook(ObservableList<IBook> list) {
        List<IBook> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

}
