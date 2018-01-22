package org.whstsa.library.gui.api;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryManagerUtils {

    public static List<IPerson> returnPeople(ObservableReference<ILibrary> libraryReference) {
        List<IPerson> people = new ArrayList<>();
        libraryReference.poll().getMembers().forEach(member -> people.add(member.getPerson()));

        return people;
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

    public static List<String> getPeopleWithoutLibrary(ObservableReference<ILibrary> libraryReference) {
        List<IPerson> people = ObjectDelegate.getPeople().stream().filter(
                person -> person.getMemberships().stream().filter(
                        member -> member.getLibrary().equals(libraryReference.poll())).collect(Collectors.toList()).size() < 1).collect(Collectors.toList());
        return getNamesFromPeople(people);
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

}
