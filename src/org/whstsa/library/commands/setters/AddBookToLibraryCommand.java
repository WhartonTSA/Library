package org.whstsa.library.commands.setters;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.commands.CommandUtil;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.*;

/**
 * Created by eric on 11/19/17.
 */
public class AddBookToLibraryCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length < 2) {
            return ICommand.showSyntax();
        }
        JSONObject result = new JSONObject();
        JSONArray books = new JSONArray();
        UUID libraryID;
        List<UUID> bookIDs = new ArrayList<>();
        try {
            libraryID = UUID.fromString(args[0]);
        } catch (IllegalArgumentException ex) {
            return CommandUtil.createErrorResponse("The provided library UUID is not a valid UUID.");
        }
        ILibrary library = ObjectDelegate.getLibrary(libraryID);
        if (library == null) {
            return CommandUtil.createErrorResponse("The provided library UUID does not exist.");
        }
        List<String> rawBookIDs = new LinkedList<>(Arrays.asList(args));
        rawBookIDs.remove(0);
        for (String rawBookID : rawBookIDs) {
            try {
                bookIDs.add(UUID.fromString(rawBookID));
            } catch (IllegalArgumentException ex) {
                commandSender.sendMessage("Notice: Skipping UUID " + rawBookID + " because it is not a valid UUID.");
            }
        }
        bookIDs.forEach(book -> {
            library.addBook(book);
            books.put(book);
        });
        result.put("books", books);
        return result;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("library id");
        args.add("...book id");
        return args;
    }

    @Override
    public String getName() {
        return "addbooktolibrary";
    }
}
