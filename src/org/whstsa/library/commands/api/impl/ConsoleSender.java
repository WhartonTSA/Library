package org.whstsa.library.commands.api.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.whstsa.library.commands.api.ICommandSender;

public class ConsoleSender implements ICommandSender {

    private static final ConsoleSender consoleSender = new ConsoleSender();

    private ConsoleSender() {
    }

    public static ConsoleSender getConsoleSender() {
        return consoleSender;
    }

    @Override
    public Type getType() {
        return Type.CONSOLE;
    }

    @Override
    public void sendMessage(String... messages) {
        JSONObject result = new JSONObject();
        JSONArray messageOutput = new JSONArray();
        for (String message : messages) {
            messageOutput.put(message);
        }
        result.put("messages", messageOutput);
        System.out.println(result.toString(4));
    }

    @Override
    public void sendMessage(Object... messages) {
        JSONObject result = new JSONObject();
        JSONArray messageOutput = new JSONArray();
        for (Object message : messages) {
            if (message instanceof JSONObject) {
                System.out.println(((JSONObject) message).toString(4));
                continue;
            }
            messageOutput.put(message.toString());
        }
        if (messageOutput.length() == 0) {
            return;
        }
        result.put("messages", messageOutput);
        System.out.println(result.toString(4));
    }

}
