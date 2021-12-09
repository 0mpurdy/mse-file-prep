package com.zerompurdy.mse_core.dto.hymn;

import java.util.Collection;

public class HymnVerseDto {
    private int verseNumber;
    private Collection<String> lines;

    public HymnVerseDto() {
    }

    public HymnVerseDto(int verseNumber, Collection<String> lines) {
        this.verseNumber = verseNumber;
        this.lines = lines;
    }
}
