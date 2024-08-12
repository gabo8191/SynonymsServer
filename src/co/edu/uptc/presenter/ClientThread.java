package co.edu.uptc.presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import co.edu.uptc.model.Synonym;
import co.edu.uptc.net.Connection;
import co.edu.uptc.net.Request;
import co.edu.uptc.net.Response;
import co.edu.uptc.persistence.Persistence;

public class ClientThread extends Thread {

  private Persistence persistence;
  private int index;
  private Connection connection;
  private Synonym synonym;
  private Gson gson;

  public ClientThread(Socket socket, Synonym synonym, Persistence persistence) {
    connection = new Connection(socket);
    gson = new Gson();
    this.synonym = synonym;
    this.persistence = persistence;
  }

  @Override
  public void run() {
    readData();
    startMenu();
  }

  public boolean readData() {
    Persistence persistence = new Persistence("data/dictionary.txt");
    try {
      Map<String, List<String>> data = persistence.loadFile();
      for (String word : data.keySet()) {
        synonym.addWord(word);
        for (String synonymWord : data.get(word)) {
          synonym.addSynonym(word, synonymWord);
        }
      }
      return true;
    } catch (FileNotFoundException e) {
      System.out.println("Error: Archivo no encontrado");
    } catch (IOException e) {
      System.out.println("Error de lectura del archivo");
    }
    return false;
  }

  public void startMenu() {
    try {
      connection.connect();
      String option = "";

      while (!option.equals("EXIT")) {
        Request request = gson.fromJson(connection.receive(), Request.class);
        option = request.getOption();

        switch (option) {
          case "ADD_SYNONYM":
            handleAddSynonymAction();
            break;
          case "FIND_SYNONYM":
            handleFindSynonymAction(request.getWord());
            break;
          case "NEXT":
            handleNextAction(request.getWord());
            break;
          case "PREVIOUS":
            handlePreviousAction(request.getWord());
            break;
          case "ACCEPT_WORD":
            handleAcceptWordAction(request.getWord());
            break;
          case "ACCEPT_SYNONYM":
            handleAcceptSynonymAction(request.getWord(), request.getSynonym());
            break;
          case "EXIT":
            break;
          default:
            connection.send(gson.toJson(new Response("Opción no válida")));
            break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error de E/S: " + e.getMessage());
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
  }

  public void writeDataWord(String word) {
    Persistence persistence = new Persistence("data/dictionary.txt");
    try {
      persistence.writeWord(word);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("Error reading file");
    }
  }

  public void writeDataSynonym(String word, String synonym) {
    Persistence persistence = new Persistence("data/dictionary.txt");
    try {
      persistence.writeSynonym(word, synonym);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("Error reading file");
    }
  }

  private void handleAddSynonymAction() {
    populateWordList();
  }

  private void populateWordList() {
    ArrayList<String> words = new ArrayList<>(synonym.getDictionary().keySet());
    String[] wordArray = words.toArray(new String[0]);
    String wordsString = String.join(",", wordArray);
    connection.send(gson.toJson(new Response(wordsString)));
  }

  private void handleFindSynonymAction(String word) {
    List<String> synonymList = synonym.getSynonymList(word);
    Response response = null;

    if (synonymList == null) {
      response = new Response("No existe la palabra");
    } else {
      if (synonymList.isEmpty()) {
        response = new Response("No tiene sinónimos");
      } else {
        index = 0;
        response = new Response(String.join(",", synonymList));
      }
    }
    connection.send(gson.toJson(response));
  }

  private void handleNextAction(String word) {
    List<String> synonymList = synonym.getSynonymList(word);

    Response response = null;
    if (index < synonymList.size() - 1) {
      index++;
      response = new Response(String.join(",", synonymList));
      connection.send(gson.toJson(response));
    } else {
      response = new Response("No hay más sinónimos");
      connection.send(gson.toJson(response));
    }
  }

  private void handlePreviousAction(String word) {
    List<String> synonymList = synonym.getSynonymList(word);
    Response response = null;
    if (index > 0) {
      index--;
      response = new Response(String.join(",", synonymList));
    } else {
      response = new Response("No hay más sinónimos");
    }
    connection.send(gson.toJson(response));
  }

  private void handleAcceptWordAction(String word) {
    if (!word.isEmpty()) {
      synonym.addWord(word);
      writeDataWord(word);
      connection.send(gson.toJson(new Response("Palabra agregada")));
    } else {
      connection.send(gson.toJson(new Response("No se puede agregar una palabra vacía")));
    }
  }

  private void handleAcceptSynonymAction(String word, String synonymWord) {
    if (!word.isEmpty() && !synonymWord.isEmpty()) {
      synonym.addSynonym(word, synonymWord);
      writeDataSynonym(word, synonymWord);
      connection.send(gson.toJson(new Response("Sinónimo agregado")));
    } else {
      connection.send(gson.toJson(new Response("No se puede agregar un sinónimo vacío")));
    }
  }
}
