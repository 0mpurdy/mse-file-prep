package com.a0mpurdy.mse.reader.ministry;

import com.a0mpurdy.mse.data.ministry.MinistryAuthor;
import com.a0mpurdy.mse.data.ministry.MinistryBook;
import com.a0mpurdy.mse.data.ministry.MinistryPage;
import com.a0mpurdy.mse.data.ministry.MinistryParagraph;
import com.a0mpurdy.mse.reader.MseReaderException;

import java.io.*;
import java.util.ArrayList;

/**
 * Reads ministry into memory
 */
public class MinistryTextReader {

    public ArrayList<MinistryAuthor> readAllMinistryAuthors(String sourceFolder, ArrayList<AuthorDetails> authorDetails) {

        ArrayList<MinistryAuthor> authors = new ArrayList<>();

        for (AuthorDetails details : authorDetails) {
            authors.add(readMinistryAuthor(
                    sourceFolder + File.separator + details.getFolderName(),
                    details.getName(),
                    details.getFilePrefix()));
        }

        return authors;
    }

    public MinistryAuthor readMinistryAuthor(String folder, String name, String filePrefix) {

        System.out.print("\rReading " + name + " text.");

        MinistryAuthor author = new MinistryAuthor(filePrefix, name, filePrefix);

        try {

            // set up reader
            File f;
            f = new File(folder);
            f.mkdirs();
            System.out.print("\r\tReading from " + f.getCanonicalPath());

            int volumeNumber = 1;
            f = new File(folder + File.separator + filePrefix + volumeNumber + ".txt");

            // for each volume
            while (f.exists()) {
                try {
                    createMinistryBook(author, f, volumeNumber);
                } catch (MseReaderException e) {
                    System.out.println("\r" + e.getMessage());
                }
                volumeNumber++;
                f = new File(folder + File.separator + filePrefix + volumeNumber + ".txt");
            }
        } catch (IOException ioe) {
            System.out.println("\n!*** Error preparing " + author.getName() + " ***!");
            System.out.println(ioe.getMessage());
        }

        return author;
    }

    private MinistryBook createMinistryBook(MinistryAuthor author, File textFile, int volumeNumber) throws IOException, MseReaderException {

        MinistryBook currentVolume;

        // print progress
        System.out.print("\rPreparing " + author.getShortDescription() + " Volume: " + volumeNumber);

        // get source file
        BufferedReader br = new BufferedReader(new FileReader(textFile));

        try {

            MinistryPage currentPage = parseStart(author, br, volumeNumber);
            currentVolume = currentPage.getParentBook();
            String line = br.readLine();

            while (line != null) {

                currentPage = parseMinistryLine(line, currentPage);

                line = br.readLine();
            }
        } catch (IOException e) {
            br.close();
            throw new MseReaderException(e.getMessage());
        }

        return currentVolume;
    }

    private MinistryPage parseStart(MinistryAuthor author, BufferedReader br, int volumeNumber) throws IOException, MseReaderException {

        // read the volume title
        String line = br.readLine();
        String title;
        try {
            title = getTitleFromLine(line);
        } catch (MseReaderException e) {
            throw new MseReaderException(author.getShortDescription() + e.getMessage());
        }
        MinistryBook book = author.createNewVolume(title, volumeNumber);

        // check if first line is a page number
        line = br.readLine();
        if (isPageNumber(line)) {
            int pageNumber = getNextPageNumber(line);
            return book.createNewPage(pageNumber);
        } else {
            return parsePreface(line, br, book);
        }
    }

    private MinistryPage parsePreface(String line, BufferedReader br, MinistryBook currentVolume) throws MseReaderException, IOException {
        MinistryPage currentPage = parseMinistryLine(line, currentVolume.getPreface());
        while (currentPage == currentVolume.getPreface()) {
            line = br.readLine();
            currentPage = parseMinistryLine(line, currentVolume.getPreface());
        }
        return currentPage;
    }

    private MinistryPage parseMinistryLine(String line, MinistryPage ministryPage) throws MseReaderException {

        if (line.length() > 0) {

            // heading or a special line
            if (line.charAt(0) == '{') {
                try {
                    return parseSpecialLine(line, ministryPage);
                } catch (NumberFormatException e) {
                    throw new MseReaderException(ministryPage.getShortDescription() + " - could not parse special line " + e.getMessage());
                }
            } else if (line.length() < 400 &&
                    (line.equals(line.toUpperCase()) && (line.charAt(0) != ' '))) {
                // if it is a title
                ministryPage.createNewTitleParagraph(line);
                return ministryPage;
            }

            // todo missing footnotes

            MinistryParagraph currentParagraph = ministryPage.createNewParagraph();
            return parseGenericMinistryLine(line, currentParagraph);

        } else {
            System.out.println("\r" + ministryPage.getShortDescription() + " empty line");
            return ministryPage;
        }

    }

    private MinistryPage parseSpecialLine(String line, MinistryPage ministryPage) throws MseReaderException {
        // page number - format {73}
        int pageNumber = Integer.parseInt(line.substring(line.indexOf('{') + 1, line.lastIndexOf('}')));
        return ministryPage.getParentBook().createNewPage(pageNumber);
    }

    private static int getNextPageNumber(String line) {
        // remove brackets
        line = line.substring(1, line.length() - 1);

        int pageNum;
        try {
            pageNum = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            pageNum = -1;
            e.printStackTrace();
        }

        return pageNum;
    }

    private static boolean isPageNumber(String line) {
        if (line == null) return false;
        if (line.length() < 3) return false;
        if (line.charAt(0) != '{') return false;
        if (line.charAt(line.length() - 1) != '}') return false;

        // check all characters between {} are digits
        for (int i = 1; i < line.length() - 1; i++) {
            if (!(Character.isDigit(line.charAt(i)))) return false;
        }

        return true;
    }

    private static String getTitleFromLine(String line) throws MseReaderException {
        if (line.length() > 3 && line.charAt(0) == '{' || line.charAt(1) == '#' && line.charAt(line.length() - 1) == '}') {
            return line.substring(2, line.lastIndexOf('}'));
        }
        throw new MseReaderException("Could not get title from line: " + line);
    }

    private MinistryPage parseGenericMinistryLine(String line, MinistryParagraph currentParagraph) {

        ArrayList<String> sentences = splitSentences(line);

        for (String sentence : sentences) {
            currentParagraph.addSentence(sentence.trim());
        }

        return currentParagraph.getParentPage();
    }

    private ArrayList<String> splitSentences(String line) {
        ArrayList<String> sentences = new ArrayList<>();
        int i = 0;
        int endOfPreviousSentence = -1;
        while (i < line.length()) {
            if (line.charAt(i) == '.' || line.charAt(i) == '!' || line.charAt(i) == '?') {
                sentences.add(line.substring(endOfPreviousSentence + 1, i + 1));
                endOfPreviousSentence = i;
            }
            i++;
        }
        if (endOfPreviousSentence < line.length() - 1) {
            sentences.add(line.substring(endOfPreviousSentence + 1, line.length()));
        }
        return sentences;
    }
}
