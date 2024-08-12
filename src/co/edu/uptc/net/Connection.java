package co.edu.uptc.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

  private Socket socket;
  private DataOutputStream salida;
  private DataInputStream entrada;

  public Connection(Socket socket) {
    this.socket = socket;
  }

  public void connect() {

    try {
      salida = new DataOutputStream(socket.getOutputStream());
      entrada = new DataInputStream(socket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException("Error al crear los flujos de entrada y salida");
    }
  }

  public void close() {
    try {
      if (socket != null) {
        socket.close();
      }
    } catch (Exception e) {
      throw new RuntimeException("Error al cerrar el socket");
    }
  }

  public void send(String message) {
    try {
      salida.writeUTF(message);
    } catch (Exception e) {
      throw new RuntimeException("Error al enviar el mensaje");
    }
  }

  public String receive() {
    try {
      return entrada.readUTF();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean isClosed() {
    return socket.isClosed();
  }

}
