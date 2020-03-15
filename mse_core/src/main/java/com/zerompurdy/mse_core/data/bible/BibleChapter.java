package com.zerompurdy.mse_core.data.bible;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mj_pu_000 on 17/03/2017.
 */
public class BibleChapter implements Serializable {

    private static final long serialVersionUID = 4L;

    private BibleBook parentBibleBook;
    private int chapter;
    private ArrayList<BibleVerse> verses;

    public BibleChapter(BibleBook parentBibleBook, int chapter) {
        this.parentBibleBook = parentBibleBook;
        this.chapter = chapter;
        this.verses = new ArrayList<>();
    }

    public String getShortDescription() {
        return parentBibleBook.getShortDescription() + ":" + chapter;
    }

    public BibleVerse createVerse(int verse, String content) throws IllegalArgumentException {
        if (verse != this.verses.size() + 1) {
            throw new IllegalArgumentException("Verse numbers don't match up " + getShortDescription() + " expected " + this.verses.size() + 1 + " got " + verse);
        }
        BibleVerse nextVerse = new BibleVerse(this, verse, content);
        this.verses.add(nextVerse);
        return nextVerse;
    }
}
