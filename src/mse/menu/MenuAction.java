package mse.menu;

import mse.common.log.ILogger;
import mse.data.author.Author;
import mse.data.author.AuthorIndex;
import mse.common.config.Config;
import mse.olddata.PreparePlatform;
import mse.helpers.Serializer;
import mse.processors.*;
import mse.data.bible.Bible;
import mse.reader.bible.BibleTextReader;
import mse.data.hymn.HymnBook;
import mse.reader.hymn.HymnTextReader;
import mse.data.ministry.MinistryAuthor;
import mse.reader.ministry.MinistryTextReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Michael Purdy on 27/10/2016.
 */
public class MenuAction {

    public static void executeMenuChoice(int mainMenuChoice, Scanner sc, Config cfg) {
        switch (mainMenuChoice) {
            case 0:
                System.out.println("Closing ...");
                break;
            case 1:
                prepareAllFiles(sc, cfg);
                break;
            case 2:
                createAllIndexes(sc, cfg);
                break;
            case 3:
                createAllSerialFiles(sc);
                System.out.println("\rSerialized all files");
                break;
            case 4:
                System.out.println("Test");
                PreparePlatform platform = chooseSystem(sc);
                MinistryTextReader mtr = new MinistryTextReader();
                ArrayList<MinistryAuthor> authors = mtr.readAllMinistryAuthors(platform.getSourcePath());
                System.out.println("\rWriting authors");
                for (MinistryAuthor author : authors) {
                    Serializer.writeAuthor(platform.getSerialFolder(), author);
                }
//                Serializer.readAuthor(platform.getSerialFolder(), )
                break;
            case 5:
                MenuPrinter.printMenu(MenuPrinter.otherMainMenuOptions);
                doOtherMenuOption(cfg, sc.nextInt(), sc);
                break;
            case 6:
                MenuPrinter.printMenu(MenuPrinter.debugMenuOptions);
                doDebugMenuOption(cfg, sc.nextInt(), sc);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private static void createAllSerialFiles(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);

        serializeAllHymns(platform);
        serializeAllBibles(platform);
    }

    private static void serializeAllHymns(PreparePlatform platform) {
        HymnTextReader htr = new HymnTextReader();
        ArrayList<HymnBook> hymnBooks = htr.readAll(platform.getSourcePath() + File.separator + "hymns");
        for (HymnBook hymnBook : hymnBooks) {
            Serializer.writeHymnBook(platform.getSerialFolder() + File.separator + "hymns", hymnBook);
        }
    }

    private static void serializeAllBibles(PreparePlatform platform) {
        BibleTextReader btr = new BibleTextReader();
        ArrayList<Bible> bibles = btr.readAll(platform.getSourcePath());
        for (Bible bible : bibles) {
            Serializer.writeBible(platform.getSerialFolder(), bible);
        }
    }

    private static void readHymnText(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        HymnTextReader hymnTextReader = new HymnTextReader();
        HymnBook hymnBook = hymnTextReader.readHymnBook(platform.getSourcePath() + File.separator + "hymns", "hymns1962");
        Serializer.writeHymnBook(platform.getSerialFolder() + File.separator + "hymns", hymnBook);
        System.out.println("Read hymns text");
    }

    private static void readSerialHymn(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        HymnBook hymnBook = Serializer.readHymnBook(platform.getSerialFolder() + File.separator + "hymns", "hymns1962.ser");
        System.out.println("Read hymns text");
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

    private static void readSingleSerialMinistryAuthor(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        MinistryAuthor fromFile = Serializer.readAuthor(platform.getSerialFolder(), "misc.ser");
    }

    private static void readJndBibleText(Scanner sc) {
        PreparePlatform platform = chooseSystem(sc);
        BibleTextReader bibleTextReader = new BibleTextReader();
        Bible jndBible = bibleTextReader.readVersion("JND Bible", "JND", "jnd", "bible", platform.getSourcePath() + File.separator + "bible");
        Serializer.writeBible(platform.getSerialFolder(), jndBible);
        System.out.println("Created JND");
    }

    private static void prepareAllFiles(Scanner sc, Config cfg) {
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

    private static void prepareSingleAuthor(Scanner sc, Config cfg) {
        System.out.println("\nWhich author do you wish to prepare?");
        MenuPrinter.printAuthorMenu();
        int authorChoice = sc.nextInt();
        sc.nextLine();

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

    private static void createAllIndexes(Scanner sc, Config cfg) {
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

    private static void createSingleAuthorIndex(Scanner sc, Config cfg) {
        System.out.println("\nWhich author do you wish to index?");
        MenuPrinter.printAuthorMenu();
        int authorChoice = sc.nextInt();
        sc.nextLine();

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

    private static void checkAuthorIndex(Scanner sc) {
        System.out.println("\nWhich author index do you wish to check?");
        MenuPrinter.printAuthorMenu();
        int authorChoice = sc.nextInt();
        sc.nextLine();
        if ((authorChoice >= 0) && (authorChoice < Author.values().length)) {


            Author author = Author.values()[authorChoice];
            if (author.isSearchable()) {

                // choose system to check index for
                PreparePlatform platform = chooseSystem(sc);
                if (platform != null) {
                    AuthorIndex authorIndex = new AuthorIndex(author);
                    authorIndex.loadIndex(platform.getResDir());
                    System.out.println(authorIndex.getTokenCountMap().size());

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

            } else {
                System.out.println("This author is not searchable");
            }
        } else {
            System.out.println("This is not a valid option");
        }
    }

    private static void checkAllIndexes(Scanner sc) {

        // choose system to check index for
        PreparePlatform platform = chooseSystem(sc);
        if (platform != null) {
            ArrayList<AuthorIndex> authorIndexes = new ArrayList<>();
            for (Author nextAuthor : Author.values()) {
                if (nextAuthor.isSearchable()) {
                    AuthorIndex authorIndex = new AuthorIndex(nextAuthor);
                    authorIndex.loadIndex(platform.getResDir());
                    authorIndexes.add(authorIndex);
                }
            }
        }
    }

    private static void doOtherMenuOption(Config cfg, int option, Scanner sc) {
        switch (option) {
            case 0:
                return;
            case 1:
                prepareSingleAuthor(sc, cfg);
                break;
            case 2:
                createSingleAuthorIndex(sc, cfg);
                break;
            case 3:
                System.out.println("Not currently working");
//                createSuperIndex(sc, cfg);
                break;
            case 4:
                checkAuthorIndex(sc);
                break;
            case 5:
                checkAllIndexes(sc);
                break;
            case 6:
                serializeAllBibles(chooseSystem(sc));
                break;
            case 7:
                serializeAllHymns(chooseSystem(sc));
                System.out.print("\rFinished reading all hymns\n");
                break;
            case 8:
                // benchmark
                System.out.println("Benchmarking ...\n\n");
                new Benchmark().run();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private static void doDebugMenuOption(Config cfg, int option, Scanner sc) {
        switch (option) {
            case 0:
                return;
            case 1:
                // Read single author text
                readSingleSerialMinistryAuthor(sc);
                break;
            case 2:
                // benchmark
                System.out.println("Benchmarking ...\n\n");
                new Benchmark().run();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private static PreparePlatform chooseSystem(Scanner sc) {

        System.out.println("\nChoose a system:");

        ArrayList<String> systems = new ArrayList<>();
        systems.add("Cancel");
        for (PreparePlatform platform : PreparePlatform.values()) {
            systems.add(platform.getName());
        }

        MenuPrinter.printMenu(systems);
        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 0:
                return null;
            default:
                return PreparePlatform.values()[option - 1];
        }
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
