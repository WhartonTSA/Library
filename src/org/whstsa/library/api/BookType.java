package org.whstsa.library.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BookType {
    GENERIC("Generic"),
    FICTION("Fiction"),
    NONFICTION("Nonfiction"),
    SCIENCE_FICTION("Science Fiction"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    MYSTERY("Mystery"),
    BIOGRAPHY("Biography"),
    CHILDRENS("Children's"),
    GRAPHIC_NOVEL("Graphic Novel");

    public final String genre;

    BookType(String genre) {
        this.genre = genre;
    }

    public static BookType getGenre(String genre) {
        if (genre == null) {
            return GENERIC;
        }
        return BookType.valueOf(adaptEnumName(genre));
    }

    public static int getGenreIndex(String genre) {
        String adaptedName = adaptEnumName(genre);
        BookType bookType = BookType.valueOf(adaptedName);
        return Arrays.binarySearch(BookType.values(), bookType);
    }

    private static String adaptEnumName(String niceName) {
        return niceName.toUpperCase().replace(' ', '_').replace("'", "");
    }

    public static List<String> getGenres() {
        List<String> list = new ArrayList<>();
        for (BookType book : BookType.values()) {
            list.add(book.getGenre());
        }
        return list;
    }

    public String getGenre() {
        return this.genre;
    }
}
