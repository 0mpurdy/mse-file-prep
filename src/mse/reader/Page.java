package mse.reader;

import java.util.ArrayList;

/**
 * Created by michaelpurdy on 14/03/2017.
 */
public class Page {

    private Book parentBook;
    private ArrayList<Paragraph> paragraphs;

    public Book getParentBook() {
        return parentBook;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public Paragraph getParagraph(int i) {
        return paragraphs.get((i));
    }

    public int addParagraph(Paragraph paragraph) {
        this.paragraphs.add(paragraph);
        return this.paragraphs.size();
    }
}
