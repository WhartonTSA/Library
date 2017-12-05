package org.whstsa.library.util;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by eric on 11/19/17.
 */
public class Readline {

    private static final List<String> trueRepresenters;
    private static final List<String> falseRepresenters;
    private static final String BOOLEAN_DISPLAY_OPTIONS_FORMAT = " [%s/%s]";

    static {
        trueRepresenters = new ArrayList<>();
        trueRepresenters.add("y");
        trueRepresenters.add("yes");
        trueRepresenters.add("true");

        falseRepresenters = new ArrayList<>();
        falseRepresenters.add("n");
        falseRepresenters.add("no");
        falseRepresenters.add("false");
    }

    private InputStream inputStream;
    private PrintStream outputStream;
    private Scanner scanner;

    public Readline(InputStream inputStream, PrintStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.scanner = new Scanner(this.inputStream);
    }

    private static boolean isMatch(String search, List<String> stringList) {
        return stringList.stream().anyMatch(str -> str.equals(search));
    }

    private static String getBooleanOptions(boolean defaultBool) {
        String trueDisplay = trueRepresenters.get(0);
        String falseDisplay = falseRepresenters.get(0);
        if (defaultBool) {
            trueDisplay = trueDisplay.toUpperCase();
        } else {
            falseDisplay = falseDisplay.toUpperCase();
        }
        return String.format(BOOLEAN_DISPLAY_OPTIONS_FORMAT, trueDisplay, falseDisplay);
    }

    public String question(String prompt) {
        this.outputStream.println(prompt);
        return this.scanner.next();
    }

    public boolean getBoolean(String question, boolean defaultBool) {
        String rawInput = this.question(question + getBooleanOptions(defaultBool)).toLowerCase();
        if (isMatch(rawInput, trueRepresenters)) {
            return true;
        } else if (isMatch(rawInput, falseRepresenters)) {
            return false;
        } else {
            return defaultBool;
        }
    }

    public boolean getBoolean(String question) {
        return this.getBoolean(question, true);
    }

}
