package org.whstsa.library.commands.getters;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by eric on 11/19/17.
 */
public class GetCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        JSONObject result = new JSONObject();
        JSONArray books = new JSONArray();
        JSONArray people = new JSONArray();
        JSONArray libraries = new JSONArray();
        JSONArray other = new JSONArray();

        Map<UUID, Serializable> serializableMap = ObjectDelegate.getCombinedMap();

        for (String arg : args) {
            UUID uuid;
            try {
                uuid = UUID.fromString(arg);
            } catch (IllegalArgumentException ex) {
                commandSender.sendMessage("Notice: Skipping " + arg + " as it is an invalid UUID.");
                continue;
            }
            Serializable serializable = serializableMap.get(uuid);
            if (serializable == null) {
                commandSender.sendMessage("Notice: Skipping " + arg + " as it does not exist.");
                continue;
            }
            if (serializable instanceof IBook) {
                books.put(serializable.toJSON());
            } else if (serializable instanceof IPerson) {
                people.put(serializable.toJSON());
            } else if (serializable instanceof ILibrary) {
                libraries.put(serializable.toJSON());
            } else {
                other.put(serializable.toJSON());
            }
        }

        result.put("books", books);
        result.put("people", people);
        result.put("libraries", libraries);
        result.put("other", other);

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
        return "get";
    }
}
