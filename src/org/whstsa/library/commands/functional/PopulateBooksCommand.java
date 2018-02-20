package org.whstsa.library.commands.functional;

import org.json.JSONObject;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.util.DayGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 12/07/17.
 */
public class PopulateBooksCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        int totalBooksAdded;
        try {
            totalBooksAdded = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
        for (int person = 0; person < totalBooksAdded; person++) {
            DayGenerator.generateBook(DayGenerator.randomLibrary());
        }
        commandSender.sendMessage("Successfully added " + totalBooksAdded + " books to random libraries.");
        return null;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("# of books");
        return args;
    }

    @Override
    public String getName() {
        return "populatebooks";
    }
}