package mse.reader;

import mse.common.Author;
import mse.common.Config;
import mse.data.BibleBook;
import mse.data.BiblePrepareCache;
import mse.data.PreparePlatform;
import mse.helpers.FileHelper;
import mse.helpers.HtmlHelper;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Michael Purdy on 14/03/2017.
 */
public class BibleTextReader {

    private Config cfg;
    private mse.reader.Author bible;

    BibleTextReader(Config cfg) {
        this.cfg = cfg;
        this.bible = new mse.reader.Author();
    }

    public void readAll(PreparePlatform platform) {

        System.out.print("\nReading Bible");

        StringBuilder errMessages = new StringBuilder();

        try {

            // get the paths for the files that are used in preparing the bible html
            BiblePrepareCache bpc = new BiblePrepareCache(cfg, platform);

            // get the synopsis pages map
            bpc.synopsisPages = getSynopsisPages(bpc.getSynopsisSource());

            // for each book in the bible
            for (BibleBook nextBook : BibleBook.values()) {

                System.out.print("\rPreparing " + nextBook.getNameWithSpaces());
                bpc.nextBook(nextBook);

                readSingleBook(cfg, bpc, platform.getStylesLink(), errMessages);

            }
        } catch (Exception e) {
            System.out.println("Error reading Bible");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (!errMessages.toString().equals("")) {
            System.out.println("\r" + errMessages);
        }

        System.out.println("\rFinished Preparing Bible: " + FileHelper.getTargetPath(Author.BIBLE, platform));

    }

    public void readSingleBook(Config cfg, BiblePrepareCache bpc, String mseStyleLocation, StringBuilder errMessages) throws IOException {
        // create buffered readers to read the jnd and kjv txt files
        BufferedReader brJND = new BufferedReader(new FileReader(bpc.getJndSource()));
        BufferedReader brKJV = new BufferedReader(new FileReader(bpc.getKjvSource()));

        // create print writers to write the bible html and txt (overwrite any existing files)
        PrintWriter pwBible = new PrintWriter(new FileWriter(bpc.getBibleOutput(), false));
        PrintWriter pwBibleTxt = new PrintWriter(new FileWriter(bpc.getBibleTextOutput()));

        // write the html header
        HtmlHelper.writeHtmlHeader(pwBible, "Darby translation and King James Version of The Bible", mseStyleLocation);
        HtmlHelper.writeBibleStart(pwBible, pwBibleTxt, bpc.book);

        // for each chapter
        for (int chapterCount = 1; chapterCount <= bpc.book.getNumChapters(); chapterCount++) {
            pwBible.println("\t<a href=\"#" + chapterCount + "\">" + chapterCount + "</a> ");
//            System.out.print(String.format("\rPreparing %s chapter %d", bpc.book.getName(), chapterCount));
        }
        pwBible.println("\t<table class=\"bible\">");

        // read the first line of the two versions and assume there are more lines
        bpc.jndLine = brJND.readLine();
        bpc.kjvLine = brKJV.readLine();

        // if there are no lines log error
        if (bpc.jndLine == null) {
            System.out.println("\n!!! No JND lines found");
        }
        if (bpc.kjvLine == null) {
            System.out.println("\n!!! No KJV lines found");
        }

        // while there are more lines
        while ((bpc.jndLine != null) && (bpc.kjvLine != null)) {

            readSingleLine(cfg, bpc, errMessages);
            // write the html and text output
            pwBible.println(bpc.bufferString);
            pwBibleTxt.println(bpc.bufferTxt);

            // check if italics run over a line
            if (bpc.startedItalic) {
                errMessages.append("\n\tItalics - ").append(bpc.bufferTxt);
            }

            // read the next line to process
            bpc.jndLine = brJND.readLine();
            bpc.kjvLine = brKJV.readLine();
        }

        // write the end of the html document
        pwBible.println("</table>\n</body>\n\n</html>");

        // close the print writers
        pwBible.close();
        pwBibleTxt.close();

        // close the readers
        brJND.close();
        brKJV.close();
    }

    public Page parseSingleLine(String line, Page current) {

        // if it is a chapter heading
        if (line.indexOf("{") == 0) {

            // todo synopsis

            // start new chapter (page)
            int chapter = getChapter(line);
            current = current.getParentBook().newPage(chapter);

        } else {
            // if it is a verse

            // from start of line to first space is verse integer
            int verseNumber = Integer.parseInt(line.substring(0, line.indexOf(" ")));

            // from first space to end is verse text
            String verse = line.substring(line.indexOf(" ")).trim();

            int nextVerse = current.addParagraph(new Paragraph(line));
        }

        return current;



            // if it is a verse

            // get the verse number and content
            bpc.verseNum = bpc.jndLine.substring(0, bpc.jndLine.indexOf(" "));
            bpc.jndVerse = bpc.jndLine.substring(bpc.jndLine.indexOf(" ")).trim();
            bpc.kjvVerse = bpc.kjvLine.substring(bpc.kjvLine.indexOf(" ")).trim();

            // create the html output
            bpc.bufferString = "\t\t<tr";

            // verse is odd make the class of <td> odd
            if ((Integer.parseInt(bpc.verseNum) % 2) != 0) {
                bpc.bufferString += " class=\"odd\"";
            }

            bpc.bufferString += String.format(">\n\t\t\t<td class=\"verse-num\"><a name=%s:%s>%s</a></td>\n", bpc.chapter, bpc.verseNum, bpc.verseNum);
            bpc.bufferString += String.format("\t\t\t<td>%s</td>\n\t\t\t<td>%s</td>\n\t\t</tr>", bpc.jndVerse, bpc.kjvVerse);

            // create the text output
            bpc.bufferTxt = bpc.verseNum + " " + bpc.jndVerse + " " + bpc.kjvVerse;

//                            // legacy logging of short verses to find paragraph problems
//                            if (jndVerse.length() < 5) {
//                                System.out.println("Short verse: " + bufferString);
//                            }

            // insert italics
            while (bpc.bufferString.contains("*")) {
                if (bpc.startedItalic) {
                    bpc.bufferString = bpc.bufferString.substring(0, bpc.bufferString.indexOf("*")) + "</i>" + bpc.bufferString.substring(bpc.bufferString.indexOf("*") + 1);
                } else {
                    bpc.bufferString = bpc.bufferString.substring(0, bpc.bufferString.indexOf("*")) + "<i>" + bpc.bufferString.substring(bpc.bufferString.indexOf("*") + 1);
                }
                bpc.startedItalic = !bpc.startedItalic;
            }

        }
    }

