package org.whstsa.library;

import java.util.Date;

public class World {

    private static Date currentDate = null;

    public static Date getDate() {
        return currentDate == null ? new Date() : currentDate;
    }

    public static void setDate(Date date) {
        currentDate = date;
    }

    public static void setToDefaultDate() { currentDate = new Date(); }

}
