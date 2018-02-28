package org.whstsa.library.api.impl.library;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.BookNotRegisteredException;
import org.whstsa.library.api.exceptions.CheckedInException;
import org.whstsa.library.api.exceptions.MemberMismatchException;
import org.whstsa.library.api.exceptions.OutstandingFinesException;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.util.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class Member implements IMember {

    private static final String MEMBER_MISMATCH_CHECKIN = "Cannot check-in a checkout for member %s under member %s";
    private static final String MEMBER_MISMATCH_CHECKOUT = "Checkout belongs to member %s but was attempted to be added to member %s";
    private Map<IBook, List<ICheckout>> books;
    private IPerson person;
    private ILibrary library;
    private UUID uuid;

    public Member(IPerson person, ILibrary library) {
        this.person = person;
        this.books = new HashMap<>();
        this.uuid = UUID.randomUUID();
        this.library = library;
    }

    public void impl_setID(UUID uuid) {
        this.uuid = uuid;
    }

    public void impl_setBooks(Map<IBook, List<ICheckout>> books) {
        this.books = books;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        JSONObject checkoutMap = new JSONObject();
        this.books.forEach((book, checkoutList) -> {
            JSONArray checkoutArray = new JSONArray();
            checkoutList.forEach(checkout -> {
                if (checkout.isReturned()) {
                    return;
                }
                checkoutArray.put(checkout.toJSON());
            });
            if (checkoutArray.length() == 0) {
                return;
            }
            checkoutMap.put(book.getID().toString(), checkoutArray);
        });
        object.put("checkouts", checkoutMap);
        object.put("personID", this.person.getID());
        object.put("uuid", this.uuid);

        return object;
    }

    @Override
    public double getFine() {
        double fine = 0;
        for (ICheckout checkout : this.getCheckouts()) {
            if (checkout.isReturned()) {
                continue;
            }
            fine += checkout.getFine();
        }
        return fine;
    }

    @Override
    public UUID getID() {
        return this.uuid;
    }

    @Override
    public void addBook(IBook book, int quantity) {
        ICheckout checkout = new Checkout(this, book);
        this.checkout(checkout);
    }

    @Override
    public void returnCheckout(ICheckout checkout) throws OutstandingFinesException {
        Logger.DEFAULT_LOGGER.debug("Started checkin process");
        List<ICheckout> checkoutList = this.books.get(checkout.getBook());
        if (checkoutList == null || checkoutList.size() == 0) {
            Logger.DEFAULT_LOGGER.warn("Ignoring return checkout for unknown book");
            return;
        }
        if (checkoutList.contains(checkout)) {
            if (checkout.getFine() != 0) {
                Logger.DEFAULT_LOGGER.warn("Terminating checkin process due to unresolved fines");
                throw new OutstandingFinesException(this, OutstandingFinesException.Actions.REMOVE_BOOK, checkout.getFine());
            }
            try {
                checkout.checkIn();
            } catch (CheckedInException e) {
                Logger.DEFAULT_LOGGER.warn("Checkout was already checked in - continuing");
                // The error is swallowed at the moment
            }
            Logger.DEFAULT_LOGGER.log(String.format("Checking in %s for person %s", checkout.getBook().getName(), this.getName()));
            checkoutList.remove(checkout);
        }
    }

    @Override //TODO ASK ERIC ABOUT INTENTION OF REMOVING ALL CHECKOUTS/BOOK
    public void removeBook(IBook book) throws OutstandingFinesException {
        if (this.books.containsKey(book)) {
            for (ICheckout checkout : this.books.get(book)) {
                if (checkout.getFine() != 0) {
                    throw new OutstandingFinesException(this, OutstandingFinesException.Actions.REMOVE_BOOK, checkout.getFine());
                }
                try {
                    checkout.checkIn();
                } catch (CheckedInException e) {
                    // The error is swallowed at the moment
                }
            }
            this.books.remove(book);
        }
    }

    @Override
    public void checkIn(ICheckout checkout) throws OutstandingFinesException, MemberMismatchException, CheckedInException {
        if (checkout.getOwner() != this) {
            throw new MemberMismatchException(String.format(MEMBER_MISMATCH_CHECKIN, checkout.getOwner().getID(), this.getID()));
        }
        if (checkout.getFine() != 0) {
            throw new OutstandingFinesException(this, OutstandingFinesException.Actions.CHECK_IN, checkout.getFine());
        }
        checkout.checkIn();
    }

    @Override
    public void checkInAndPayFines(ICheckout checkout) throws MemberMismatchException, CheckedInException {
        if (checkout.getOwner() != this) {
            throw new MemberMismatchException(String.format(MEMBER_MISMATCH_CHECKIN, checkout.getOwner().getID(), this.getID()));
        }
        if (checkout.getFine() != 0) {
            checkout.payFine();
        }
        checkout.checkIn();
    }

    @Override
    public void addBook(UUID id) {
        IBook book = this.getLibrary().getBookMap().get(id);
        if (book != null) {
            this.addBook(book.getID());
        }
    }

    @Override
    public void removeBook(UUID id) throws OutstandingFinesException {
        IBook book = this.getLibrary().getBookMap().get(id);
        if (book != null) {
            this.removeBook(book);
        }
    }

    @Override
    public List<IBook> getBooks() {
        List<IBook> books = new ArrayList<>();
        books.addAll(this.books.keySet());
        return books;
    }

    @Override
    public Map<UUID, IBook> getBookMap() {
        Map<UUID, IBook> bookMap = new HashMap<>();
        this.books.keySet().forEach(book -> bookMap.put(book.getID(), book));
        return bookMap;
    }

    @Override
    public List<UUID> getBookIDs() {
        List<UUID> uuidList = new LinkedList<>();
        this.books.keySet().forEach(book -> uuidList.add(book.getID()));
        return uuidList;
    }

    @Override
    public boolean hasBook(UUID id) {
        return this.hasBook(ObjectDelegate.getBook(id));
    }

    @Override
    public boolean hasBook(IBook book) {
        return book != null && this.books.containsKey(book);
    }

    @Override
    public ILibrary getLibrary() {
        return this.library;
    }

    @Override
    public IPerson getPerson() {
        return this.person;
    }

    @Override
    public List<ICheckout> getCheckout(IBook book) {
        List<ICheckout> checkouts = this.books.get(book);
        if (checkouts == null) {
            return new ArrayList<>();
        }
        return checkouts;
    }

    @Override
    public void checkout(ICheckout checkout) throws BookNotRegisteredException, MemberMismatchException {
        if (checkout.getOwner() != this) {
            throw new MemberMismatchException(String.format(MEMBER_MISMATCH_CHECKOUT, checkout.getOwner().getID(), this.getID()));
        }
        if (!this.getLibrary().hasBook(checkout.getBook())) {
            throw new BookNotRegisteredException(this.getLibrary(), checkout.getBook());
        }
        if (!this.books.containsKey(checkout.getBook())) {
            this.books.put(checkout.getBook(), new ArrayList<>());
        }
        List<ICheckout> checkouts = this.books.get(checkout.getBook());
        if (!checkouts.contains(checkout)) {
            checkouts.add(checkout);
        }
    }

    @Override
    public List<ICheckout> getCheckouts() {
        return this.getCheckouts(false);
    }

    @Override
    public List<ICheckout> getCheckouts(boolean notReturned) {
        List<ICheckout> checkouts = new ArrayList<>();
        this.books.values().forEach(checkouts::addAll);
        if (notReturned) {
            checkouts = checkouts.stream().filter(checkout -> !checkout.isReturned()).collect(Collectors.toList());
        }
        return checkouts;
    }

    @Override
    public Map<IBook, List<ICheckout>> getCheckoutMap() {
        return this.books;
    }

    @Override
    public String getName() {
        return this.getPerson().getName();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}