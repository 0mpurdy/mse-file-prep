package mse.data.hymn;

import mse.reader.MseReaderException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Hymn book that contains hymns
 *
 * @author Michael Purdy
 */
public class HymnBook implements Serializable {

    private String title;
    private String filename;
    private String code;
    private ArrayList<Hymn> hymns;

    public HymnBook(String title, String filename, String code) {
        this.title = title;
        this.filename = filename;
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return this.title;
    }

    public Hymn createNewHymn(int hymnNumber, String author, String meter) throws MseReaderException {
        if (hymnNumber != hymns.size() + 1) {
            throw new MseReaderException("Unexpected hymn number " + getShortDescription() + " got " + hymnNumber + " expected " + hymns.size() + 1);
        }
        Hymn nextHymn = new Hymn(this, hymnNumber, author, meter);
        this.hymns.add(nextHymn);
        return nextHymn;
    }

    public Hymn setFirstHymn(int hymnNumber, String author, String meter) throws MseReaderException {
        if (hymnNumber != 1) {
            throw new MseReaderException("Unexpected hymn number " + getShortDescription() + " " + hymnNumber);
        }
        Hymn nextHymn = new Hymn(this, hymnNumber, author, meter);
        this.hymns = new ArrayList<>();
        this.hymns.add(nextHymn);
        return nextHymn;
    }

    public String getSerializedName() {
        return filename + ".ser";
    }
}
