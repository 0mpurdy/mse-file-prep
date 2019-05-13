package com.zerompurdy.mse;

import com.zerompurdy.mse.common.config.Config;
import com.zerompurdy.mse.menu.MainMenuFactory;
import com.zerompurdy.mse.menu.Menu;

import java.util.Scanner;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Config cfg = new Config();

        System.out.println("MSE File Prep console application");
        System.out.println("Version: " + cfg.getMseVersion());

        Scanner sc = new Scanner(System.in);
        Menu mainMenu = MainMenuFactory.make(sc, cfg);

        int mainMenuChoice = -1;

        while (mainMenuChoice != 0) {
            mainMenuChoice = mainMenu.run(sc);
        }
    }

}
