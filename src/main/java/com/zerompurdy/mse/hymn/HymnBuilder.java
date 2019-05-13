package com.zerompurdy.mse.hymn;

import com.zerompurdy.mse.reader.MseReaderException;
import com.zerompurdy.mse_core.data.hymn.Hymn;
import com.zerompurdy.mse_core.data.hymn.HymnBook;
import com.zerompurdy.mse_core.data.hymn.HymnVerse;

/**
 * Hymn with verses
 *
 * @author Michael Purdy
 */
public class HymnBuilder {

    Hymn innerHymn;

    public HymnBuilder(HymnBook parentHymnBook, int number, String author, String meter) {
        innerHymn = new Hymn(parentHymnBook, number, author, meter);
    }

    public static HymnVerse createNewVerse(Hymn hymn, int verseNumber) throws MseReaderException {
        if (verseNumber == -1) {
            HymnVerse chorus = new HymnVerse(hymn, -1);
            hymn.setChorus(chorus);
            return chorus;
        }
        if (verseNumber != hymn.getVerses().size() + 1) {
            throw new MseReaderException("Unexpected verse number " +
                    hymn.getShortDescription() + " got " +
                    verseNumber + " expected " + hymn.getVerses().size() + 1);
        }
        HymnVerse nextVerse = new HymnVerse(hymn, verseNumber);
        hymn.addVerse(nextVerse);
        return nextVerse;
    }

    public HymnVerse createNewVerse(int verseNumber) throws MseReaderException {
        return createNewVerse(innerHymn, verseNumber);
    }

    public Hymn asHymn() {
        return innerHymn;
    }

}
