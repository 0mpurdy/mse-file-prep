package com.a0mpurdy.mse.hymn;

import com.a0mpurdy.mse.reader.MseReaderException;
import com.a0mpurdy.mse_core.data.hymn.HymnBook;

/**
 * Hymn book that contains hymns
 *
 * @author Michael Purdy
 */
public class HymnBookBuilder {

    private HymnBook innerBook;

    public HymnBookBuilder(String title, String filename, String code) {
        innerBook = new HymnBook(title, filename, code);
    }

    public void setTitle(String title) {
        innerBook.setTitle(title);
    }

    public String getShortDescription() {
        return innerBook.getShortDescription();
    }

    public static HymnBuilder createNewHymn(HymnBook hymnBook, int hymnNumber, String author, String meter) throws MseReaderException {
        if (hymnNumber != hymnBook.getHymns().size() + 1) {
            throw new MseReaderException("Unexpected hymn number " + hymnBook.getShortDescription() +
                    " got " + hymnNumber + " expected " +
                    hymnBook.getHymns().size() + 1);
        }
        HymnBuilder nextHymn = new HymnBuilder(hymnBook, hymnNumber, author, meter);
        hymnBook.addHymn(nextHymn.asHymn());
        return nextHymn;
    }

    public HymnBuilder createNewHymn(int hymnNumber, String author, String meter) throws MseReaderException {
        return createNewHymn(innerBook, hymnNumber, author, meter);
    }

    public HymnBook asHymnBook() {
        return innerBook;
    }
}
