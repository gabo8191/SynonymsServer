package co.edu.uptc.presenter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import co.edu.uptc.model.Synonym;
import co.edu.uptc.persistence.Persistence;

public class Presenter {

    private final int PUERTO = 1234;
    private ServerSocket serverSocket;
    private Synonym synonym;
    private Persistence persistence;
    private static final String DICTIONARY_DATA = "data/dictionary.txt";

    public Presenter() throws IOException {
        serverSocket = new ServerSocket(PUERTO);
        synonym = new Synonym();
        persistence = new Persistence(DICTIONARY_DATA);
    }

    public void runServer() throws IOException {

        while (true) {
            Socket socket = serverSocket.accept();
            ClientThread clientThread = new ClientThread(socket, synonym, persistence);
            clientThread.start();
        }
    }

    public static void main(String[] args) {
        try {
            Presenter presenter = new Presenter();
            presenter.runServer();

        } catch (IOException e) {
            System.out.println("Error in the server");
        }
    }

}
