package org.whstsa.library.commands.getters;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 11/19/17.
 */
public class GetLibraryCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        JSONObject result = new JSONObject();
        JSONArray libraries = new JSONArray();
        for (String arg : args) {
            UUID uuid;
            try {
                uuid = UUID.fromString(arg);
            } catch (IllegalArgumentException ex) {
                commandSender.sendMessage("Notice: Skipping " + arg + " as it is an invalid UUID.");
                continue;
            }
            ILibrary library = ObjectDelegate.getLibrary(uuid);
            if (library == null) {
                commandSender.sendMessage("Notice: Skipping " + arg + " as it does not exist.");
                continue;
            }
            libraries.put(library.toJSON());
        }
        result.put("libraries", libraries);
        return result;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("...uuid");
        return args;
    }

    @Override
    public String getName() {
        return "getlibrary";
    }
}
