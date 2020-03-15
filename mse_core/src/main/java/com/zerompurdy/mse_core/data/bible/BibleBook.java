package com.zerompurdy.mse_core.data.bible;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mj_pu_000 on 17/03/2017.
 */
public class BibleBook implements Serializable {

    private static final long serialVersionUID = 4L;

    private Bible parentBible;
    private String name;
    private ArrayList<BibleChapter> chapters;

    public BibleBook(Bible parentBible, String name) {
        this.parentBible = parentBible;
        this.name = name;
        this.chapters = new ArrayList<>();
    }

    public BibleChapter createNewChapter(int chapter) throws IllegalArgumentException {
        if (chapter != this.chapters.size() + 1) {
            throw new IllegalArgumentException("Chapter numbers don't match up " + getShortDescription() + " expected " + this.chapters.size() + 1 + " got " + chapter);
        }
        BibleChapter nextChapter = new BibleChapter(this, chapter);
        this.chapters.add(nextChapter);
        return nextChapter;
    }

    public BibleChapter getLastChapter() {
        return this.chapters.get(this.chapters.size() - 1);
    }

    public String getShortDescription() {
        return this.parentBible.getShortDescription() + " " + name;
    }
}
