package mse.data.hymn;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael Purdy on 17/03/2017.
 */
public class HymnVerse implements Serializable {

    private Hymn parentHymn;
    private int verseNumber;
    private ArrayList<String> lines;

    public HymnVerse(Hymn parentHymn, int verseNumber) {
        this.parentHymn = parentHymn;
        this.verseNumber = verseNumber;
        this.lines = new ArrayList<>();
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public String getShortDescription() {
        return this.parentHymn.getShortDescription() + ":" + verseNumber;
    }

    public Hymn getParentHymn() {
        return parentHymn;
    }
}
