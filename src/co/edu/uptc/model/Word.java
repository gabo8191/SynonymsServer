package co.edu.uptc.model;

import java.util.ArrayList;

public class Word {
    private String word;
    private ArrayList<String> synonym;

    public Word(String word) {
        this.word = word.toLowerCase();
        synonym = new ArrayList<>();
    }

    public void addSynonym(String synonym) {
        this.synonym.add(synonym);
    }

    public ArrayList<String> getSynonym() {
        return synonym;
    }

    public String getWord() {
        return word;
    }
}
