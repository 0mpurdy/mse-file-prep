package com.zerompurdy.mse_core.dto.hymn;

import java.util.Collection;

public class HymnDto {
    private int number;
    private String author;
    private String meter;
    private Collection<HymnVerseDto> verses;
    private HymnVerseDto chorus;

    public HymnDto() {
    }

    public HymnDto(int number, String author, String meter, Collection<HymnVerseDto> verses, HymnVerseDto chorus) {
        this.number = number;
        this.author = author;
        this.meter = meter;
        this.verses = verses;
        this.chorus = chorus;
    }
}
