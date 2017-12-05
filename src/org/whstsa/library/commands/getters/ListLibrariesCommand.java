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
public class ListLibrariesCommand implements ICommand {

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        JSONObject response = new JSONObject();
        boolean full = args.length > 0 ? Boolean.valueOf(args[0]) : false;
        ObjectDelegate.getLibraries().forEach(library -> {
            if (full) {
                response.put(library.getID().toString(), library.toJSON());
            } else {
                response.put(library.getID().toString(), library.getName());
            }
        });
        return response;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("full");
        return args;
    }

    @Override
    public String getName() {
        return "listlibraries";
    }

}
