package org.whstsa.library.api.impl;

import org.json.JSONObject;
import org.whstsa.library.api.BookType;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.db.Loader;

import java.util.UUID;

/**
 * Created by eric on 11/18/17.
 */
public class Book implements IBook {

    private String title;
    private String authorName;
    private BookType type;
    private UUID uuid;

    public Book(String title, String authorName, BookType type) {
        this.title = title;
        this.authorName = authorName;
        this.type = type;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        object.put("title", this.title);
        object.put("authorName", this.authorName);
        object.put("bookType", this.type.name());
        object.put("uuid", this.uuid);

        return object;
    }

    public void impl_setID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getID() {
        return this.uuid;
    }

    @Override
    public BookType getType() {
        return this.type;
    }

    @Override
    public void setType(BookType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return this.title;
    }

    @Override
    public String getAuthorName() {
        return this.authorName;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setAuthor(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public void load() {
        Loader.getLoader().loadBook(this);
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
