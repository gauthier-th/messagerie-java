import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class SocketManager implements Runnable {

    int port;
    ChatManager chatManager;
    HashMap<String, SocketRunnable> runnables;
    HashMap<String, Thread> threads;

    SocketManager(int port, ChatManager chatManager ) {
        this.port = port;
        this.chatManager = chatManager;
        this.runnables = new HashMap<>();
        this.threads = new HashMap<>();
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
            String uuid = Utils.getUUID();
            SocketRunnable runnable = new SocketRunnable(socket, this, uuid);
            Thread socketThread = new Thread(runnable);
            this.chatManager.newUser(uuid, runnable);
            runnables.put(uuid, runnable);
            threads.put(uuid, socketThread);
            socketThread.start();
        }
    }

    public void runnableDisconnect(String uuid) {
        threads.get(uuid).interrupt();
        runnables.remove(uuid);
        threads.remove(uuid);
        this.chatManager.userDisconnect(uuid);
    }

    public SocketRunnable getSocketRunnable(String uuid) {
        return runnables.get(uuid);
    }

    public Thread getSocketThread(String uuid) {
        return threads.get(uuid);
    }
}
