package org.whstsa.library.api;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BookType {
    GENERIC ("Generic"),
    FICTION ("Fiction"),
    NONFICTION ("Nonfiction"),
    SCIENCE_FICTION ("Science Fiction"),
    FANTASY ("Fantasy"),
    HORROR ("Horror"),
    MYSTERY ("Mystery"),
    BIOGRAPHY ("Biography"),
    CHILDRENS ("Children's"),
    GRAPHIC_NOVEL ("Graphic Novel");

    public final String genre;

    BookType(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return this.genre;
    }

    public static BookType getGenre(String genre) {
        BookType type;
        switch (genre) {
            case ("Generic"): type = GENERIC; break;
            case ("Fiction"): type = FICTION; break;
            case ("Nonfiction"): type = NONFICTION; break;
            case ("Science Fiction"): type = SCIENCE_FICTION; break;
            case ("Fantasy"): type = FANTASY; break;
            case ("Horror"): type = HORROR; break;
            case ("Mystery"): type = MYSTERY; break;
            case ("Biography"): type = BIOGRAPHY; break;
            case ("Children's"): type = CHILDRENS; break;
            case ("Graphic Novel"): type = GRAPHIC_NOVEL; break;
            default: type = null;
        }
        return type;
    }

    public static int getGenreIndex(String genre) {
        int index;
        switch (genre) {
            case ("Generic"): index = 0;  break;
            case ("Fiction"): index = 1; break;
            case ("Nonfiction"): index = 2; break;
            case ("Science Fiction"): index = 3; break;
            case ("Fantasy"): index = 4; break;
            case ("Horror"): index = 5; break;
            case ("Mystery"): index = 6; break;
            case ("Biography"): index = 7; break;
            case ("Children's"): index = 8; break;
            case ("Graphic Novel"): index = 9; break;
            default: index = -1;
        }
        return index;
    }

    public static List<String> getGenres() {
        List<String> list = new ArrayList<>();
        for (BookType book : BookType.values()) {
            list.add(book.getGenre());
        }
        return list;
    }
}
