package org.whstsa.library.gui.components.tables;

import org.whstsa.library.api.books.IBook;

import java.util.Date;

public class MemberBookRow {

    private IBook book;
    private String title;
    private Date dueDate;

    public MemberBookRow(IBook book, Date dueDate) {
        this.book = book;
        this.title = this.book.getName();
        this.dueDate = dueDate;
    }

    public IBook getBook() {
        return this.book;
    }

    public String getTitle() {
        return this.title;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

}
