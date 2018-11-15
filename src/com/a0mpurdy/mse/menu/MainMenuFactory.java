package com.a0mpurdy.mse.menu;

import com.a0mpurdy.mse.common.config.Config;
import com.a0mpurdy.mse.data.ministry.MinistryAuthor;
import com.a0mpurdy.mse.helpers.Serializer;
import com.a0mpurdy.mse.olddata.PreparePlatform;
import com.a0mpurdy.mse.reader.ministry.AuthorDetails;
import com.a0mpurdy.mse.reader.ministry.MinistryTextReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.a0mpurdy.mse.menu.MenuAction.chooseSystem;

public class MainMenuFactory {

    public static Menu make(Scanner sc, Config cfg) {
        List<MenuOption> options = new ArrayList<MenuOption>();

        options.add(new MenuOption("Exit", () -> {
            System.out.println("Closing...");
        }));

        options.add(new MenuOption("Prepare all files", () -> {
            MenuAction.prepareAllFiles(sc, cfg);
        }));

        options.add(new MenuOption("Create all indexes", () -> {
            MenuAction.createAllIndexes(sc, cfg);
        }));

        options.add(new MenuOption("Create all serial files", () -> {
            MenuAction.createAllSerialFiles(sc);
            System.out.println("\rSerialized all files");
        }));

        options.add(new MenuOption("Test", () -> {
            System.out.println("Test");
            PreparePlatform platform = chooseSystem(sc);
            MinistryTextReader mtr = new MinistryTextReader();

            ArrayList<AuthorDetails> authorDetails = new ArrayList<>();
            authorDetails.add(new AuthorDetails("Miscellaneous", "misc", "misc"));
            authorDetails.add(new AuthorDetails("J.N. Darby", "jnd", "jnd"));
            authorDetails.add(new AuthorDetails("J. Taylor", "jt", "jt"));
            authorDetails.add(new AuthorDetails("W.J. House", "wjh", "wjh"));

            ArrayList<MinistryAuthor> authors = mtr.readAllMinistryAuthors(platform.getSourcePath(), authorDetails);
            System.out.println("\rWriting authors");
            for (MinistryAuthor author : authors) {
                Serializer.writeAuthor(platform.getSerialFolder(), author);
            }
//                Serializer.readAuthor(platform.getSerialFolder(), )
        }));

        options.add(new MenuOption("Other", () -> {
            OtherMenuFactory
                    .make(sc, cfg)
                    .run(sc);
        }));

        options.add(new MenuOption("Debug", () -> {
            DebugMenuFactory
                    .make(sc)
                    .run(sc);
        }));

        return new Menu(options);
    }

}
