package org.whstsa.library.gui.components.tables;

import org.whstsa.library.util.BookStatus;

public class BookStatusRow {

    private int copy;
    private BookStatus status;
    private String ownerName;

    BookStatusRow(int copy, BookStatus status, String ownerName) {
        this.copy = copy;
        this.status = status;
        this.ownerName = ownerName;
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


}
