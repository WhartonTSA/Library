package org.whstsa.library.commands.setters;

import org.json.JSONObject;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.MaximumCheckoutsException;
import org.whstsa.library.api.exceptions.OutOfStockException;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.commands.CommandUtil;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 11/19/17.
 */
public class CheckoutBookCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length < 2) {
            return ICommand.showSyntax();
        }
        JSONObject result = new JSONObject();
        UUID memberID;
        UUID bookID;
        try {
            memberID = UUID.fromString(args[0]);
            bookID = UUID.fromString(args[1]);
        } catch (IllegalArgumentException ex) {
            return CommandUtil.createErrorResponse("The IDs provided are invalid.");
        }
        IMember member = Library.findLibrary(memberID).getMemberMap().get(memberID);
        if (member == null) {
            return CommandUtil.createErrorResponse("No member with the given ID could be found.");
        }
        IBook book = ObjectDelegate.getBook(bookID);
        if (book == null) {
            return CommandUtil.createErrorResponse("No book with the given ID could be found.");
        }
        try {
            result = member.getLibrary().reserveBook(member, book, 5).toJSON();
        } catch (OutOfStockException | MaximumCheckoutsException ex) {
            return CommandUtil.createErrorResponse(ex.getMessage());
        }
        return result;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("member id");
        args.add("book id");
        return args;
    }

    @Override
    public String getName() {
        return "checkout";
    }
}
