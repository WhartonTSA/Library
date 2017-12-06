package org.whstsa.library.commands.functional;

import org.json.JSONObject;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.Loader;
import org.whstsa.library.util.DayGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class DayGeneratorCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        DayGenerator.simulateDay();
        commandSender.sendMessage("Successfully generated day.");
        return null;
    }

    @Override
    public List<String> getArgs() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "generateday";
    }
}
