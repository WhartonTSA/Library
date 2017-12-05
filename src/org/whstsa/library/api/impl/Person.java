package org.whstsa.library.api.impl;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.MemberMismatchException;
import org.whstsa.library.api.exceptions.NotEnoughMoneyException;
import org.whstsa.library.api.impl.library.Member;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;

import java.util.*;

/**
 * Created by eric on 11/18/17.
 */
public class Person implements IPerson {

    private StringProperty firstName;
    private StringProperty lastName;
    private boolean teacher;
    private Map<ILibrary, IMember> memberships;
    private double wallet = 0;

    private UUID uuid;

    public Person(String firstName, String lastName, boolean teacher) {
        this.firstName = new SimpleStringProperty(this, "firstName");
        this.lastName = new SimpleStringProperty(this, "lastName");
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.teacher = teacher;
        this.memberships = new HashMap<>();
        this.uuid = UUID.randomUUID();
    }

    public Person(String firstName, String lastName) {
        this(firstName, lastName, false);
    }

    public void impl_setID(UUID uuid) {
        this.uuid = uuid;
    }

    public void impl_setMoney(double money) {
        this.wallet = money;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        object.put("firstName", this.firstName);
        object.put("lastName", this.lastName);
        object.put("teacher", this.teacher);
        object.put("wallet", this.wallet);
        object.put("uuid", this.uuid);

        return object;
    }

    @Override
    public UUID getID() {
        return this.uuid;
    }

    @Override
    public List<IBook> getBooks() {
        List<IBook> books = new ArrayList<>();
        for (IMember member : this.getMemberships()) {
            books.addAll(member.getBooks());
        }
        return books;
    }

    @Override
    public Map<UUID, IBook> getBookMap() {
        Map<UUID, IBook> bookMap = new HashMap<>();
        this.getBooks().forEach(book -> bookMap.put(book.getID(), book));
        return bookMap;
    }

    @Override
    public List<UUID> getBookIDs() {
        List<UUID> uuidList = new ArrayList<>();
        this.getBooks().forEach(book -> uuidList.add(book.getID()));
        return uuidList;
    }

    @Override
    public boolean hasBook(UUID id) {
        return this.getBookIDs().contains(id);
    }

    @Override
    public boolean hasBook(IBook book) {
        return this.getBooks().contains(book);
    }

    @Override
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String getFirstName() {
        return this.firstName.get();
    }

    @Override
    public String getLastName() {
        return this.lastName.get();
    }

    @Override
    public boolean isTeacher() {
        return this.teacher;
    }

    @Override
    public List<IMember> getMemberships() {
        return new ArrayList<>(this.memberships.values());
    }

    @Override
    public IMember addMembership(IMember member) throws MemberMismatchException {
        if (member.getPerson() != this) {
            throw new MemberMismatchException("Member failed to add to person " + this.getID() + " because they are assigned person " + member.getPerson().getID());
        }
        this.memberships.put(member.getLibrary(), member);
        return member;
    }

    @Override
    public IMember addMembership(ILibrary library) {
        try {
            return this.addMembership(new Member(this, library));
        } catch (MemberMismatchException ex) {
            // This will never be thrown, but this satisfies compiler errors
            return null;
        }
    }

    @Override
    public double getWallet() {
        return this.wallet;
    }

    @Override
    public double addMoney(double money) {
        if (money < 0) {
            return this.wallet;
        }
        this.wallet += money;
        return this.wallet;
    }

    @Override
    public double deductMoney(double money) throws NotEnoughMoneyException {
        if (money > this.wallet) {
            throw new NotEnoughMoneyException(this, money);
        }
        this.wallet -= money;
        return this.wallet;
    }

    @Override
    public void load() {
        Loader.getLoader().loadPerson(this);
    }

    @Override
    public StringProperty firstNameProperty() {
        return this.firstName;
    }

    @Override
    public StringProperty lastNameProperty() {
        return this.lastName;
    }
}
