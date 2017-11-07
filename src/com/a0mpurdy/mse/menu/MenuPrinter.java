package com.a0mpurdy.mse.menu;

import com.a0mpurdy.mse.data.author.Author;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Michael Purdy on 27/10/2016.
 */
public class MenuPrinter {

    public enum Menu {
        MAIN(new String[]{"Close",
                "Prepare all files",
                "Create all indexes",
                "Create all serial files",
                "Test",
                "Other",
                "Debug"}),
        OTHER(new String[]{"Back",              // 0
                "Prepare single author",
                "Create single author index",
                "Create super index",
                "Check author index",
                "Check all author indexes",     // 5
                "Create serial Bibles",
                "Create serial Hymns",
                "Read serial Hymns",
                "Benchmark"}),
        DEBUG(new String[]{"Back",
                "Read single author text",
                "Benchmark"}),
        AUTHOR(null);

        String[] options;

        Menu(String[] options) {
            this.options = options;

            // assume null is author
            if (options == null) {
                this.options = Arrays.stream(Author.values())
                        .map(Author::getName)
                        .toArray(String[]::new);
            }
        }
    }

    static void printMenu(String[] menu) {
        System.out.println();
        int i = 0;
        for (String option : menu) {
            System.out.println(i + " - " + option);
            i++;
        }
        System.out.print("Choose an option: ");
    }

    static void printMenu(ArrayList<String> menu) {
        // [0] used because: https://stackoverflow.com/questions/4042434/converting-arrayliststring-to-string-in-java
        printMenu(menu.toArray(new String[0]));
    }

    public static void printMenu(Menu menu) {
        printMenu(menu.options);
    }

}
