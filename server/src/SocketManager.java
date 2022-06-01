import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketManager implements Runnable {

    int port;
    ArrayList<SocketRunnable> runnables;

    SocketManager(int port) {
        this.port = port;
        this.runnables = new ArrayList<>();
    }

    @Override
    public void run() {
        this.startServer();
    }

    private void startServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(this.port);
            System.out.println("Server listening on port " + this.port);
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
            SocketRunnable runnable = new SocketRunnable(socket);
            Thread thread = new Thread(runnable);
            thread.start();
            runnables.add(runnable);
        }
    }

    public ArrayList<SocketRunnable> getThreads() {
        return runnables;
    }
}
