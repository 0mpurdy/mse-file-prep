package com.zerompurdy.mse_core.data.bible;

import java.io.Serializable;

/**
 * Created by mj_pu_000 on 17/03/2017.
 */
public class BibleVerse  implements Serializable {

    private static final long serialVersionUID = 4L;

    private BibleChapter parentBibleChapter;
    private int verse;
    private String content;

    public BibleVerse(BibleChapter parentBibleChapter, int verse, String content) {
        this.parentBibleChapter = parentBibleChapter;
        this.verse = verse;
        this.content = content;
    }
}
