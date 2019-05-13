/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zerompurdy.mse.common.config;

/**
 *
 * @author michael
 */
public class Config {

    private static final String mseVersion = "3.0.6";

    // todo fix laziness
    public FileConfig fileConfig;

    public Config() {
        setDefaults();
    }

    /**
     * Set the defaults for the configuration if one doesn't exist already
     */
    private void setDefaults() {
        fileConfig = new FileConfig();
    }

    public String getMseVersion() {
        return mseVersion;
    }

    // todo make synopsis configurable again?
    /**
     * Should the link to the synopsis be included when preparing the HTML files
     * @return boolean True if the link to the synopsis should be included
     */
    public boolean isSynopsis() {
        return true;
    }

}
