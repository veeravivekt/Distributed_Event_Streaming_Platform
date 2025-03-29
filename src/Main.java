import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays; 

public class Main {
  public static void main(String[] args){
    System.err.println("Logs from your program will appear here!");
    
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 9092;
    try { // binding a port
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true); 
      while (true) {
        clientSocket = serverSocket.accept();
        System.out.println("Client connected!");

        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        // Read the first 12 bytes of the request
        byte[] requestHeader = new byte[12];
        int bytesRead = inputStream.read(requestHeader);

        if (bytesRead == 12) {
          // get the correlationId (bytes 8 to 12)
          byte[] correlationId = Arrays.copyOfRange(requestHeader, 8, 12); 

          // Prepare the response
          byte[] response = new byte[8];
          // First 4 bytes set to 0
          System.arraycopy(new byte[]{0, 0, 0, 0}, 0, response, 0, 4);
          
          System.arraycopy(correlationId, 0, response, 4, 4);

          // Send response
          outputStream.write(response);
          outputStream.flush();

          System.out.println("Response sent with correlation ID: " + String.format("%02X%02X%02X%02X", correlationId[0], correlationId[1], correlationId[2], correlationId[3]));
        } else {
          System.err.println("Received incomplete request header");
        }
        clientSocket.close();
      }
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
