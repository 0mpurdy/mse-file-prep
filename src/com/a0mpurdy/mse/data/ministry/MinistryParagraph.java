package com.a0mpurdy.mse.data.ministry;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by michaelpurdy on 14/03/2017.
 */
public class MinistryParagraph implements Serializable {

    private MinistryPage parentPage;
    private boolean isTitle;
    private ArrayList<String> sentences;

    public MinistryParagraph(MinistryPage parentPage) {
        this.parentPage = parentPage;
        this.isTitle = false;
        this.sentences = new ArrayList<>();
    }

    public MinistryParagraph(MinistryPage parentPage, String title) {
        this(parentPage);
        this.isTitle = true;
        this.sentences.add(title);
    }

    public void addSentence(String sentence) {
        // if previous sentence is not a full sentence then append
        if (previousSentenceFull()) {
            this.sentences.add(sentence);
        } else {
            this.sentences.set(this.sentences.size() - 1, getLastSentence() + " " + sentence);
        }
    }

    public MinistryPage getParentPage() {
        return parentPage;
    }

    public ArrayList<String> getSentences() {
        return sentences;
    }

    public String getSentence(int i) {
        return sentences.get(i);
    }

    public String getShortDescription() {
        return parentPage.getShortDescription() + ":" + this.parentPage.getParagraphs().indexOf(this);
    }

    public static boolean isFullSentence(String sentence) {
        if (sentence.length() < 1) return false;
        char lastChar = sentence.charAt(sentence.length() - 1);
        return lastChar == '.' || lastChar == '!' || lastChar == '?';
    }

    /**
     * Check if the previous sentence ended with a punctuation mark
     *
     * @return true if previous sentence ended with a punctuation mark
     */
    public boolean previousSentenceFull() {
        if (this.sentences.size() > 0) {
            return isFullSentence(getLastSentence());
        }
        return true;
    }

    public String getLastSentence() {
        return this.sentences.get(this.sentences.size() - 1);
    }
}
