package com.zerompurdy.mse.menu;

import com.zerompurdy.mse.common.config.Config;
import com.zerompurdy.mse.data.author.Author;
import com.zerompurdy.mse.data.author.AuthorIndex;
import com.zerompurdy.mse_core.data.bible.Bible;
import com.zerompurdy.mse.data.ministry.MinistryAuthor;
import com.zerompurdy.mse.helpers.Serializer;
import com.zerompurdy.mse.parser.HymnParser;
import com.zerompurdy.mse.olddata.PreparePlatform;
import com.zerompurdy.mse.processors.*;
import com.zerompurdy.mse.parser.BibleParser;
import com.zerompurdy.mse.reader.ministry.MinistryTextReader;
import com.zerompurdy.mse_core.data.hymn.HymnBook;
import com.zerompurdy.mse_core.log.ILogger;

import java.io.*;
import java.util.*;

/**
 * Created by Michael Purdy on 27/10/2016.
 */
public class MenuAction {

    /**
     * Create the serialised version of all supported files
     * @param sc
     * @return total nanoseconds to perform the serialisation.
     */
    protected static long createAllSerialFiles(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);

        long start = System.nanoTime();

        serializeAllHymns(platform);
        serializeAllBibles(platform);

        return System.nanoTime() - start;
    }

    protected static void serializeAllHymns(PreparePlatform platform) {
        HymnParser htr = new HymnParser();
        List<HymnBook> hymnBooks = htr.readAll(platform.getSourcePath() + File.separator + "hymns");
        for (HymnBook hymnBook : hymnBooks) {
            Serializer.writeHymnBook(platform.getSerialFolder() + File.separator + "hymns", hymnBook);
        }
    }

    protected static void serializeAllBibles(PreparePlatform platform) {
        BibleParser btr = new BibleParser();
        ArrayList<Bible> bibles = btr.readAll(platform.getSourcePath());
        for (Bible bible : bibles) {
            Serializer.writeBible(platform.getSerialFolder(), bible);
        }
    }

    private static void readHymnText(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        HymnParser hymnParser = new HymnParser();
        HymnBook hymnBook = hymnParser.readHymnBook(platform.getSourcePath() + File.separator + "hymns", "hymns1962");
        Serializer.writeHymnBook(platform.getSerialFolder() + File.separator + "hymns", hymnBook);
        System.out.println("Read hymns text");
    }

    protected static HymnBook readSerialHymn(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        return Serializer.readHymnBook(platform.getSerialFolder() + File.separator + "hymns", "hymns1962.ser");
    }

    private static void readSerialJndBible(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        Bible jndVersion = Serializer.readBible(platform.getSerialFolder(), "JND-Bible.ser");
        System.out.println("Read JND");
    }

    private static void serializeAndReadMiscMinistry(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        MinistryTextReader mtr = new MinistryTextReader();
        MinistryAuthor miscMinistry = mtr.readMinistryAuthor(platform.getSourcePath() + File.separator + "misc", "Misc", "misc");
        Serializer.writeAuthor(platform.getSerialFolder(), miscMinistry);
        MinistryAuthor fromFile = Serializer.readAuthor(platform.getSerialFolder(), "misc.ser");
        System.out.println("Read Misc Ministry");
    }

    protected static void readSingleSerialMinistryAuthor(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        MinistryAuthor fromFile = Serializer.readAuthor(platform.getSerialFolder(), "misc.ser");
    }

    private static void readJndBibleText(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        BibleParser bibleParser = new BibleParser();
        Bible jndBible = bibleParser.readVersion("JND Bible", "JND", "jnd", "bible", platform.getSourcePath() + File.separator + "bible");
        Serializer.writeBible(platform.getSerialFolder(), jndBible);
        System.out.println("Created JND");
    }

    protected static void prepareAllFiles(Scanner sc, Config cfg) {
        PreparePlatform platform = chooseSystem(sc);
        if (platform != null) {

            System.out.println("Preparing all files");

            // prepare bible
            Preparer.prepareBibleHtml(cfg, platform);
            Preparer.createBibleContents(cfg, platform);

            // prepare hymns
            Preparer.prepareHymnsHtml(cfg, platform);
            Preparer.createHymnsContents(cfg, platform);

            // prepare ministry
            for (Author nextAuthor : Author.values()) {
                if (nextAuthor.isMinistry()) {
                    Preparer.prepareMinistry(cfg, nextAuthor, platform);
                }
            }
        }
    }

    protected static void prepareSingleAuthor(Scanner sc, Config cfg) {
        System.out.println("\nWhich author do you wish to prepare?");
        int authorChoice = AuthorMenuFactory.make().getChoice(sc);

        PreparePlatform platform = chooseSystem(sc);
        if (platform != null) {
            if (authorChoice == 0) {
                Preparer.prepareBibleHtml(cfg, platform);
                Preparer.createBibleContents(cfg, platform);
            } else if (authorChoice == 1) {
                Preparer.prepareHymnsHtml(cfg, platform);
                Preparer.createHymnsContents(cfg, platform);
            } else if ((authorChoice >= 3) && (authorChoice <= 12)) {
                Preparer.prepareMinistry(cfg, Author.values()[authorChoice], platform);
            } else {
                System.out.println("\nOption " + authorChoice + " is not available at the moment");
            }
        }
    }

    protected static void createAllIndexes(Scanner sc, Config cfg) {
        PreparePlatform platform = chooseSystem(sc);
        if (platform != null) {
            System.out.println("Creating all indexes ...");
            long startIndexing = System.nanoTime();
            // add a reference processor for each author then write the index
            for (Author nextAuthor : Author.values()) {
                if (nextAuthor.isSearchable()) {
                    processAuthor(nextAuthor, cfg, platform);
                }
            }
            long endIndexing = System.nanoTime();
            System.out.println("Total Index Time: " + ((endIndexing - startIndexing) / 1000000) + "ms");
        } // end creating all indexes
    }

    protected static void createSingleAuthorIndex(Scanner sc, Config cfg) {
        System.out.println("\nWhich author do you wish to index?");
        int authorChoice = AuthorMenuFactory.make().getChoice(sc);

        PreparePlatform platform = chooseSystem(sc);
        if (platform != null) {
            if ((authorChoice >= 0) && (authorChoice < Author.values().length)) {
                Author author = Author.values()[authorChoice];
                processAuthor(author, cfg, platform);
            } else {
                System.out.println("This is not a valid option");
            }
        }
    }

