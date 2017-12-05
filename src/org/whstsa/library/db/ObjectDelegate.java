package org.whstsa.library.db;

import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;

import java.util.*;

/**
 * Created by eric on 11/19/17.
 */
public class ObjectDelegate {

    private ObjectDelegate() {
    }

    public static IBook getBook(UUID uuid) {
        return getBookMap().get(uuid);
    }

    public static IPerson getPerson(UUID uuid) {
        return getPersonMap().get(uuid);
    }

    public static ILibrary getLibrary(UUID uuid) {
        return getLibraryMap().get(uuid);
    }

    public static Map<UUID, IBook> getBookMap() {
        return getLoader().getBookMap();
    }

    public static List<IBook> getBooks() {
        return new ArrayList<>(getBookMap().values());
    }

    public static List<UUID> getBookIDs() {
        return new ArrayList<>(getBookMap().keySet());
    }

    public static Map<UUID, IPerson> getPersonMap() {
        return getLoader().getPersonMap();
    }

    public static List<IPerson> getPeople() {
        return new ArrayList<>(getPersonMap().values());
    }

    public static List<UUID> getPeopleIDs() {
        return new ArrayList<>(getPersonMap().keySet());
    }

    public static Map<UUID, ILibrary> getLibraryMap() {
        return getLoader().getLibraryMap();
    }

    public static List<ILibrary> getLibraries() {
        return new ArrayList<>(getLibraryMap().values());
    }

    public static List<UUID> getLibraryIDs() {
        return new ArrayList<>(getLibraryMap().keySet());
    }

    public static List<IMember> getAllMembers() {
        List<IMember> members = new ArrayList<>();
        getLibraries().stream().map(library -> library.getMembers()).forEach(members::addAll);
        return members;
    }

    public static Map<UUID, Serializable> getCombinedMap() {
        Map<UUID, IBook> bookMap = getBookMap();
        Map<UUID, IPerson> personMap = getPersonMap();
        Map<UUID, ILibrary> libraryMap = getLibraryMap();

        Map<UUID, Serializable> serializableMap = new HashMap<>();
        bookMap.forEach(serializableMap::put);
        personMap.forEach(serializableMap::put);
        libraryMap.forEach(serializableMap::put);

        return serializableMap;
    }

    public static List<Serializable> getCombined() {
        return new ArrayList<>(getCombinedMap().values());
    }

    public static List<UUID> getCombinedIDs() {
        return new ArrayList<>(getCombinedMap().keySet());
    }

    private static Loader getLoader() {
        return Loader.getLoader();
    }

}
