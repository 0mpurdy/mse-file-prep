package com.zerompurdy.mse_core.dto.hymn;

import java.util.Collection;

public class HymnBookDto {
    /**
     * Title of the hymn book
     */
    private String title;
    private String filename;
    private String code;

    /**
     * List of hymns in the book
     */
    private Collection<HymnDto> hymns;

    public HymnBookDto() {
    }

    public HymnBookDto(String title, String filename, String code, Collection<HymnDto> hymns) {
        this.title = title;
        this.filename = filename;
        this.code = code;
        this.hymns = hymns;
    }
}
