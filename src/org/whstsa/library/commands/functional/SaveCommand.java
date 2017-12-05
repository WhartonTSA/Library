package org.whstsa.library.commands.functional;

import org.json.JSONObject;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.Loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class SaveCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        try {
            LibraryDB.getFileDelegate().save(Loader.getLoader().computeJSON());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        commandSender.sendMessage("Successfully saved the library.");
        return null;
    }

    @Override
    public List<String> getArgs() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "save";
    }
}