//    private static void createSuperIndex(Scanner sc, Config cfg) {
//        System.out.println("Creating super index");
//        // choose system to create super index for
//        PreparePlatform platform = chooseSystem(sc);
//        if (platform != null) {
//            createSuperIndex(cfg, platform);
//        }
//    }

    protected static void checkAuthorIndex(Scanner sc) {
        System.out.println("\nWhich author index do you wish to check?");
        int authorChoice = AuthorMenuFactory.make().getChoice(sc);
        if ((authorChoice >= 0) && (authorChoice < Author.values().length)) {


            Author author = Author.values()[authorChoice];
            if (author.isSearchable()) {

                // choose system to check index for
                PreparePlatform platform = chooseSystem(sc);
                if (platform != null) {
                    AuthorIndex authorIndex = new AuthorIndex(author);
                    authorIndex.loadIndex(platform.getResDir());
                    checkSingleIndex(authorIndex, sc);
                }

            } else {
                System.out.println("This author is not searchable");
            }
        } else {
            System.out.println("This is not a valid option");
        }
    }

    private static void checkSingleIndex(AuthorIndex authorIndex, Scanner sc) {
        System.out.println("Author: " + authorIndex.getAuthorName());
        System.out.println("Token map size: " + authorIndex.getTokenCountMap().size());
        System.out.println("Key 1: " + authorIndex.getTokenCountMap().keySet().iterator().next());

        System.out.print("Do you wish to write the index to a file (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("index.txt"));
                for (Map.Entry<String, short[]> entry : authorIndex.getReferencesMap().entrySet()) {
                    bw.write("\"" + entry.getKey() + "\": [");
                    for (short ref : entry.getValue()) {
                        bw.write(ref + ", ");
                    }
                    bw.write("]\n");
                }
            } catch (IOException ioe) {
                System.out.println("Error writing index");
            }
        }
    }

    protected static void checkAllIndexes(Scanner sc) {
        // choose system to check index for
        PreparePlatform platform = chooseSystem(sc);
        if (platform != null) {
            ArrayList<AuthorIndex> authorIndexes = new ArrayList<>();
            for (Author nextAuthor : Author.values()) {
                if (nextAuthor.isSearchable()) {
                    AuthorIndex authorIndex = new AuthorIndex(nextAuthor);
                    authorIndex.loadIndex(platform.getResDir());
                    authorIndexes.add(authorIndex);
                    checkSingleIndex(authorIndex, sc);
                }
            }
        }
    }

    protected static PreparePlatform chooseSystem(Scanner sc) {
        System.out.println("\nChoose a system:");
        int option = SystemsMenuFactory.make().getChoice(sc);

        if (option == 0) return null;

        return PreparePlatform.values()[option - 1];
    }

    private static void processAuthor(Author author, Config cfg, PreparePlatform platform) {

        ArrayList<String> messages = new ArrayList<>();

        long startAuthor = System.nanoTime();

        final ReferenceQueue referenceQueue = new ReferenceQueue(author, cfg);
        ReferenceProcessor referenceProcessor = new ReferenceProcessor(referenceQueue, platform);
        referenceProcessor.start();
        Indexer.indexAuthor(cfg, author, referenceQueue, messages, platform);
        System.out.println();
        referenceProcessor.interrupt();
        while (referenceProcessor.isAlive()) {
            try {
                referenceProcessor.join(500);
                System.out.print("\rWords left: " + referenceQueue.size());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        long endAuthor = System.nanoTime();

        System.out.println("\nAuthor Time: " + ((endAuthor - startAuthor) / 1000000) + "ms");

        for (String message : messages) {
            System.out.println(message);
        }

    }

    private static void createSuperIndex(Config cfg, PreparePlatform platform, ILogger logger) {

        // super index token count
        HashMap<String, Integer> superIndex = new HashMap<>();

        // Read each index and create the super index
        for (Author nextAuthor : Author.values()) {
            if (nextAuthor.isSearchable()) {

                System.out.println("Creating super index for " + nextAuthor.getName());

                AuthorIndex nextAuthorIndex = new AuthorIndex(nextAuthor);
                nextAuthorIndex.loadIndex(platform.getResDir());

                // if the index loads
                if (nextAuthorIndex != null) {
                    // for each word in the author index
                    HashMap<String, Integer> authorTokenCount = nextAuthorIndex.getTokenCountMap();
                    for (String token : authorTokenCount.keySet()) {

                        // key: author code + token
                        // value: token count for author
                        superIndex.put(nextAuthor.getCode() + "-" + token, authorTokenCount.get(token));
                    }
                } else {
                    System.out.println("Author index cannot be null");
                }
            }

        }

        try {

            // output super index
            FileOutputStream fileOutStream = new FileOutputStream(platform.getResDir() + "super.idx");
            ObjectOutputStream objectOutStream = new ObjectOutputStream(fileOutStream);
            objectOutStream.writeObject(superIndex);
            objectOutStream.flush();
            objectOutStream.close();

        } catch (IOException ioe) {
            System.out.println("Error writing super index file");
            System.out.println(ioe.getMessage());
        }
    }

//    private static AuthorIndex readIndex(String filename) {
//
//        AuthorIndex result;
//        ObjectInputStream objectInputStream = null;
//
//        try {
//            File indexFile = new File(filename);
//            InputStream inStream = new FileInputStream(indexFile);
//            BufferedInputStream bInStream = new BufferedInputStream(inStream);
//            objectInputStream = new ObjectInputStream(bInStream);
//
//            result =  (AuthorIndex) objectInputStream.readObject();
//
//        } catch (IOException ioe) {
//            System.out.println("Error reading file: " + filename);
//            System.out.println(ioe.getMessage());
//            result = null;
//        } catch (ClassNotFoundException cnfe) {
//            System.out.println("Invalid class in file: " + filename);
//            result = null;
//        } finally {
//            if (objectInputStream != null) {
//                try {
//                    objectInputStream.close();
//                } catch (IOException ioe) {
//                    System.out.println("Error closing: " + filename);
//                }
//            }
//        }
//        return result;
//    }

    private static HashMap<String, Integer> readSuperIndex(String filename) {

        HashMap<String, Integer> result;
        ObjectInputStream objectInputStream = null;

        try {
            File indexFile = new File(filename);
            InputStream inStream = new FileInputStream(indexFile);
            BufferedInputStream bInStream = new BufferedInputStream(inStream);
            objectInputStream = new ObjectInputStream(bInStream);

            try {
                result = (HashMap<String, Integer>) objectInputStream.readObject();
            } catch (ClassCastException cce) {
                result = null;
            }
        } catch (IOException ioe) {
            System.out.println("Error reading file: " + filename);
            System.out.println(ioe.getMessage());
            result = null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Invalid class in file: " + filename);
            result = null;
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException ioe) {
                    System.out.println("Error closing: " + filename);
                }
            }
        }
        return result;
    }

}
