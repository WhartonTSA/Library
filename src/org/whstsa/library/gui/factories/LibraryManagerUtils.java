package org.whstsa.library.gui.factories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryManagerUtils {

    public static List<IPerson> returnPeople(ILibrary library) {
        List<IPerson> people = new ArrayList<>();
        library.getMembers().forEach(member -> people.add(member.getPerson()));
        return people;
    }

    public static ObservableList<String> getMemberNames(ILibrary library) {
        List<String> members = new ArrayList<>();
        library.getMembers().forEach(member -> members.add(member.getName()));
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

    public static IPerson getPersonFromName(String name) {
        for (int i = 0; i < ObjectDelegate.getPeople().size(); i++) {
            if (ObjectDelegate.getPeople().get(i).getName().equals(name)) {
                return ObjectDelegate.getPeople().get(i);
            }
        }
        return null;
    }

    public static List<String> getPeopleWithoutLibrary(ILibrary library) {//returns a list of people that do not have a membership with the
        List<IPerson> libraryPeople = library.getPeople();
        List<IPerson> people = ObjectDelegate.getPeople().stream().filter(person -> !libraryPeople.contains(person)).collect(Collectors.toList());
        return getNamesFromPeople(people);
    }

    public static ObservableList<String>  getBookTitlesFromList(ObservableList<IBook> books) {
        return toObservableList(books.stream().map(IBook::getTitle).collect(Collectors.toList()));
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
        return toObservableList(member.getBooks().stream().map(IBook::getTitle).collect(Collectors.toList()));
    }

    public static ObservableList<String> getBookTitles(ObservableReference<ILibrary> libraryReference) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < libraryReference.poll().getBooks().size(); i++) {
            list.add(libraryReference.poll().getBooks().get(i).getTitle());
        }
        return toObservableList(list);
    }

    public static <T> ObservableList<T> toObservableList(Collection<T> list) {
        return FXCollections.observableArrayList(list);
    }

    public static <T> List<T> fromObservableList(ObservableList<T> list) {
        ArrayList<T> arrayList = new ArrayList<>();
        list.stream().forEach(arrayList::add);
        return arrayList;
    }

}
