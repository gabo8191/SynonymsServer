package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Synonym {
    private Map<String, Word> wordMap;

    public Synonym() {
        wordMap = new HashMap<>();
    }

    public synchronized void addSynonym(String word, String synonym) {
        Word wordAux = wordMap.get(word.toLowerCase());
        if (wordAux != null) {
            wordAux.addSynonym(synonym.toLowerCase());
        }
    }

    public synchronized Word searchWord(String word) {
        return wordMap.get(word.toLowerCase());
    }

    public synchronized void addWord(String word) {
        String lowerCaseWord = word.toLowerCase();
        if (!wordMap.containsKey(lowerCaseWord)) {
            wordMap.put(lowerCaseWord, new Word(lowerCaseWord));
        }
    }

    public synchronized ArrayList<String> getSynonymList(String word) {
        Word wordAux = searchWord(word);
        if (wordAux != null) {
            return wordAux.getSynonym();
        }
        return null;
    }

    public synchronized Map<String, List<String>> getDictionary() {
        Map<String, List<String>> dictionary = new HashMap<>();
        for (String word : wordMap.keySet()) {
            dictionary.put(word, wordMap.get(word).getSynonym());
        }
        return dictionary;
    }

}
