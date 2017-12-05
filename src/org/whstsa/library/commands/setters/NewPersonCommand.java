package org.whstsa.library.commands.setters;

import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class NewPersonCommand implements ICommand {

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (3 > args.length) {
            return ICommand.showSyntax();
        }
        IPerson person = new Person(args[0], args[1], Boolean.valueOf(args[2]));
        Loader.getLoader().loadPerson(person);
        return person.toJSON();
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("first name");
        args.add("last name");
        args.add("teacher");
        return args;
    }

    @Override
    public String getName() {
        return "newperson";
    }

}
