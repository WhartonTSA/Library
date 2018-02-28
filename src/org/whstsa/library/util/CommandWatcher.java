package org.whstsa.library.util;

import org.json.JSONObject;
import org.whstsa.library.commands.InputListener;
import org.whstsa.library.commands.InputRunner;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.commands.api.impl.ConsoleSender;
import org.whstsa.library.commands.functional.*;
import org.whstsa.library.commands.getters.*;
import org.whstsa.library.commands.setters.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by eric on 11/19/17.
 */
public class CommandWatcher implements InputListener {

    private static final String COMMAND_SYNTAX_ARGUMENT_FORMAT = " <%s>";
    private final InputStream inputStream;
    private final PrintStream printStream;
    private boolean watching;
    private Map<String, ICommand> commandMap;

    public CommandWatcher(InputStream inputStream, PrintStream printStream) {
        this.inputStream = inputStream;
        this.printStream = printStream;
        this.commandMap = new HashMap<>();
        this.loadCommands();
        this.watching = false;
    }

    private static String buildHelpPrompt(ICommand command) {
        StringBuilder helpPrompt = new StringBuilder();
        helpPrompt.append(command.getName());
        command.getArgs().forEach(arg -> helpPrompt.append(String.format(COMMAND_SYNTAX_ARGUMENT_FORMAT, arg)));
        return helpPrompt.toString();
    }

    public void inputReceived(String input) {
        List<String> argv = Arrays.asList(input.split("(?<!\\\\)\\s+"));
        argv = argv.stream().map(str -> str.replace("\\ ", " ")).collect(Collectors.toList());
        String[] args = new String[argv.size() - 1];
        for (int i = 1; i < argv.size(); i++) {
            args[i - 1] = argv.get(i);
        }
        String commandKey = argv.get(0);
        ICommandSender commandSender = ConsoleSender.getConsoleSender();
        if (this.commandMap.containsKey(commandKey)) {
            ICommand command = this.commandMap.get(commandKey);
            JSONObject result = command.handle(args, commandSender);
            if (result == null) {
                return;
            }
            if (result.has(ICommand.HELP_FLAG) && result.getBoolean(ICommand.HELP_FLAG)) {
                commandSender.sendMessage(buildHelpPrompt(command));
            } else {
                commandSender.sendMessage(result);
            }
        } else {
            commandSender.sendMessage("Unknown command, type /help for a list of commands.");
        }
    }

    public void run() {
        new Thread(() -> {
            InputRunner inputRunner = new InputRunner();
            inputRunner.addListener(this);
            inputRunner.run();
        }).start();
    }

    public List<ICommand> getCommands() {
        return Collections.unmodifiableList(new ArrayList<>(this.commandMap.values()));
    }

    private void loadCommands() {
        List<ICommand> commands = new ArrayList<>();

        commands.add(new GetLibraryCommand());
        commands.add(new NewLibraryCommand());
        commands.add(new ListLibrariesCommand());
        commands.add(new AddToLibraryCommand());
        commands.add(new AddBookToLibraryCommand());

        commands.add(new CheckoutBookCommand());
        commands.add(new ReturnBookCommand());

        commands.add(new GetPersonCommand());
        commands.add(new NewPersonCommand());
        commands.add(new ListPeopleCommand());

        commands.add(new GetBookCommand());
        commands.add(new ListBooksCommand());
        commands.add(new NewBookCommand());

        commands.add(new GetCommand());

        commands.add(new SetDaysCommand());

        commands.add(new SaveCommand());

        commands.add(new HelpCommand(this));

        commands.add(new DayGeneratorCommand());
        commands.add(new PopulatePeopleCommand());
        commands.add(new PopulateBooksCommand());


        commands.forEach(command -> this.commandMap.put(command.getName(), command));
    }
}
