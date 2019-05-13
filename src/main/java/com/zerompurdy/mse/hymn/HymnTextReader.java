package com.zerompurdy.mse.hymn;

import com.zerompurdy.mse.helpers.FileHelper;
import com.zerompurdy.mse.reader.MseReaderException;
import com.zerompurdy.mse_core.data.hymn.HymnBook;
import com.zerompurdy.mse_core.data.hymn.HymnVerse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mj_pu_000 on 17/03/2017.
 *
 * Parses text format hymn book into Java class
 */
public class HymnTextReader {

    private String[] hymnBookYears = {"1903", "1932", "1951", "1962", "1973"};

    /**
     * Create a list of all the hymnbooks within a folder
     * @param folder
     * @return
     */
    public List<HymnBook> readAll(String folder) {
        return FileHelper.getFiles(folder)
                .map(this::readHymnBook)
                .collect(Collectors.toList());
    }

    public HymnBook readHymnBook(String folder, String filename) {
        return readHymnBook(new File(folder + File.separator + filename));
    }

    public HymnBook readHymnBook(File hymnBookFile) {

        HymnBookBuilder hymnBook = new HymnBookBuilder(null, hymnBookFile.getName(), null);

        System.out.print("Preparing Hymns");

        try {

            System.out.print("\r\tPreparing " + hymnBookFile.getAbsolutePath());

            // make the reader
            BufferedReader brHymns = new BufferedReader(new FileReader(hymnBookFile));

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

    /**
     * Parse the next line of the hymn
     * Lines can be in the following format:
     *
     * 1) {17}
     * 2) |2|
     * 3) Some text
     *
     * In case 1, this is a new hymn
     * In case 2, this is a new verse
     * In case 3, it is the next line in the verse
     *
     * @param line The line to parse
     * @param currentVerse The current verse being processed
     * @param brHymns The reader for the file
     * @return The verse after it has been updated with the current line
     * @throws IOException
     * @throws MseReaderException If an invalid format is detected
     */
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

            try {
                // get the verse number
                int verseNumber = getVerseNumberFromLine(line);

                // start a new verse (unless verse 1 which is created when creating a new hymn)
                if (verseNumber != 1) {
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

    /**
     * Extract a title from a line
     * @param line Line containing a title for a hymn book
     * @return
     */
    private String getTitleFromLine(String line) {
        return line.substring(line.indexOf("#") + 1, line.length() - 1);
    }

    /**
     * Extract a hymn number from a line
     * @param line Line containing a hymn number
     * @return
     */
    private int getHymnNumberFromLine(String line) {
        return Integer.parseInt(line.substring(1, line.length() - 1));
    }

    /**
     * Extract a meter from a line
     * @param line Line containing a meter
     * @return
     */
    private String getMeterFromLine(String line) {
        // split the line by the comma and extract the info
        String[] info = line.split(",");
        if (info.length > 1) {
            return info[1].substring(1);
        }
        return null;
    }

    /**
     * Parse a hymn book author from a line
     * @param line Line containing a hymn book author
     * @return
     */
    private String getAuthorFromLine(String line) {
        // split the line by the comma and extract the info
        String[] info = line.split(",");
        if (info.length > 0) {
            return info[0];
        }
        return null;
    }

    /**
     * Extract a verse number from a line
     * @param line Line containing a verse number
     * @return
     * @throws NumberFormatException
     * @throws StringIndexOutOfBoundsException
     */
    private int getVerseNumberFromLine(String line) throws NumberFormatException, StringIndexOutOfBoundsException {
        return Integer.parseInt(line.substring(1, line.lastIndexOf('|')));
    }

}
