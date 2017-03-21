package mse.data.ministry;

import mse.reader.MseReaderException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by michaelpurdy on 14/03/2017.
 */
public class MinistryAuthor implements Serializable {

    private final String name;
    private final String folder;
    private final String filePrefix;
    private int failedVolumes = 0;
    private ArrayList<MinistryBook> books;

    public MinistryAuthor(String name, String folder, String filePrefix) {
        this.filePrefix = filePrefix;
        this.name = name;
        this.folder = folder;
        this.books = new ArrayList<>();
    }

    public MinistryBook addNewBook(String name, int volume) {
        MinistryBook newBook = new MinistryBook(this, name, volume);
        this.books.add(newBook);
        return newBook;
    }

    public ArrayList<MinistryBook> getBooks() {
        return books;
    }

    public MinistryBook getBook(int i) {
        return books.get(i);
    }

    public String getName() {
        return name;
    }

    public String getFolder() {
        return folder;
    }

    public String getPath(String folder) {
        return folder + File.separator + getFolder();
    }

    public String getSerializedName() {
        return filePrefix + ".ser";
    }

    public String getShortDescription() {
        return name;
    }

    public MinistryBook createNewVolume(String name, int volumeNumber) throws MseReaderException {
        if (volumeNumber > this.books.size() + 1 + failedVolumes) {
            failedVolumes++;
            throw new MseReaderException("\r" + getShortDescription() + " - expected volume " + (this.books.size() + failedVolumes) + " got " + volumeNumber);
        }
        MinistryBook nextBook = new MinistryBook(this, name, volumeNumber);
        this.books.add(nextBook);
        return nextBook;
    }
}
