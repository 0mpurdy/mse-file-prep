package com.zerompurdy.mse_core.data.hymn;

import com.zerompurdy.mse_core.dto.hymn.HymnDto;
import com.zerompurdy.mse_core.dto.hymn.HymnVerseDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Hymn
 *
 * @author MichaelPurdy
 */
public class Hymn implements Serializable {

    private static final long serialVersionUID = 4L;

    private HymnBook parentHymnBook;
    private int number;
    private String author;
    private String meter;
    private ArrayList<HymnVerse> verses;
    private HymnVerse chorus;

    public Hymn(HymnBook parentHymnBook, int number, String author, String meter) {
        this.parentHymnBook = parentHymnBook;
        this.number = number;
        this.author = author;
        this.meter = meter;
        this.verses = new ArrayList<>();
    }

    public HymnBook getParentHymnBook() {
        return parentHymnBook;
    }

    public String getShortDescription() {
        return this.parentHymnBook.getShortDescription() + ":" + number;
    }

    public int getNumber() {
        return number;
    }

    public HymnVerse getVerse(int number) {
        return verses.get(number - 1);
    }

    public String getVerseText() {
        StringBuilder verseText = new StringBuilder();
        for (HymnVerse verse : verses) {
            verseText.append(verse.getVerseText());
            verseText.append('\n');
        }
        return verseText.toString();
    }

    public ArrayList<HymnVerse> getVerses() {
        return verses;
    }

    public void setChorus(HymnVerse chorus) {
        this.chorus = chorus;
    }

    public void addVerse(HymnVerse verse) {
        this.verses.add(verse);
    }

    public HymnDto toDto() {
        Collection<HymnVerseDto> dtoVerses = this.verses.stream().map(x -> x.toDto()).collect(Collectors.toList());
        HymnVerseDto chorusDto = this.chorus != null
            ? this.chorus.toDto()
            : null;

        return new HymnDto(
            this.number,
            this.author,
            this.meter,
            dtoVerses,
            chorusDto);
    }
}
