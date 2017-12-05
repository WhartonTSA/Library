package org.whstsa.library.commands.setters;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.commands.CommandUtil;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by eric on 11/19/17.
 */
public class AddToLibraryCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length < 2) {
            return ICommand.showSyntax();
        }
        JSONObject result = new JSONObject();
        JSONArray members = new JSONArray();
        String libraryID = args[0];
        UUID libraryUUID;
        try {
            libraryUUID = UUID.fromString(libraryID);
        } catch (IllegalArgumentException ex) {
            return CommandUtil.createErrorResponse("The provided library ID is invalid.");
        }
        ILibrary library = ObjectDelegate.getLibrary(libraryUUID);
        if (library == null) {
            return CommandUtil.createErrorResponse("The provided library ID does not exist.");
        }
        List<String> userIDs = new LinkedList<>(Arrays.asList(args));
        userIDs.remove(0);
        commandSender.sendMessage(userIDs.size());
        List<IPerson> people = userIDs.stream().map(string -> {
            try {
                return UUID.fromString(string);
            } catch (IllegalArgumentException ex) {
                commandSender.sendMessage("Notice: Skipping id " + string + " as it is not a valid id.");
                return null;
            }
        }).map(ObjectDelegate::getPerson).filter(Objects::nonNull).collect(Collectors.toList());
        people.forEach(person -> {
            boolean alreadyRegistered = person.getMemberships().stream().filter(member -> member.getLibrary() == library).collect(Collectors.toList()).size() != 0;
            if (alreadyRegistered) {
                commandSender.sendMessage("Notice: Skipping id " + person.getID() + " as they are already registered.");
                return;
            }
            IMember member = library.addMember(person);
            members.put(member.toJSON());
        });
        result.put("members", members);
        return result;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("library id");
        args.add("...user ids");
        return args;
    }

    @Override
    public String getName() {
        return "joinlibrary";
    }
}
