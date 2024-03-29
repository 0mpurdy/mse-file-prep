package com.a0mpurdy.mse.processors;

import com.a0mpurdy.mse.olddata.BibleBook;
import com.a0mpurdy.mse.olddata.HymnBook;
import com.a0mpurdy.mse.data.author.Author;
import com.a0mpurdy.mse.data.author.AuthorIndex;
import com.a0mpurdy.mse.common.config.Config;
import com.a0mpurdy.mse.olddata.PreparePlatform;
import com.a0mpurdy.mse.helpers.FileHelper;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by mj_pu_000 on 21/12/2015.
 */
public class Indexer {

    public static void indexAuthor(Config cfg, Author author, ReferenceQueue referenceQueue, ArrayList<String> messages, PreparePlatform platform) {

        AuthorIndex authorIndex = new AuthorIndex(author);

//        // source file path for text to index
//        String sourcePath = author.getIndexPreparePath(platform);
//
//        // destination file path
//        String destinationPath = FileHelper.getTargetPath(author, platform);

        int volumeNumber = 1;

        File inputVolume = getFile(platform, author, volumeNumber);

        // for all the volumes
        while (inputVolume != null) {
            indexVolume(authorIndex, inputVolume, volumeNumber, referenceQueue, messages);
            volumeNumber++;
            inputVolume = getFile(platform, author, volumeNumber);
        }

    } // indexAuthor

    public static void indexVolume(AuthorIndex authorIndex, File inputVolume, int volumeNumber, ReferenceQueue referenceQueue, ArrayList<String> messages) {

        Author author = authorIndex.getAuthor();

        System.out.print("\rAnalysing " + getReadableReference(author, volumeNumber, 0, 0));

        int pageNumber = 0;

        try {

            // line read
            String line;
            boolean noErrors = true;

            // create reader
            FileReader inputReader = new FileReader(inputVolume);
            BufferedReader brLog = new BufferedReader(inputReader);

            // while there are more lines
            while ((line = brLog.readLine()) != null) {

                StringBuilder outputLine = new StringBuilder(line);
                int charPos = 0;
                boolean skip = false;

                if (outputLine.length() > 0) {
                    if (outputLine.charAt(charPos) == '{') {
                        // if it is a page number or a title

                        charPos++;
                        StringBuilder specialLine = new StringBuilder();

                        // find the end of the page number
                        while (outputLine.charAt(charPos) != '}') {
                            specialLine.append(outputLine.charAt(charPos));
                            charPos++;
                        }

                        if (specialLine.charAt(0) != '#') {
                            // page number

                            // debug
//                            if (author.equals(Author.HYMNS)) System.out.println(outputLine);

                            pageNumber = Integer.parseInt(specialLine.toString());
                            skip = true;
                        } else {
                            outputLine = new StringBuilder(specialLine.substring(1));
                            skip = true;
                        }

                    }

                    // if the line contains html - remove the html tag
                    while (outputLine.indexOf("<") != -1) {
                        charPos = 0;
                        while ((charPos < outputLine.length()) && (outputLine.charAt(charPos) != '<')) {
                            charPos++;
                        }
                        int tempCharPos = charPos + 1;
                        while ((tempCharPos < outputLine.length()) && (outputLine.charAt(tempCharPos) != '>')) {
                            tempCharPos++;
                        }
                        if ((charPos < outputLine.length()) && (tempCharPos <= outputLine.length()))
                            outputLine.replace(charPos, tempCharPos + 1, "");
                    }

                    if (!skip) {
                        // split the line into tokens (words) by " " characters
//                        String[] tokens = outputLine.toString().split(" ");
                        String[] tokens = outputLine.toString().split("[\\W]");

                        // make each token into a word that can be searched
                        for (String token : tokens) {

                            token = token.toUpperCase();
                            if (!isAlpha(token)) {
                                token = processString(token);
                            }
                            if (!isAlpha(token)) {
                                noErrors = false;
                                messages.add("\t" + token + "\t" + volumeNumber + ":" + pageNumber);
                                token = "";
                            }

                            if (!token.equals("")) {
                                // if the string isn't empty
                                referenceQueue.add(new ReferenceQueueItem(author, token, (short) volumeNumber, (short) pageNumber));
//                                authorIndex.incrementTokenCount(token, volumeNumber, pageNumber);
                            }
                        } // end for each token
                    } // end if (!skip)
                }// end if outputline is !empty

            } // end for the whole file

        } catch (FileNotFoundException fnf) {
            System.out.println("Error - could not find file " + inputVolume.getAbsolutePath());
            System.out.println(fnf.getMessage());
        } catch (IOException ioe) {
            System.out.println("Error reading " + inputVolume.getAbsolutePath());
            System.out.println(ioe.getMessage());
        }

    }

    /**
     * Get the next file to index for the author
     *
     * @param author       The author being indexed
     * @param volumeNumber The volume number being indexed
     * @return The file to be indexed
     */
    private static File getFile(PreparePlatform platform, Author author, int volumeNumber) {
        String filename;
        try {
            filename = platform.getResDir() + File.separator + FileHelper.getSourceFile(author, volumeNumber, File.separator);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        File file = new File(filename);
        if (file.exists()) return file;
        else return null;
    }

    private static String getReadableReference(Author author, int volNum, int pageNum, int verseNum) {
        if (author.isMinistry()) {
            return author.getCode() + " volume " + volNum + " page " + pageNum;
        } else if (author.equals(Author.BIBLE)) {
            return BibleBook.values()[volNum - 1].getNameWithSpaces() + " chapter " + pageNum + ":" + verseNum;
        } else if (author.equals(Author.HYMNS)) {
            return HymnBook.values()[volNum - 1].getName() + " " + pageNum;
        }

        return "";
    }

    private static boolean isAlpha(String token) {
        char[] chars = token.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private static String processString(String token) {
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) {
                token = token.replace(Character.toString(c), "");
            }
        }

        return token;
    }

}
