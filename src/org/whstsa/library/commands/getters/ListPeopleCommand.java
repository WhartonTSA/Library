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
public class ListPeopleCommand implements ICommand {

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        boolean full = args.length == 0 ? false : Boolean.valueOf(args[0]);
        JSONObject object = new JSONObject();
        ObjectDelegate.getPeople().forEach(person -> {
            if (full) {
                object.put(person.getID().toString(), person.toJSON());
            } else {
                object.put(person.getID().toString(), person.getName());
            }
        });
        return object;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("full");
        return args;
    }

    @Override
    public String getName() {
        return "listpeople";
    }

}
