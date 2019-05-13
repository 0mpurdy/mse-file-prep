package com.zerompurdy.mse.menu;

import com.zerompurdy.mse.common.config.Config;
import com.zerompurdy.mse.processors.Benchmark;
import com.zerompurdy.mse_core.data.hymn.HymnBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OtherMenuFactory {
    public static Menu make(Scanner sc, Config cfg) {
        List<MenuOption> options = new ArrayList<MenuOption>();
        options.add(new MenuOption("Back", () -> { }));

        options.add(new MenuOption("Prepare single author", () -> {
            MenuAction.prepareSingleAuthor(sc, cfg);
        }));

        options.add(new MenuOption("Create single author index", () -> {
            MenuAction.createSingleAuthorIndex(sc, cfg);
        }));

        options.add(new MenuOption("Create super index", () -> {
            System.out.println("Not currently working");
//                    MenuAction.createSuperIndex(sc, cfg);
        }));

        options.add(new MenuOption("Check author index", () -> {
            MenuAction.checkAuthorIndex(sc);
        }));

        options.add(new MenuOption("Check all author indexes", () -> {
            MenuAction.checkAllIndexes(sc);
        }));

        options.add(new MenuOption("Create serial Bibles", () -> {
            MenuAction.serializeAllBibles(MenuAction.chooseSystem(sc));
        }));

        options.add(new MenuOption("Create serial Hymns", () -> {
            MenuAction.serializeAllHymns(MenuAction.chooseSystem(sc));
            System.out.print("\rFinished reading all hymns\n");
        }));

        options.add(new MenuOption("Read serial Hymns", () -> {
            HymnBook test = MenuAction.readSerialHymn(sc);
            System.out.print("\rRead serial hymns\n");
        }));

        options.add(new MenuOption("Benchmark", () -> {
            System.out.println("Benchmarking ...\n\n");
            new Benchmark().run();
        }));

        return new Menu(options);
    }
}
