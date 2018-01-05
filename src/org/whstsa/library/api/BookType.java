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

    public static List<String> getGenres() {
        List<String> list = new ArrayList<>();
        for (BookType book : BookType.values()) {
            list.add(book.getGenre());
        }
        return list;
    }
}
