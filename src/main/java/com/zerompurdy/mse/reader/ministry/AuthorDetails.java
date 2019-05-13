package com.zerompurdy.mse.reader.ministry;

/**
 * Created by mj_pu_000 on 19/03/2017.
 */
public class AuthorDetails {

    private String name;
    private String folderName;
    private String filePrefix;

    public AuthorDetails(String name, String folderName, String filePrefix) {
        this.name = name;
        this.folderName = folderName;
        this.filePrefix = filePrefix;
    }

    public String getName() {
        return name;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFilePrefix() {
        return filePrefix;
    }
}
