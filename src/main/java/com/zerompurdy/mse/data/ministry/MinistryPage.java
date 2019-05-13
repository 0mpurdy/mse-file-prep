package com.zerompurdy.mse.data.ministry;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by michaelpurdy on 14/03/2017.
 */
public class MinistryPage implements Serializable {

    private MinistryBook parentBook;
    private int pageNumber;
    private ArrayList<MinistryParagraph> paragraphs;

    public MinistryPage(MinistryBook parentBook, int pageNumber) {
        this.parentBook = parentBook;
        this.pageNumber = pageNumber;
        this.paragraphs = new ArrayList<>();
    }

    public int getNumber() {
        return parentBook.getPages().indexOf(this) + 1;
    }

    public MinistryBook getParentBook() {
        return parentBook;
    }

    public ArrayList<MinistryParagraph> getParagraphs() {
        return paragraphs;
    }

    public MinistryParagraph getParagraph(int i) {
        return paragraphs.get((i));
    }

    public int addParagraph(MinistryParagraph paragraph) {
        this.paragraphs.add(paragraph);
        return this.paragraphs.size();
    }

    public String getShortDescription() {
        return parentBook.getShortDescription() + ":" + pageNumber;
    }

    public MinistryParagraph createNewParagraph() {
        MinistryParagraph nextParagraph = new MinistryParagraph(this);
        this.paragraphs.add(nextParagraph);
        return nextParagraph;
    }

    public MinistryParagraph createNewTitleParagraph(String title) {
        MinistryParagraph nextParagraph = new MinistryParagraph(this, title);
        this.paragraphs.add(nextParagraph);
        return nextParagraph;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
