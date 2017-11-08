package com.a0mpurdy.mse.reader.hymn;

import com.a0mpurdy.mse.data.hymn.HymnBookBuilder;
import com.a0mpurdy.mse.data.hymn.HymnBuilder;
import com.a0mpurdy.mse.reader.MseReaderException;
import com.a0mpurdy.mse_core.data.hymn.HymnBook;
import com.a0mpurdy.mse_core.data.hymn.HymnVerse;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by mj_pu_000 on 17/03/2017.
 */
public class HymnTextReader {

    private String[] hymnBookYears = {"1903", "1932", "1951", "1962", "1973"};

    public ArrayList<HymnBook> readAll(String folder) {
        ArrayList<HymnBook> hymnBooks = new ArrayList<>();
        for (String hymnBookYear : hymnBookYears) {
            hymnBooks.add(readHymnBook(folder, "hymns" + hymnBookYear));
        }
        return hymnBooks;
    }

    public HymnBook readHymnBook(String folder, String filename) {

        HymnBookBuilder hymnBook = new HymnBookBuilder(null, filename, null);

        System.out.print("Preparing Hymns");

        try {

            String filePath = folder + File.separator + filename + ".txt";
            System.out.print("\r\tPreparing " + filePath);

            // make the reader
            BufferedReader brHymns = new BufferedReader(new FileReader(filePath));

            // read the first line of the hymn book
            String line = brHymns.readLine();

            // add the title to the hymn book
            hymnBook.setTitle(getTitleFromLine(line));

            // create the first verse to pass in
            HymnVerse currentVerse = hymnBook.createNewHymn(1, "invalid", "invalid").createNewVerse(1);

            // read the second line of the hymn book
            line = brHymns.readLine();

            // if there are still more lines
            while (line != null) {

                currentVerse = parseLine(line, currentVerse, brHymns);

                // read the next line of the hymn
                line = brHymns.readLine();

            }

            // close the reader and writer
            brHymns.close();

        } catch (IOException e) {
            System.out.println("\nError reading hymns from text " + hymnBook.getShortDescription());
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (MseReaderException e) {
            System.out.println("\nError reading hymns from text " + hymnBook.getShortDescription());
            System.out.println(e.getMessage());
        }

        return hymnBook.asHymnBook();
    }

    private HymnVerse parseLine(String line, HymnVerse currentVerse, BufferedReader brHymns) throws IOException, MseReaderException {

        // if it is a new hymn
        if (line.indexOf("{") == 0) {

            // get the hymn number
            int hymnNumber = getHymnNumberFromLine(line);

            // get the meter
            line = brHymns.readLine();
            String meter = getMeterFromLine(line);

            // get the author
            String author = getAuthorFromLine(line);

            // create new Hymn
            if (hymnNumber > 1) {
                currentVerse = HymnBookBuilder.createNewHymn(currentVerse.getParentHymn().getParentHymnBook(), hymnNumber, author, meter).createNewVerse(1);
            }

        } else if (line.indexOf("|") == 0) {
            // if it is a new verse

//            if (currentVerse.getParentHymn().getNumber() == 83) {
//                System.out.println("This one");
//            }
//            if (currentVerse.getParentHymn().getNumber() == 149) {
//                System.out.println("This one");
//            }

            try {
                // get the verse number
                int verseNumber = getVerseNumberFromLine(line);

                // start a new verse (unless verse 1 which is created when creating a new hymn)
                if (verseNumber > 1) {
                    currentVerse = HymnBuilder.createNewVerse(currentVerse.getParentHymn(), verseNumber);
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                throw new MseReaderException("Could not format new verse number \"" + line + "\" after " + currentVerse.getShortDescription());
            }

        } else {
            // if it is a verse line
            currentVerse.addLine(line);
        }

        return currentVerse;
    }

    private String getTitleFromLine(String line) {
        return line.substring(line.indexOf("#") + 1, line.length() - 1);
    }

    private int getHymnNumberFromLine(String line) {
        return Integer.parseInt(line.substring(1, line.length() - 1));
    }

    private String getMeterFromLine(String line) {
        // split the line by the comma and extract the info
        String[] info = line.split(",");
        if (info.length > 1) {
            return info[1].substring(1);
        }
        return null;
    }

    private String getAuthorFromLine(String line) {
        // split the line by the comma and extract the info
        String[] info = line.split(",");
        if (info.length > 0) {
            return info[0];
        }
        return null;
    }

    private int getVerseNumberFromLine(String line) throws NumberFormatException, StringIndexOutOfBoundsException {
        return Integer.parseInt(line.substring(1, line.lastIndexOf('|')));
    }

}
