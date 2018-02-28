package org.whstsa.library.commands.setters;

import org.json.JSONObject;
import org.whstsa.library.api.exceptions.CheckedInException;
import org.whstsa.library.api.exceptions.MemberMismatchException;
import org.whstsa.library.api.exceptions.OutstandingFinesException;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.ObjectDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReturnBookCommand implements ICommand {

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length == 0) {
            return ICommand.showSyntax();
        }
        String checkoutID = args[0];
        List<ICheckout> checkouts = new ArrayList<>();
        ObjectDelegate.getLibraries().get(0).getCheckouts().values().forEach(checkouts::addAll);
        List<ICheckout> matches = checkouts.stream().filter(checkout -> checkout.getID().toString().equals(checkoutID)).collect(Collectors.toList());
        if (matches.size() == 0) {
            JSONObject response = new JSONObject();
            response.put("error", "Checkout does not exist");
            return response;
        }
        ICheckout checkout = matches.get(0);
        try {
            checkout.getOwner().checkIn(checkout);
        } catch (OutstandingFinesException | MemberMismatchException | CheckedInException e) {
            JSONObject response = new JSONObject();
            response.put("error", e.getMessage());
            return response;
        }
        JSONObject success = new JSONObject();
        success.put("success", true);
        return success;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("checkout id");
        return args;
    }

    @Override
    public String getName() {
        return "returnbook";
    }

}
