package org.whstsa.library.commands.api;

/**
 * Created by eric on 11/19/17.
 */
public interface ICommandSender {

    Type getType();

    void sendMessage(String... messages);

    void sendMessage(Object... messages);

    enum Type {
        CONSOLE
    }

}
