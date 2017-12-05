package org.whstsa.library.commands.setters;

import org.json.JSONObject;
import org.whstsa.library.World;
import org.whstsa.library.api.DateUtils;
import org.whstsa.library.commands.CommandUtil;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class SetDaysCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length < 1) {
            return ICommand.showSyntax();
        }
        if (args[0].equals("reset")) {
            World.setDate(null);
            JSONObject result = new JSONObject();
            result.put("date", World.getDate());
            return result;
        }
        int days;
        try {
            days = Integer.parseInt(args[0]);
        } catch (NullPointerException | NumberFormatException ex) {
            return CommandUtil.createErrorResponse("The provided days number is invalid.");
        }
        Date currentDate = World.getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        World.setDate(cal.getTime());
        JSONObject result = new JSONObject();
        result.put("date", DateUtils.toDateString(World.getDate()));
        return result;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("days");
        return args;
    }

    @Override
    public String getName() {
        return "setdays";
    }
}
