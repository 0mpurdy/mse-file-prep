package com.zerompurdy.mse_core.data.bible;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Bible for one author/translation
 *
 * @author Michael Purdy
 */
public class Bible implements Serializable {

    private static final long serialVersionUID = 4L;

    private String name;
    private String author;
    private String authorCode;
    private ArrayList<BibleBook> books;

    public Bible(String name, String author, String authorCode) {
        this.name = name;
        this.author = author;
        this.authorCode = authorCode;
        this.books = new ArrayList<>();
    }

    public String getShortDescription() {
        return this.name + " " + this.author;
    }

    public void addBook(BibleBook currentBook) {
        this.books.add(currentBook);
    }

    public BibleBook createBook(String name) {
        BibleBook nextBook = new BibleBook(this, name);
        this.books.add(nextBook);
        return nextBook;
    }

    public String getSerializedFileName() {
        return authorCode + "-bible.ser";
    }
}
