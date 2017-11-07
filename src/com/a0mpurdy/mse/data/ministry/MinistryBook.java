package com.a0mpurdy.mse.data.ministry;

import com.a0mpurdy.mse.reader.MseReaderException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael Purdy on 14/03/2017.
 */
public class MinistryBook implements Serializable {

    private MinistryAuthor parentAuthor;
    private int volume;
    private ArrayList<MinistryPage> pages;
    private String name;
    private MinistryPage preface;

    MinistryBook(MinistryAuthor parentAuthor, String name, int volume) {
        this.parentAuthor = parentAuthor;
        this.name = name;
        this.volume = volume;
        this.pages = new ArrayList<>();
    }

    public MinistryAuthor getParentAuthor() {
        return parentAuthor;
    }

    public ArrayList<MinistryPage> getPages() {
        return pages;
    }

    public MinistryPage getPage(int i) {
        return pages.get(i);
    }

    public MinistryPage createNewPage(int pageNumber) throws MseReaderException {
//        if (pageNumber != this.pages.size() + 1) {
        if (this.pages.size() > 0 && (this.pages.get(this.pages.size() - 1).getPageNumber() + 1) != pageNumber) {
            System.out.println("\r" + getShortDescription() + " expected page number to be " + (this.pages.get(this.pages.size() - 1).getPageNumber() + 1) + " got " + pageNumber);
        }
        MinistryPage newPage = new MinistryPage(this, pageNumber);
        this.pages.add(newPage);
        return newPage;
    }

    public String getName() {
        return name;
    }

    public String getSerializedName() {
        return name.toLowerCase() + ".ser";
    }

    public String getSerializedPath(String folder) {
        return parentAuthor.getPath(folder) + File.separator + getSerializedName();
    }

    public String getShortDescription() {
        return parentAuthor.getShortDescription() + ":" + volume;
    }

    public int getVolume() {
        return volume;
    }

    public MinistryPage getPreface() {
        if (preface == null) {
            this.preface = new MinistryPage(this, 0);
        }
        return preface;
    }
}
