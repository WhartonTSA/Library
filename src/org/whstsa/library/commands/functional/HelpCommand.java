package org.whstsa.library.commands.functional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.util.CommandWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class HelpCommand implements ICommand {

    private CommandWatcher commandWatcher;

    public HelpCommand(CommandWatcher watcher) {
        this.commandWatcher = watcher;
    }

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        List<ICommand> commands = this.commandWatcher.getCommands();
        JSONObject commandList = new JSONObject();
        commands.forEach(command -> {
            JSONObject commandMeta = new JSONObject();
            JSONArray argList = new JSONArray();
            command.getArgs().forEach(argList::put);
            commandMeta.put("arguments", argList);
            commandList.put(command.getName(), commandMeta);
        });
        JSONObject result = new JSONObject();
        return result.put("commands", commandList);
    }

    @Override
    public List<String> getArgs() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "help";
    }
}
