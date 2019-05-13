package com.zerompurdy.mse.menu;

import java.util.List;
import java.util.Scanner;

public class Menu {
    List<MenuOption> options;

    public Menu(List<MenuOption> options) {
        this.options = options;
    }

    public void print() {
        System.out.println();
        int i = 0;
        for (MenuOption option : options) {
            System.out.println(i + " - " + option.getName());
            i++;
        }
    }

    public int getChoice(Scanner sc) {
        print();
        System.out.print("Choose an option: ");

        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }

    /**
     * Run one option from the menu
     * @param sc Scanner for input
     * @return The menu option chosen
     */
    public int run(Scanner sc) {
        int choice = getChoice(sc);

        if (choice >= options.size()) {
            System.out.println("Invalid choice");
            return choice;
        }

        options.get(choice).run();
        return choice;
    }
}
