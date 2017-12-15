package org.whstsa.library.commands.functional;

import org.json.JSONObject;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.World;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.Loader;
import org.whstsa.library.util.DayGenerator;
import org.whstsa.library.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Andre on 12/7/17
 */
public class DayGeneratorCommand implements ICommand {

    private static Logger LOGGER = new Logger("DayGenerator");

    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        int totalDaysAdvanced = Integer.parseInt(args[0]);
        for (int currentDate = 0; currentDate< totalDaysAdvanced; currentDate++) {
            LOGGER.debug("" + World.getDate());
            LOGGER.debug();
            DayGenerator.simulateDay();
            Calendar cal = Calendar.getInstance();
            cal.setTime(World.getDate());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            World.setDate(cal.getTime());
            LOGGER.debug();
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
