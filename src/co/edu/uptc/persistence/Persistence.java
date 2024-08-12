package co.edu.uptc.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Persistence {
    private String fileName;

    public Persistence(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, List<String>> loadFile() throws IOException, FileNotFoundException {
        Map<String, List<String>> synonymData = new HashMap<>();

        BufferedReader buffer = new BufferedReader(new FileReader(fileName));
        String dictionary;

        while ((dictionary = buffer.readLine()) != null) {
            String[] parts = dictionary.split(",");
            if (parts.length >= 2) {
                String word = parts[0].trim();
                List<String> synonyms = new ArrayList<>();

                for (int i = 1; i < parts.length; i++) {
                    synonyms.add(parts[i].trim());
                }
                synonymData.put(word, synonyms);
            }
        }
        buffer.close();
        return synonymData;
    }

    public void writeWord(String word) throws IOException, FileNotFoundException {
        if (word != null && !word.isEmpty()) {
            Map<String, List<String>> synonymData = loadFile();
            if (!synonymData.containsKey(word)) {
                synonymData.put(word, new ArrayList<>());
                writeFile(synonymData);
            }
        }
    }

    public void writeSynonym(String word, String synonym) throws IOException, FileNotFoundException {
        Map<String, List<String>> synonymData = loadFile();
        List<String> synonyms = synonymData.get(word);
        if (synonyms == null) {
            synonyms = new ArrayList<>();
        }
        if (!synonyms.contains(synonym)) {
            synonyms.add(synonym);
            synonymData.put(word, synonyms);
            writeFile(synonymData);
        }
    }

    public void writeFile(Map<String, List<String>> synonymData) throws IOException, FileNotFoundException {
        StringBuilder data = new StringBuilder();
        for (String word : synonymData.keySet()) {
            data.append(word);
            List<String> synonyms = synonymData.get(word);
            if (synonyms != null) {
                for (String synonym : synonyms) {
                    data.append(",").append(synonym);
                }
            }
            data.append("\n");
        }
        BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName));
        buffer.write(data.toString());
        buffer.close();
    }

}
