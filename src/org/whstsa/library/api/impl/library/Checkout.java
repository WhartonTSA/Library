package org.whstsa.library.api.impl.library;

import org.json.JSONObject;
import org.whstsa.library.World;
import org.whstsa.library.api.DateUtils;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.CheckedInException;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.IMember;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Checkout implements ICheckout {

    public static final double LATE_FEE = 0.5;
    public static final double LATE_FEE_TEACHER = 0.25;
    private static final int DUE_DATE_DAYS_LATER = 7;
    private IMember member;
    private IBook book;
    private Date dueDate;
    private boolean returned;
    private UUID uuid;

    public Checkout(IMember member, IBook book) {
        this.member = member;
        this.book = book;
        this.uuid = UUID.randomUUID();
        this.returned = false;
        this.resetDueDate();
    }

    public void impl_setID(UUID uuid) {
        this.uuid = uuid;
    }

    public void impl_setReturned(boolean returned) {
        this.returned = returned;
    }

    public void impl_setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("uuid", this.uuid);
        object.put("dueDate", DateUtils.toDateString(this.dueDate));
        object.put("returned", this.returned);
        object.put("bookID", this.book.getID());
        return object;
    }

    @Override
    public UUID getID() {
        return this.uuid;
    }

    @Override
    public IMember getOwner() {
        return this.member;
    }

    @Override
    public IBook getBook() {
        return this.book;
    }

    @Override
    public Date getDueDate() {
        return this.dueDate;
    }

    @Override
    public void resetDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(World.getDate());
        cal.add(Calendar.DAY_OF_MONTH, DUE_DATE_DAYS_LATER);
        this.dueDate = cal.getTime();
    }

    @Override
    public double getFine() {
        if (this.isReturned()) {
            return 0;
        }
        return this.getDaysPast() * this.getMultiplier();
    }

    @Override
    public void payFine() {
        this.resetDueDate();
    }

    @Override
    public int getDaysPast() {
        Date now = World.getDate();
        if (now.after(this.dueDate)) {
            Calendar nowCal = Calendar.getInstance();
            Calendar dueCal = Calendar.getInstance();
            nowCal.setTime(now);
            dueCal.setTime(this.dueDate);
            long millisDiff = nowCal.getTimeInMillis() - dueCal.getTimeInMillis();
            return (int) Math.ceil((float) millisDiff / (24 * 60 * 60 * 1000));
        }
        return 0;
    }

    @Override
    public boolean isReturned() {
        return this.returned;
    }

    @Override
    public boolean isOverdue() {
        return this.getDaysPast() != 0;
    }

    @Override
    public void checkIn() throws CheckedInException {
        if (this.returned) {
            throw new CheckedInException(this);
        }
        this.returned = true;
    }

    private double getMultiplier() {
        return this.getOwner().getPerson().isTeacher() ? LATE_FEE_TEACHER : LATE_FEE;
    }

}
