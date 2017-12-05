package org.whstsa.library.commands.setters;

import org.json.JSONObject;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class NewLibraryCommand implements ICommand {

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length == 0) {
            return ICommand.showSyntax();
        }
        StringBuilder libraryNameBuilder = new StringBuilder();
        for (String arg : args) {
            libraryNameBuilder.append(arg);
            libraryNameBuilder.append(' ');
        }
        String libraryName = libraryNameBuilder.toString().trim();
        ILibrary library = new Library(libraryName);
        Loader.getLoader().loadLibrary(library);
        return library.toJSON();
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("library name");
        return args;
    }

    @Override
    public String getName() {
        return "newlibrary";
    }

}
