package org.whstsa.library.commands.getters;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 11/19/17.
 */
public class GetPersonCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        JSONObject result = new JSONObject();
        JSONArray people = new JSONArray();
        for (String arg : args) {
            UUID uuid;
            try {
                uuid = UUID.fromString(arg);
            } catch (IllegalArgumentException ex) {
                commandSender.sendMessage("Notice: Skipping " + arg + " as it is an invalid UUID.");
                continue;
            }
            IPerson person = ObjectDelegate.getPerson(uuid);
            if (person == null) {
                commandSender.sendMessage("Notice: Skipping " + arg + " as it does not exist.");
                continue;
            }
            people.put(person.toJSON());
        }
        result.put("people", people);
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
        return "getperson";
    }
}
