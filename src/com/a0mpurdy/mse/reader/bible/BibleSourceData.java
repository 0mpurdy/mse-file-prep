package com.a0mpurdy.mse.reader.bible;

/**
 * Created by mj_pu_000 on 17/03/2017.
 */
public class BibleSourceData {

    String author;
    String authorCode;
    String folder;
    String bookPrefix;

    public BibleSourceData(String author, String authorCode, String folder, String bookPrefix) {
        this.author = author;
        this.authorCode = authorCode;
        this.folder = folder;
        this.bookPrefix = bookPrefix;
    }
}
