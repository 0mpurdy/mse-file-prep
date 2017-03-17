package mse.reader;

import java.util.ArrayList;

/**
 * Created by michaelpurdy on 14/03/2017.
 */
public class Book {

    private Author parentAuthor;
    private ArrayList<Page> pages;

    public Author getParentAuthor() {
        return parentAuthor;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public Page getPage(int i) {
        return pages.get(i);
    }

    public Page newPage(int pageNumber) {

    }

}
