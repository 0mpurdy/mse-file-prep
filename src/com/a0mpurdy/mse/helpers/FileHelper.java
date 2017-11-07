package com.a0mpurdy.mse.helpers;

import com.a0mpurdy.mse.data.author.Author;
import com.a0mpurdy.mse.data.author.BibleBook;
import com.a0mpurdy.mse.data.author.HymnBook;
import com.a0mpurdy.mse.data.author.IAuthor;

import java.io.File;
import java.io.IOException;

/**
 * @author Michael Purdy
 *         Helps to generate links and folder paths
 */
public abstract class FileHelper {

    /**
     * Get the path to the source folder of an author, relative to the res directory
     * eg target/fer
     *
     * @param author
     * @return
     */
    public static String getSourceFolder(IAuthor author, String fileSeparator) {
        return getSourceFolder() + fileSeparator + author.getFolder();
    }

    /**
     * Get the source folder name
     *
     * @return name of the source folder
     */
    public static String getSourceFolder() {
        return "source";
    }

    /**
     * Get the source path for an author
     * eg: res/source/bible/bible8.txt
     *
     * @param author        author of the source file
     * @param volumeNumber  volume number of the source file
     * @param fileSeparator file separator between folders
     * @return
     */
    public static String getSourceFile(Author author, int volumeNumber, String fileSeparator) {
        return getSourceFolder(author, fileSeparator) + File.separator + getSourceFile(author, volumeNumber);
    }

    /**
     * Get the source filename for an author
     * eg: bible8.txt
     *
     * @param author       author of the source file
     * @param volumeNumber volume number of the source file
     * @return
     */
    public static String getSourceFile(Author author, int volumeNumber) {
        switch (author) {
            case BIBLE:
                return "bible" + volumeNumber + ".txt";
            default:
                return getTextFile(author, volumeNumber);
        }
    }

    /**
     * Get the path to the target folder of an author, relative to the res directory
     * eg target/fer
     *
     * @param author
     * @return
     */
    public static String getTargetFolder(IAuthor author, String fileSeparator) {
        return getTargetFolder() + fileSeparator + author.getFolder();
    }

    /**
     * Get the target folder name
     *
     * @return name of the target folder
     */
    public static String getTargetFolder() {
        return "target";
    }

    /**
     * Get the path to a text file
     * eg res/source/fer4.txt
     *
     * @param author the author of the file
     * @param volNum the volume number of the file
     * @return the path to the text file
     */
    public static String getTextFile(IAuthor author, int volNum, String fileSeparator) {
        return getSourceFolder(author, fileSeparator) + fileSeparator + getTextFile(author, volNum);
    }

    /**
     * Get the name of a text file
     * eg fer4.txt
     *
     * @param author the author of the file
     * @param volNum the volume number of the file
     * @return the name of the text file
     */
    public static String getTextFile(IAuthor author, int volNum) {
        String filename;

        switch (author.getType()) {
            case BIBLE:
                filename = BibleBook.values()[volNum - 1].getSourceFilename();
                break;
            case HYMNS:
                filename = HymnBook.values()[volNum - 1].getSourceFilename();
                break;
            // todo throw NoTunesFileException?
            default:
                filename = author.getSourceName(volNum);

        }
        return filename;
    }

    /**
     * Get the path to an html file, relative to the res directory
     * eg target/fer/fer4.html
     *
     * @param author author of the file
     * @param volNum volume number of the file
     * @return the name of the file
     */
    public static String getHtmlFile(IAuthor author, int volNum, String fileSeparator) {
        return getTargetFolder(author, fileSeparator) + fileSeparator + getHtmlFile(author, volNum);
    }

    /**
     * Get the name of an html file
     * eg fer4.html
     * If the volume number < 0 return the contents file name
     *
     * @param author author of the file
     * @param volNum volume number of the file
     * @return the name of the file
     */
    public static String getHtmlFile(IAuthor author, int volNum) {
        String filename;

        switch (author.getType()) {
            case BIBLE:
                filename = BibleBook.values()[volNum - 1].getTargetFilename();
                break;
            case HYMNS:
                filename = HymnBook.values()[volNum - 1].getTargetFilename();
                break;
            // todo throw NoTunesFileException?
            default:
                filename = author.getTargetName(volNum);

        }
        return filename;
    }

    /**
     * Get the path to the index folder, relative to the res directory
     *
     * @param author
     * @return
     */
    public static String getIndexFile(IAuthor author, String fileSeparator) {
        return getTargetFolder(author, fileSeparator) + fileSeparator + getIndexFile(author);
    }

    /**
     * Get the index file name
     *
     * @param author
     * @return
     */
    public static String getIndexFile(IAuthor author) {
        return "index-" + author.getCode().toLowerCase() + ".idx";
    }

    /**
     * Get the path to an author's contents page, relative to res directory
     *
     * @param author
     * @return
     */
    public static String getContentsFile(IAuthor author, String fileSeparator) {
        return getTargetFolder(author, fileSeparator) + fileSeparator + getContentsFile(author);
    }

    /**
     * Get the name of an author's contents page
     *
     * @param author
     * @return
     */
    public static String getContentsFile(IAuthor author) {
        return author.getCode().toLowerCase() + "-contents.html";
    }

    /**
     * Check if the folder exists, if it doesn't, create it
     *
     * @param path String path to folder that is to be checked
     * @return pathname if it exists or is created
     * @throws IOException
     */
    public static String checkFolder(String path) throws IOException {
        File f = new File(path);
        System.out.print("\rChecking " + f.getCanonicalPath());
        if (f.exists() || f.mkdirs()) {
            return path;
        } else {
            return null;
        }
    }

}
