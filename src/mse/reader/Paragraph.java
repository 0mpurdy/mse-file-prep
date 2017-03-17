package mse.reader;

import java.util.ArrayList;

/**
 * Created by michaelpurdy on 14/03/2017.
 */
public class Paragraph {

    private Page parentPage;
    private ArrayList<String> sentences;

    Paragraph(String sentence) {
        this.sentences = new ArrayList<>();
        this.sentences.add(sentence);
    }

    public Page getParentPage() {
        return parentPage;
    }

    public ArrayList<String> getSentences() {
        return sentences;
    }

    public String getSentence(int i) {
        return sentences.get(i);
    }
}
