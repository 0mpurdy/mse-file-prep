package mse.data.hymn;

import mse.reader.MseReaderException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Hymn with verses
 *
 * @author Michael Purdy
 */
public class Hymn implements Serializable {

    private HymnBook parentHymnBook;
    private int number;
    private String author;
    private String meter;
    private ArrayList<HymnVerse> verses;
    private HymnChorus chorus;

    public Hymn(HymnBook parentHymnBook, int number, String author, String meter) {
        this.parentHymnBook = parentHymnBook;
        this.number = number;
        this.author = author;
        this.meter = meter;
        this.verses = new ArrayList<>();
    }

    public HymnBook getParentHymnBook() {
        return parentHymnBook;
    }

    public String getShortDescription() {
        return this.parentHymnBook.getShortDescription() + ":" + number;
    }

    public HymnVerse createNewVerse(int verseNumber) throws MseReaderException {
        if (verseNumber == -1) {
            HymnChorus chorus = new HymnChorus(this);
            this.chorus = chorus;
            return chorus;
        }
        if (verseNumber != this.verses.size() + 1) {
            throw new MseReaderException("Unexpected verse number " + getShortDescription() + " got " + verseNumber + " expected " + this.verses.size() + 1);
        }
        HymnVerse nextVerse = new HymnVerse(this, verseNumber);
        this.verses.add(nextVerse);
        return nextVerse;
    }

    public int getNumber() {
        return number;
    }
}
