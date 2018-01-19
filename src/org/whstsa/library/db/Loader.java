package org.whstsa.library.db;

import org.apache.commons.lang3.EnumUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.Tester;
import org.whstsa.library.api.BookType;
import org.whstsa.library.api.DateUtils;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.LoadingException;
import org.whstsa.library.api.exceptions.OutstandingFinesException;
import org.whstsa.library.api.impl.Book;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.impl.library.Checkout;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.impl.library.Member;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.util.Logger;

import java.util.*;

/**
 * Created by eric on 11/18/17.
 */
public class Loader {

    private static final Loader LOADER = new Loader();
    private Map<UUID, IBook> bookMap = new HashMap<>();
    private Map<UUID, IPerson> personMap = new HashMap<>();
    private Map<UUID, ILibrary> libraryMap = new HashMap<>();

    private Loader() {
    }

    /**
     * Returns the Loader singleton
     *
     * @return the loader instance
     */
    public static Loader getLoader() {
        return LOADER;
    }

    /**
     * Parses a fully serialized JSON object into the data stores
     *
     * @param object the json object to parse
     */
    public void load(JSONObject object) {
        Tester.print("Loading JSON into data stores");
        
        this.validateObject(object, "books");
        this.validateObject(object, "libraries");
        this.validateObject(object, "people");
        
        Object rawBooks = object.get("books");
        Object rawLibraries = object.get("libraries");
        Object rawPeople = object.get("people");

        if (!(rawBooks instanceof JSONArray) || !(rawLibraries instanceof JSONArray) || !(rawPeople instanceof JSONArray)) {
            Tester.print("JSON is malformed. Not loading.");
            return;
        }

        JSONArray booksArray = (JSONArray) rawBooks;
        JSONArray librariesArray = (JSONArray) rawLibraries;
        JSONArray peopleArray = (JSONArray) rawPeople;

        Tester.print("Loading books");
        try {
            this.loadBooks(booksArray);
        } catch (LoadingException ex) {
            ex.printStackTrace();
            return;
        }

        Tester.print("Loading people");
        try {
            this.loadPeople(peopleArray);
        } catch (LoadingException ex) {
            ex.printStackTrace();
            return;
        }

        Tester.print("Loading libraries");
        try {
            this.loadLibraries(librariesArray);
        } catch (LoadingException ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Loads a raw book object into the book store
     *
     * @param rawBook the book to load
     * @return the loaded book
     * @throws LoadingException thrown when an error occurs during loading
     */
    public IBook loadBook(Object rawBook) throws LoadingException {
        if (rawBook instanceof JSONObject) {
            JSONObject bookObject = (JSONObject) rawBook;
            String authorName = bookObject.getString("authorName");
            String title = bookObject.getString("title");
            String rawUUID = String.valueOf(bookObject.get("uuid"));
            String rawBookType = bookObject.getString("bookType");

            UUID uuid;
            try {
                uuid = UUID.fromString(rawUUID);
            } catch (IllegalArgumentException ex) {
                throw new LoadingException(ex);
            }

            BookType type = EnumUtils.getEnum(BookType.class, rawBookType);
            if (type == null) {
                type = BookType.GENERIC;
            }

            Book book = new Book(title, authorName, type);
            book.impl_setID(uuid);

            Tester.print("Loaded book object");
            Tester.print(book);

            this.bookMap.put(book.getID(), book);
            return book;
        }
        throw new LoadingException("Raw book was not of JSONObject type");
    }

    /**
     * Removes the book data from the database.
     *
     * Make sure that you have cleared all references to the book ID
     * or the program will error out on next run.
     *
     * @param uuid the book uuid
     */
    public void unloadBook(UUID uuid) {
        this.bookMap.remove(uuid);
    }

    /**
     * Removes the person data from the database.
     *
     * Make sure that you have cleared all references to the person ID
     * or the program will error out on next run.
     *
     * @param uuid the person uuid
     */
    public void unloadPerson(UUID uuid) {
        this.personMap.remove(uuid);
    }

    /**
     * Removes the library from the library database.
     *
     * @param uuid the library uuid
     */
    public void unloadLibrary(UUID uuid) {
        this.libraryMap.remove(uuid);
    }

    /**
     * Loads a series of books into the book store
     *
     * @param books the books to load
     * @return the loaded books
     * @throws LoadingException thrown when an error occurs during loading
     */
    public List<IBook> loadBooks(JSONArray books) throws LoadingException {
        List<IBook> loadedBooks = new ArrayList<>();
        final JSONObject stats = new JSONObject();
        stats.put("halted", false);
        books.forEach(rawBook -> {
            if (stats.getBoolean("halted")) {
                return;
            }
            try {
                IBook book = this.loadBook(rawBook);
                loadedBooks.add(book);
            } catch (LoadingException ex) {
                stats.put("halted", true);
                stats.put("cause", ex);
            }
        });
        if (stats.getBoolean("halted")) {
            throw (LoadingException) stats.get("cause");
        }
        return loadedBooks;
    }

    /**
     * Load a raw person object into the people store
     *
     * @param rawPerson the person to load
     * @return the loaded person
     * @throws LoadingException thrown when an error occurs during loading
     */
    public IPerson loadPerson(Object rawPerson) throws LoadingException {
        if (rawPerson instanceof JSONObject) {
            JSONObject personObject = (JSONObject) rawPerson;
            String firstName = personObject.getString("firstName");
            String lastName = personObject.getString("lastName");
            boolean teacher = personObject.getBoolean("teacher");
            double wallet = personObject.getDouble("wallet");
            String rawUUID = String.valueOf(personObject.get("uuid"));

            UUID uuid;
            try {
                uuid = UUID.fromString(rawUUID);
            } catch (IllegalArgumentException ex) {
                throw new LoadingException(ex);
            }

            Person person = new Person(firstName, lastName, teacher);
            person.addMoney(wallet);
            person.impl_setID(uuid);

            Tester.print("Loaded person object");
            Tester.print(person);

            this.personMap.put(person.getID(), person);
            return person;
        }
        throw new LoadingException("Raw person was not of JSONObject type");
    }

    /**
     * Load a series of people into the people store
     *
     * @param people the people to load
     * @return the loaded people
     * @throws LoadingException thrown when an error occurs during loading
     */
    public List<IPerson> loadPeople(JSONArray people) throws LoadingException {
        List<IPerson> loadedPeople = new ArrayList<>();
        final JSONObject stats = new JSONObject();
        stats.put("halted", false);
        people.forEach(rawPerson -> {
            if (stats.getBoolean("halted")) {
                return;
            }
            try {
                IPerson person = this.loadPerson(rawPerson);
                loadedPeople.add(person);
            } catch (LoadingException ex) {
                stats.put("halted", true);
                stats.put("cause", ex);
            }
        });
        if (stats.getBoolean("halted")) {
            throw (LoadingException) stats.get("cause");
        }
        return loadedPeople;
    }

    /**
     * Load a raw library object into the library store
     *
     * @param rawLibrary the library to load
     * @return the loaded library
     * @throws LoadingException thrown when an error occurs during loading
     */
    public ILibrary loadLibrary(Object rawLibrary) throws LoadingException {
        if (rawLibrary instanceof JSONObject) {
            JSONObject libraryObject = (JSONObject) rawLibrary;

            List<IBook> bookList = new ArrayList<>();
            List<IMember> memberList = new ArrayList<>();
            String libraryName = libraryObject.getString("name");
            String rawUUID = String.valueOf(libraryObject.get("uuid"));

            UUID uuid;
            try {
                uuid = UUID.fromString(rawUUID);
            } catch (IllegalArgumentException ex) {
                throw new LoadingException(ex);
            }

            JSONArray bookIDRawList = libraryObject.getJSONArray("books");
            List<UUID> bookIDList = new ArrayList<>();
            bookIDRawList.forEach(id -> {
                try {
                    bookIDList.add(UUID.fromString(String.valueOf(id)));
                } catch (IllegalArgumentException ex) {
                }
            });
            bookIDList.forEach(bookID -> bookList.add(this.bookMap.get(bookID)));

            Library library = new Library(libraryName);
            library.impl_setBookList(bookList);
            library.impl_setID(uuid);

            this.libraryMap.put(library.getID(), library);

            libraryObject.getJSONArray("members").forEach(memberObject -> {
                try {
                    IMember member = this.loadMember(memberObject, library);
                    memberList.add(member);
                } catch (LoadingException ex) {
                    ex.printStackTrace();
                }
            });

            library.impl_setMembers(memberList);

            libraryObject.getJSONObject("quantities").keySet().forEach(id -> {
                UUID bookID = UUID.fromString(id);
                IBook book = this.bookMap.get(bookID);
                if (book == null) {
                    return;
                }
                library.setQuantity(book.getID() , libraryObject.getInt(id));
            });

            Tester.print("Loaded library object");
            Tester.print(library);

            return library;
        }
        throw new LoadingException("Raw library was not of JSONObject type");
    }

    /**
     * Load a series of library objects into the library store
     *
     * @param libraries the libraries to load
     * @return the loaded libraries
     * @throws LoadingException thrown when an error occurs during loading
     */
    public List<ILibrary> loadLibraries(JSONArray libraries) throws LoadingException {
        List<ILibrary> loadedLibraries = new ArrayList<>();
        final JSONObject stats = new JSONObject();
        stats.put("halted", false);
        libraries.forEach(rawLibrary -> {
            if (stats.getBoolean("halted")) {
                return;
            }
            try {
                ILibrary library = this.loadLibrary(rawLibrary);
                loadedLibraries.add(library);
            } catch (LoadingException ex) {
                stats.put("halted", true);
                stats.put("cause", ex);
            }
        });
        if (stats.getBoolean("halted")) {
            throw (LoadingException) stats.get("cause");
        }
        return loadedLibraries;
    }

    /**
     * Load a raw member object into the member store
     *
     * @param rawMember the member to load
     * @param library   the library they belong to
     * @return the loaded member
     * @throws LoadingException thrown when an error occurs during loading
     */
    private IMember loadMember(Object rawMember, ILibrary library) throws LoadingException {
        if (rawMember instanceof JSONObject) {
            JSONObject memberObject = (JSONObject) rawMember;

            String rawPersonUUID = String.valueOf(memberObject.get("personID"));
            String rawUUID = String.valueOf(memberObject.get("uuid"));
            Map<IBook, List<ICheckout>> memberBooks = new HashMap<>();

            UUID personUUID;
            UUID uuid;
            try {
                personUUID = UUID.fromString(rawPersonUUID);
                uuid = UUID.fromString(rawUUID);
            } catch (IllegalArgumentException ex) {
                throw new LoadingException(ex);
            }

            IPerson person = this.personMap.get(personUUID);

            Member member = new Member(person, library);
            member.impl_setID(uuid);
            
            Map<IBook, List<ICheckout>> bookList = new HashMap<>();

            JSONObject checkoutsObjectMap = memberObject.getJSONObject("checkouts");
            checkoutsObjectMap.keySet().forEach(key -> {
                JSONArray checkoutJSONArray = checkoutsObjectMap.getJSONArray(key);
                checkoutJSONArray.forEach(rawCheckout -> {
                    try {
                        ICheckout checkout = this.loadCheckout(rawCheckout, member);
                        if (!bookList.containsKey(checkout.getBook())) {
                        	bookList.put(checkout.getBook(), new ArrayList<>());
                        }
                        bookList.get(checkout.getBook()).add(checkout);
                    } catch (LoadingException ex) {
                        ex.printStackTrace();
                    }
                });
            });
            
            member.impl_setBooks(bookList);

            Tester.print("Loaded member object");
            Tester.print(member);

            return member;
        }
        throw new LoadingException("Raw member was not of JSONObject type");
    }

    /**
     * Loads a checkout object into a loaded member object
     *
     * @param rawCheckout the raw checkout object
     * @param member      the loaded member object
     * @return the loaded checkout
     * @throws LoadingException thrown when an error occurs during loading
     */
    private ICheckout loadCheckout(Object rawCheckout, IMember member) throws LoadingException {
        if (rawCheckout instanceof JSONObject) {
            JSONObject checkoutObject = (JSONObject) rawCheckout;

            String rawDueDate = checkoutObject.getString("dueDate");
            boolean isReturned = checkoutObject.getBoolean("returned");
            String rawUUID = String.valueOf(checkoutObject.get("uuid"));
            String rawBookUUID = String.valueOf(checkoutObject.get("bookID"));

            UUID checkoutUUID;
            UUID bookUUID;
            Date dueDate;
            try {
                checkoutUUID = UUID.fromString(rawUUID);
                bookUUID = UUID.fromString(rawBookUUID);
                dueDate = DateUtils.fromDateString(rawDueDate);
            } catch (IllegalArgumentException ex) {
                throw new LoadingException(ex);
            }

            IBook book = this.bookMap.get(bookUUID);
            if (book == null) {
                throw new LoadingException("Checkout (" + checkoutUUID + ") refers to a book ID (" + bookUUID + ") that has not been loaded yet.");
            }

            Checkout checkout = new Checkout(member, book);
            checkout.impl_setDueDate(dueDate);
            checkout.impl_setID(checkoutUUID);
            checkout.impl_setReturned(isReturned);

            Tester.print("Loaded checkout object");
            Tester.print(checkout);

            return checkout;
        }
        throw new LoadingException("Raw checkout was not of JSONObject type");
    }

    /**
     * Returns the map for UUID-books.
     *
     * @return the book map
     */
    protected Map<UUID, IBook> getBookMap() {
        return this.bookMap;
    }

    /**
     * Returns the map for UUID-people.
     *
     * @return the people map
     */
    protected Map<UUID, IPerson> getPersonMap() {
        return this.personMap;
    }

    /**
     * Returns the map for UUID-library.
     *
     * @return the library map.
     */
    protected Map<UUID, ILibrary> getLibraryMap() {
        return this.libraryMap;
    }

    /**
     * Loads a newly constructed book into the book store
     *
     * @param book the book to load
     */
    public void loadBook(IBook book) {
        this.bookMap.put(book.getID(), book);
    }

    /**
     * Load a newly constructed person into the book store
     *
     * @param person the person to load
     */
    public void loadPerson(IPerson person) {
        this.personMap.put(person.getID(), person);
    }

    /**
     * Load a newly constructed library into the library store
     *
     * @param library the library to load
     */
    public void loadLibrary(ILibrary library) {
        this.libraryMap.put(library.getID(), library);
    }

    /**
     * Load a series of newly constructed books into the book store
     *
     * @param books the books to load
     */
    public void loadAllBooks(List<IBook> books) {
        books.forEach(book -> this.bookMap.put(book.getID(), book));
    }

    /**
     * Load a series of newly constructed people into the people store
     *
     * @param people the people to load
     */
    public void loadAllPeople(List<IPerson> people) {
        people.forEach(person -> this.personMap.put(person.getID(), person));
    }

    /**
     * Load a series of newly constructed libraries into the library store
     *
     * @param libraries the libraries to load
     */
    public void loadAllLibraries(List<ILibrary> libraries) {
        libraries.forEach(library -> this.libraryMap.put(library.getID(), library));
    }

    /**
     * Computes a JSONObject containing serialized objects for all obejects
     * in the loader stores
     *
     * @return the computed JSONObject
     */
    public JSONObject computeJSON() {
        JSONObject object = new JSONObject();

        JSONArray libraries = new JSONArray();
        for (ILibrary library : this.libraryMap.values()) {
            libraries.put(library.toJSON());
        }
        object.put("libraries", libraries);

        JSONArray people = new JSONArray();
        for (IPerson person : this.personMap.values()) {
            people.put(person.toJSON());
        }
        object.put("people", people);

        JSONArray books = new JSONArray();
        for (IBook book : this.bookMap.values()) {
            books.put(book.toJSON());
        }
        object.put("books", books);

        return object;
    }
    
    private void validateObject(JSONObject object, String key) {
    	if (!object.has(key) || !(object.get(key) instanceof JSONArray)) {
        	object.put(key, new JSONArray());
        }
    }

}
