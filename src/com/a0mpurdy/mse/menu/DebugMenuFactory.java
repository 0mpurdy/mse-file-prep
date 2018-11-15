package com.a0mpurdy.mse.menu;

import com.a0mpurdy.mse.processors.Benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DebugMenuFactory {
    public static Menu make(Scanner sc) {
        List<MenuOption> options = new ArrayList<MenuOption>();
        options.add(new MenuOption("Back", () -> { }));

        options.add(new MenuOption("Read single author text", () -> {
            MenuAction.readSingleSerialMinistryAuthor(sc);
        }));

        options.add(new MenuOption("Benchmark", () -> {
            System.out.println("Benchmarking ...\n\n");
            new Benchmark().run();
        }));

        return new Menu(options);
    }
}
