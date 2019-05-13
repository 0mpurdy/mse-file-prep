package com.zerompurdy.mse_core.data.hymn;

import com.zerompurdy.mse_core.data.IBook;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Hymn book
 *
 * @author MichaelPurdy
 */
public class HymnBook implements Serializable, IBook {

    private static final long serialVersionUID = 3L;

    /**
     * Title of the hymn book
     */
    private String title;
    private String filename;
    private String code;

    /**
     * List of hymns in the book
     */
    private ArrayList<Hymn> hymns;

    public HymnBook(String title, String filename, String code, ArrayList<Hymn> hymns) {
        this.title = title;
        this.filename = filename;
        this.code = code;
        this.hymns = hymns;
    }

    public HymnBook(String title, String filename, String code) {
        this(title, filename, code, new ArrayList<>());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getFilename() {
        int suffixIndex = filename.indexOf(".");
        if (suffixIndex > 0) {
            return filename.substring(0, suffixIndex);
        }
        return filename;
    }

    public String getSerializedName() {
        return getFilename() + ".ser";
    }

    public int getNumHymns() {
        return hymns.size();
    }

    public Hymn getHymn(int number) throws IndexOutOfBoundsException {
        return hymns.get(number - 1);
    }

    public int getId() {
        return 1962;
    }

    public ArrayList<Hymn> getHymns() {
        return hymns;
    }

    public void addHymn(Hymn hymn) {
        this.hymns.add(hymn);
    }

    @Override
    public String getShortDescription() {
        return this.title;
    }

    @Override
    public String getAuthor() {
        return "Hymnbooks have no author.";
    }

}
