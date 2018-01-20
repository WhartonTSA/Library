package org.whstsa.library.util;

public enum BookStatus {
    AVAILABLE ("Available"),
    CHECKED_OUT ("Checked Out"),
    RESERVED ("Reserved");

    public final String status;

    BookStatus(String status) {
        this.status = status;
    }

    public String getString() {
        return this.status;
    }

    public static BookStatus getStatus(String status) {
        return BookStatus.valueOf(adaptEnumName(status));
    }

    private static String adaptEnumName(String niceName) {
        return niceName.toUpperCase().replace(' ', '_');
    }
}
