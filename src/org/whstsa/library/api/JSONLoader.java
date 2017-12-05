package org.whstsa.library.api;

import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;

import java.io.File;
import java.util.List;

public class JSONLoader {

    private List<IBook> books;
    private List<ILibrary> libraries;

    public JSONLoader(File file) {
        if (file == null || !file.exists() || !file.isFile() || !file.canRead()) {
            throw new RuntimeException("Cannot read file " + (file != null ? file.toString() : null));
        }

    }
}
