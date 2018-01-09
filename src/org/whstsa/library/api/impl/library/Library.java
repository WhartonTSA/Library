package org.whstsa.library.api.impl.library;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.*;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.util.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class Library implements ILibrary {

    private List<IBook> books;
    private List<IMember> members;
    private Map<IBook, Integer> bookQuantity;

    private String name;

    private UUID uuid;

    public Library(String name) {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.bookQuantity = new HashMap<>();
    }

    public static ILibrary findLibrary(UUID memberID) {
        return ObjectDelegate.getLibraries()
                .stream()
                .filter(library -> library.getMemberIDs().contains(memberID))
                .collect(Collectors.toList())
                .get(0);
    }

    public void impl_setBookList(List<IBook> bookList) {
        this.books = bookList;
    }

    public void impl_setID(UUID uuid) {
        this.uuid = uuid;
    }

    public void impl_setMembers(List<IMember> members) {
        this.members = members;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        JSONArray books = new JSONArray();
        this.books.forEach(book -> books.put(book.getID()));
        object.put("books", books);

        JSONArray members = new JSONArray();
        this.members.forEach(member -> members.put(member.toJSON()));
        object.put("members", members);

        object.put("uuid", this.uuid.toString());

        object.put("name", this.name);

        JSONObject quantities = new JSONObject();
        this.bookQuantity.forEach((book, quantity) -> quantities.put(book.getID().toString(), quantity));
        object.put("quantities", quantities);

        return object;
    }

    @Override
    public void addBook(IBook book) {
        if (this.books.contains(book)) {
            return;
        }
        this.books.add(book);
    }

    @Override
    public void removeBook(IBook book) {
        for (IMember member : this.members) {
            if (member.hasBook(book)) {
                throw new InCirculationException(this, book);
            }
        }
        this.books.remove(book);
    }

    @Override
    public void addBook(UUID id) {
        IBook book = ObjectDelegate.getBook(id);
        if (book != null) {
            if (!this.books.contains(book)) {
                this.books.add(book);
            }
        }
    }

    @Override
    public void removeBook(UUID id) {
        this.removeBook(this.getBookMap().get(id));
    }

    @Override
    public List<IBook> getBooks() {
        return this.books;
    }

    @Override
    public Map<UUID, IBook> getBookMap() {
        Map<UUID, IBook> bookMap = new HashMap<>();
        this.books.forEach(book -> bookMap.put(book.getID(), book));
        return bookMap;
    }

    @Override
    public List<UUID> getBookIDs() {
        List<UUID> bookIDs = new ArrayList<>();
        this.books.forEach(book -> bookIDs.add(book.getID()));
        return bookIDs;
    }

    @Override
    public UUID getID() {
        return this.uuid;
    }

    @Override
    public ICheckout reserveBook(IMember member, IBook book) throws BookNotRegisteredException, OutOfStockException {
        boolean hasCheckouts = this.getCheckouts().get(book) != null;
        boolean noBooksLeft = hasCheckouts ? this.getCheckouts().get(book).size() == this.bookQuantity.get(book) : false;
        Logger.DEFAULT_LOGGER.debug((this.bookQuantity == null) + "");
        if (this.bookQuantity.get(book) == 0 || noBooksLeft) {
            throw new OutOfStockException(book , this);
        }
        ICheckout checkout = new Checkout(member, book);
        member.checkout(checkout);
        return checkout;
    }

    @Override
    public IMember addMember(IPerson person) {
        IMember possibleMember = this.hasMember(person);
        if (possibleMember != null) {
            return possibleMember;
        }
        IMember member = person.addMembership(this);
        this.members.add(member);
        return member;
    }

    @Override
    public void removeMember(UUID memberID) throws CannotDeregisterException {
        IMember member = this.getMemberMap().get(memberID);
        if (member != null) {
            this.removeMember(member);
            return;
        }
        throw new MemberNotFoundException("Member ID: " + memberID);
    }

    @Override
    public void removeMember(IPerson person) throws CannotDeregisterException {
        for (IMember member : person.getMemberships()) {
            if (member.getLibrary() == this) {
                this.removeMember(member);
                return;
            }
        }
        throw new MemberNotFoundException("Person ID: " + person.getID());
    }

    @Override
    public void removeMember(IMember member) throws CannotDeregisterException {
        if (member.getFine() > 0) {
            throw new OutstandingFinesException(member);
        }
        if (member.getBooks().size() >= 1) {
            throw new MemberHasBooksException(member);
        }
        this.members.remove(member);

    }

    @Override
    public boolean isMember(IPerson person) {
        for (IMember member : person.getMemberships()) {
            if (member.getLibrary() == this) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<IMember> getMembers() {
        return this.members;
    }

    @Override
    public List<UUID> getMemberIDs() {
        List<UUID> ids = new ArrayList<>();
        this.members.forEach(member -> ids.add(member.getID()));
        return ids;
    }

    @Override
    public Map<UUID, IMember> getMemberMap() {
        Map<UUID, IMember> memberMap = new HashMap<>();
        this.members.forEach(member -> memberMap.put(member.getID(), member));
        return memberMap;
    }

    @Override
    public boolean hasBook(UUID id) {
        return this.getBookMap().containsKey(id);
    }

    @Override
    public boolean hasBook(IBook book) {
        return this.hasBook(book.getID());
    }

    @Override
    public String getName() {
        return this.name;
    }

    private IMember hasMember(IPerson person) {
        for (IMember member : this.members) {
            if (member.getPerson() == person) {
                return member;
            }
        }
        return null;
    }

    @Override
    public void load() {
        Loader.getLoader().loadLibrary(this);
    }

    @Override
    public Map<IBook, List<ICheckout>> getCheckouts() {
        Map<IBook, List<ICheckout>> bookListMap = new HashMap<>();
        this.getMembers().forEach(member -> member.getCheckoutMap().forEach((book, checkoutList) -> {
            if (!bookListMap.containsKey(book)) {
                bookListMap.put(book, new ArrayList<>());
            }
            bookListMap.get(book).addAll(checkoutList);
        }));
        return bookListMap;
    }

    public int getQuantity(IBook book) {
        return this.bookQuantity.get(book);
    }

    public void setQuantity(IBook book, int amount) {
        this.bookQuantity.put(book, amount);
    }

}
