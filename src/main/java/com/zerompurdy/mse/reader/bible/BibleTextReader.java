package com.zerompurdy.mse.reader.bible;

import com.zerompurdy.mse.data.bible.Bible;
import com.zerompurdy.mse.data.bible.BibleBook;
import com.zerompurdy.mse.reader.MseReaderException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads bible text into a bible object
 *
 * @author Michael Purdy
 */
public class BibleTextReader {



    private String[] bookNames = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
            "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings",
            "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra",
            "Nehemiah", "Esther", "Job", "Psalms",
            "Proverbs", "Ecclesiastes", "Song Of Songs", "Isaiah",
            "Jeremiah", "Lamentations", "Ezekiel", "Daniel",
            "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum",
            "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi",
            "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians",
            "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians",
            "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon",
            "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John",
            "3 John", "Jude", "Revelation"
    };

    public ArrayList<Bible> readAll(String sourcePath) {
        ArrayList<BibleSourceData> sources = new ArrayList<>();
        sources.add(new BibleSourceData("JND", "jnd", "bible", "bible"));
        sources.add(new BibleSourceData("KJV", "kjv", "kjv", "kjv"));

        ArrayList<Bible> bibles = new ArrayList<>();

        for (BibleSourceData sourceData : sources) {
            bibles.add(readVersion(
                    sourceData.author + " Bible",
                    sourceData.author,
                    sourceData.authorCode,
                    sourcePath + File.separator + sourceData.folder,
                    sourceData.bookPrefix
            ));
        }

        return bibles;
    }

    /**
     * Read in a version of the Bible from text
     *
     * @param name       Name of the bible version eg: "JND"
     * @param sourcePath Path to the source folder
     * @return
     */
    public Bible readVersion(String name, String author, String authorCode, String bookPrefix, String sourcePath) {

        System.out.print("\rReading " + name + " from " + sourcePath);

        Bible bible = new Bible(name, author, authorCode);
        StringBuilder errorMessages = new StringBuilder();

        int bookNumber = 0;
        for (String bookName : bookNames) {
            bookNumber += 1;
            try {

                BibleBook book = readSingleBook(sourcePath + File.separator + bookPrefix + bookNumber + ".txt", bookName, bible);
                System.out.print("\rFinished " + book.getShortDescription());

            } catch (IOException e) {
                errorMessages.append("IO exception when reading " + bookName + "\n" + e.getMessage() + "\n");
            } catch (MseReaderException e) {
                errorMessages.append("MSE Reader exception when reading " + bookName + "\n" + e.getMessage() + "\n");
            }
        }

        return bible;
    }

    public BibleBook readSingleBook(String bookPath, String bookName, Bible bible) throws MseReaderException, IOException {

        BibleBook currentBook = bible.createBook(bookName);

        // create buffered reader to read the text
        BufferedReader br = new BufferedReader(new FileReader(bookPath));

        // if there are no lines log error
        String line = br.readLine();
        if (line == null) {
            throw new MseReaderException(bookPath + " has no lines");
        }

        // while there are more lines
        while (line != null) {

            parseSingleLine(line, currentBook);

            // todo check if italics run over a line

            // read the next line to process
            line = br.readLine();
        }

        br.close();

        return currentBook;
    }

    public void parseSingleLine(String line, BibleBook currentBook) throws MseReaderException {
        // if it is a chapter heading
        if (line.indexOf("{") == 0) {

            // todo synopsis

            // start new chapter
            int chapter = getChapterFromLine(line);
            currentBook.createNewChapter(chapter);

        } else {
            // if it is a verse

            // from start of line to first space is verse integer
            int verseNumber = Integer.parseInt(line.substring(0, line.indexOf(" ")));

            // from first space to end is verse text
            String verse = line.substring(line.indexOf(" ")).trim();

            currentBook.getLastChapter().createVerse(verseNumber, line);
        }
    }

    private int getChapterFromLine(String line) {
        return Integer.parseInt(line.substring(1, line.length() - 1));
    }

}
