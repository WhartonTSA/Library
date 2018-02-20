package org.whstsa.library.gui.components.tables;

import org.whstsa.library.util.BookStatus;

import java.util.Date;

public class BookStatusRow {

    private int copy;
    private BookStatus status;
    private String ownerName;
    private Date dueDate;

    public BookStatusRow(int copy, BookStatus status, String ownerName, Date dueDate) {
        this.copy = copy;
        this.status = status;
        this.ownerName = ownerName;
        this.dueDate = dueDate;
    }

    public int getCopy() {
        return this.copy;
    }

    public BookStatus getStatus() {
        return this.status;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public Date getDueDate() {
        return this.dueDate;
    }


}
