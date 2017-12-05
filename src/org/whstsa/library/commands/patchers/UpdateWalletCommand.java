package org.whstsa.library.commands.patchers;

import org.json.JSONObject;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.commands.CommandUtil;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 11/19/17.
 */
public class UpdateWalletCommand implements ICommand {

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length < 2) {
            return ICommand.showSyntax();
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(args[0]);
        } catch (IllegalArgumentException ex) {
            return CommandUtil.createErrorResponse("The provided UUID is invalid.");
        }
        IPerson person = ObjectDelegate.getPerson(uuid);
        if (person == null) {
            return CommandUtil.createErrorResponse("A person with the given ID could not be found.");
        }
        double wallet;
        try {
            wallet = Double.parseDouble(args[1]);
        } catch (NullPointerException | NumberFormatException ex) {
            return CommandUtil.createErrorResponse("The provided wallet amount is invalid.");
        }
        if (person instanceof Person) {
            ((Person) person).impl_setMoney(Double.valueOf(args[1]));
        } else {
            person.addMoney(Double.valueOf(args[1]));
        }
        return person.toJSON();
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("uuid");
        args.add("money");
        return args;
    }

    @Override
    public String getName() {
        return "updatewallet";
    }

}
