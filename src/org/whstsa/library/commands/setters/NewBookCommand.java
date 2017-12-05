package org.whstsa.library.commands.setters;

import org.apache.commons.lang3.EnumUtils;
import org.json.JSONObject;
import org.whstsa.library.api.BookType;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.impl.Book;
import org.whstsa.library.commands.api.ICommand;
import org.whstsa.library.commands.api.ICommandSender;
import org.whstsa.library.db.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 11/19/17.
 */
public class NewBookCommand implements ICommand {
    @Override
    public JSONObject handle(String[] args, ICommandSender commandSender) {
        if (args.length < 3) {
            return ICommand.showSyntax();
        }
        String bookName = args[0];
        String authorName = args[1];
        BookType genre = EnumUtils.getEnum(BookType.class, args[2].toUpperCase());
        if (genre == null) {
            genre = BookType.GENERIC;
        }
        IBook book = new Book(bookName, authorName, genre);
        Loader.getLoader().loadBook(book);
        return book.toJSON();
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>();
        args.add("book name");
        args.add("author name");
        args.add("genre");
        return args;
    }

    @Override
    public String getName() {
        return "newbook";
    }
}
