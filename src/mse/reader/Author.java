package mse.reader;

import java.util.ArrayList;

/**
 * Created by michaelpurdy on 14/03/2017.
 */
public class Author {

    private ArrayList<Book> books;

    public ArrayList<Book> getBooks() {
        return books;
    }

    public Book getBook(int i) {
        return books.get(i);
    }

}