    private int getChapter(String line) {
        return Integer.parseInt(line.substring(1, line.length() - 1));
    }

    public void createBibleContents(Config cfg, PreparePlatform preparePlatform) {

        System.out.print("Preparing Bible contents...");

        String contentsFilePath = FileHelper.getTargetPath(Author.BIBLE, Author.BIBLE.getContentsName(), preparePlatform);

        File bibleContentsFile = new File(contentsFilePath);

        PrintWriter pw = null;
        try {

            if (bibleContentsFile.exists() || bibleContentsFile.createNewFile()) {

                pw = new PrintWriter(bibleContentsFile);

                HtmlHelper.writeHtmlHeader(pw, "Bible Contents", preparePlatform.getStylesLink());
                pw.println("");
                pw.println("\t<div class=\"container bible-contents-table\">");
                pw.println("\t\t<div class=\"row bible-contents-header\">");
                pw.println("\t\t\t<div class=\"col-xs-6\">Old<br>Testament</div>");
                pw.println("\t\t\t<div class=\"col-xs-6\">New<br>Testament</div>");
                pw.println("\t\t</div>");

                for (int i = 0; i < BibleBook.getNumOldTestamentBooks(); i++) {

                    pw.println("\t\t<div class=\"row bible-contents-row\">\n\t\t\t<div class=\"col-xs-6\"><a href=\"" +
                            preparePlatform.getLinkPrefix(Author.BIBLE) + BibleBook.values()[i].getTargetFilename() + "\">" +
                            BibleBook.values()[i].getNameWithSpaces() + "</a></div>");

                    // if i+1 is less than the number of new testament books
                    if (i < BibleBook.getNumNewTestamentBooks()) {
                        pw.println("\t\t\t<div class=\"col-xs-6\"><a href=\"" + preparePlatform.getLinkPrefix(Author.BIBLE) +
                                BibleBook.values()[i + BibleBook.getNumOldTestamentBooks()].getTargetFilename() + "\">" +
                                BibleBook.values()[i + BibleBook.getNumOldTestamentBooks()].getNameWithSpaces() + "</a></div>");
                    } else {
                        pw.println("\t\t\t<div class=\"col-xs-6\"></div>");
                    }

                    pw.println("\t\t</div>");

                }

                pw.println("\t</div>");

                pw.println("</body>");
                pw.println();
                pw.println("</html>");

            } else {
                System.out.println("Could not find/create: " + contentsFilePath);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + contentsFilePath);
        } finally {
            if (pw != null) pw.close();
        }

        System.out.println("\rFinished preparing Bible contents");

    }

    private HashMap<String, String> getSynopsisPages(String filename) {
        // this gets a map of the corresponding synopsis page in JND's ministry for each book of the bible

        HashMap<String, String> synopsisMap = new HashMap<>();

        // boolean used to check if there are more pages to read
        boolean morePages = true;

        // buffer for the synopsis file input
        String synopsisLine;

        BufferedReader brPages = null;

        try {
            // buffered reader for reading the synopsis pages link file
            brPages = new BufferedReader(new FileReader(filename));

            // populate the synopsis pages hash map
            while (morePages) {//still more lines in pages.txt
                if ((synopsisLine = brPages.readLine()) != null) {
                    // get the synopsis for a bible book
                    // links are stored in the format {bible book}, {bible book chapter}, {jnd volume}, {jnd volume page}

                    String[] synopsisNumbers = synopsisLine.split(",");
                    String key = String.format("%s/%s", synopsisNumbers[0], synopsisNumbers[1]);
                    String value = String.format(" - <a href=\"../jnd/JND%s.html#%s\">go to synopsis</a>", synopsisNumbers[2], synopsisNumbers[3]);
                    synopsisMap.put(key, value);
                } else {
                    morePages = false;
                }
            }
        } catch (IOException ex) {
            System.out.println("!***Error creating synopsis hash map***!");
        } finally {
            if (brPages != null) {
                try {
                    brPages.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return synopsisMap;
    }

}
