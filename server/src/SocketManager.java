import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager {

    private int serverPort;

    SocketManager(int serverPort) {
        this.startServer();
    }

    private void startServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(serverPort);
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                System.out.println("Wainting for clients...");
                socket = serverSocket.accept();
            }
            catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new SocketThread(socket).start();
        }
    }

}
