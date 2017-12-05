package org.whstsa.library.commands.getters;

import org.json.JSONObject;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class ListBooksCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        boolean full = args.length == 0 ? false : Boolean.valueOf(args[0]);
        JSONObject result = new JSONObject();
        ObjectDelegate.getBooks().forEach(book -> {
            if (full) {
                result.put(book.getID().toString(), book.toJSON());
            } else {
                result.put(book.getID().toString(), book.getTitle());
            }
        });
        return result;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("full");
        return args;
    }

    @Override
    public String getName() {
        return "listbooks";
    }
}
