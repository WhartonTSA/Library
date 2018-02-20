package org.whstsa.library.api.impl;

import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.MemberMismatchException;
import org.whstsa.library.api.impl.library.Member;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.util.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by eric on 11/18/17.
 */
public class Person implements IPerson {

    private String firstName;
    private String lastName;

    private boolean teacher;

    private UUID uuid;

    public Person(String firstName, String lastName, boolean teacher) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.teacher = teacher;
        this.uuid = UUID.randomUUID();
    }

    public Person(String firstName, String lastName) {
        this(firstName, lastName, false);
    }

    public void impl_setID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        object.put("firstName", this.firstName);
        object.put("lastName", this.lastName);
        object.put("teacher", this.teacher);
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
        return this.firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean isTeacher() {
        return this.teacher;
    }

    @Override
    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    @Override
    public List<IMember> getMemberships() {
        return ObjectDelegate.getLibraries().stream()
                .filter(library -> library.hasMember(this))
                .map(library -> library.getMember(this))
                .collect(Collectors.toList());
    }

    @Deprecated
    @Override
    public IMember addMembership(IMember member) throws MemberMismatchException {
        Logger.DEFAULT_LOGGER.warn("[DEPRECATED] Don't use premade members to add memberships from the Person class.");
        if (member.getPerson() != this) {
            throw new MemberMismatchException("Member failed to addElement to person " + this.getID() + " because they are assigned person " + member.getPerson().getID());
        }
        return member.getLibrary().addMember(member);
    }

    @Override
    public IMember addMembership(ILibrary library) {
        try {
            return new Member(this, library);
        } catch (MemberMismatchException ex) {
            // This will never be thrown, but this satisfies compiler errors
            return null;
        }
    }

    @Override
    public void load() {
        Loader.getLoader().loadPerson(this);
    }

    @Override
    public boolean isRemovable() {
        return this.getBooks().size() == 0;
    }
}
