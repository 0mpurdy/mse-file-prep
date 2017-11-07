package com.a0mpurdy.mse.data.bible;

import com.a0mpurdy.mse.reader.MseReaderException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mj_pu_000 on 17/03/2017.
 */
public class BibleChapter implements Serializable {

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

    public BibleVerse createVerse(int verse, String content) throws MseReaderException {
        if (verse != this.verses.size() + 1) {
            throw new MseReaderException("Verse numbers don't match up " + getShortDescription() + " expected " + this.verses.size() + 1 + " got " + verse);
        }
        BibleVerse nextVerse = new BibleVerse(this, verse, content);
        this.verses.add(nextVerse);
        return nextVerse;
    }
}
