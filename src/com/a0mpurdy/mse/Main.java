package com.a0mpurdy.mse;

import com.a0mpurdy.mse.common.config.Config;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Cli cli = new Cli();

        cli.start(new Config());

    }

}
