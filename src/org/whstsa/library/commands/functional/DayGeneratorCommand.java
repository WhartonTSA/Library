package org.whstsa.library.commands.functional;

import org.json.JSONObject;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.World;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.Loader;
import org.whstsa.library.util.DayGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Andre on 12/7/17
 */
public class DayGeneratorCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        int totalDaysAdvanced = Integer.parseInt(args[0]);
        for (int currentDate = 0; currentDate < totalDaysAdvanced; currentDate++) {
            System.out.println("x");
            DayGenerator.simulateDay();
            Calendar cal = Calendar.getInstance();
            cal.setTime(World.getDate());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            World.setDate(cal.getTime());
            System.out.println(currentDate);
        }
        commandSender.sendMessage("Successfully generated day.");
        return null;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("days");
        return args;
    }

    @Override
    public String getName() {
        return "generateday";
    }
}
