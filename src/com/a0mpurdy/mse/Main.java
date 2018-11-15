package com.a0mpurdy.mse;

import com.a0mpurdy.mse.common.config.Config;
import com.a0mpurdy.mse.menu.MainMenuFactory;
import com.a0mpurdy.mse.menu.Menu;

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
