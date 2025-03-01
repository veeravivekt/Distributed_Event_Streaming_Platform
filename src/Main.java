import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket; 

public class Main {
  public static void main(String[] args){
    System.err.println("Logs from your program will appear here!");
    
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 9092;
    try { // binding a port
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true); 
      clientSocket = serverSocket.accept(); // accept incoming client connection
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally { // ensure that client socket is closed for free resources
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }
}
