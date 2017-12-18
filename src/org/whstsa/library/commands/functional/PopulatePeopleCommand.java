package org.whstsa.library.commands.functional;

import org.json.JSONObject;
import org.whstsa.library.World;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.util.DayGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Andre on 12/07/17.
 */
public class PopulatePeopleCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        int totalPeopleAdded = Integer.parseInt(args[0]);
        ILibrary libraryBeingPopulated = ObjectDelegate.getLibrary(UUID.fromString(args[1]));
        for (int person = 0; person < totalPeopleAdded; person++) {
            DayGenerator.generateMember(libraryBeingPopulated);
        }
        commandSender.sendMessage("Successfully added " + totalPeopleAdded + " people to " + libraryBeingPopulated.getName());
        return null;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("# of people");
        args.add("library uuid");
        return args;
    }

    @Override
    public String getName() {
        return "populatepeople";
    }
}
