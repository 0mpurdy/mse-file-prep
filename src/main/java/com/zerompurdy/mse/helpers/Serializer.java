package com.zerompurdy.mse.helpers;

import com.zerompurdy.mse.data.bible.Bible;
import com.zerompurdy.mse.data.ministry.MinistryAuthor;
import com.zerompurdy.mse_core.data.hymn.HymnBook;

import java.io.*;

/**
 * Handles reading and writing of serialized authors and books
 *
 * @author MichaelPurdy
 */
public class Serializer {

    public static void writeBible(String folder, Bible bible) {

        try {
            String outputFileName = folder + File.separator + bible.getSerializedFileName();
            FileOutputStream fileOut = new FileOutputStream(outputFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(bible);
            out.close();
            fileOut.close();
            System.out.printf("\rSaved serialized " + bible.getShortDescription() + " in " + outputFileName);
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public static Bible readBible(String folder, String filename) {
        Bible bible;
        try {
            FileInputStream fileIn = new FileInputStream(folder + File.separator + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            bible = (Bible) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Author class not found");
            c.printStackTrace();
            return null;
        }
        return bible;
    }

    /**
     * Write the author to a serialized file
     *
     * @param folder folder to write the author to
     * @param author author to write
     */
    public static void writeAuthor(String folder, MinistryAuthor author) {

        try {
            String outputFileName = folder + File.separator + author.getSerializedName();
            File outputFile = new File(outputFileName);
            outputFile.getParentFile().mkdirs();
            FileOutputStream fileOut = new FileOutputStream(outputFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(author);
            out.close();
            fileOut.close();
            System.out.printf("\rSaved serialized " + author.getShortDescription() + " in " + outputFileName);
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public static MinistryAuthor readAuthor(String folder, String filename) {
        MinistryAuthor author;
        try {
            FileInputStream fileIn = new FileInputStream(folder + File.separator + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            author = (MinistryAuthor) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Author class not found");
            c.printStackTrace();
            return null;
        }
        return author;
    }

    public static void writeHymnBook(String folder, HymnBook hymnBook) {
        try {
            File f = new File(folder);
            if (f.mkdirs()) {
                System.out.print("\rCreated folder " + folder);
            }
            String outputFileName = folder + File.separator + hymnBook.getSerializedName();
            FileOutputStream fileOut = new FileOutputStream(outputFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(hymnBook);
            out.close();
            fileOut.close();
            System.out.printf("\rSaved serialized " + hymnBook.getShortDescription() + " in " + outputFileName);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static HymnBook readHymnBook(String folder, String filename) {
        HymnBook hymnBook;
        try {
            FileInputStream fileIn = new FileInputStream(folder + File.separator + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            hymnBook = (HymnBook) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("HymnBook class not found");
            c.printStackTrace();
            return null;
        }
        return hymnBook;
    }
}
