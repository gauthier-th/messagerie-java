import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class SocketManager implements Runnable {

    String address;
    int port;
    Socket socket;
    BufferedReader bufferedReader;
    DataOutputStream outputStream;

    private Runnable connectedCallback = null;

    private static SocketManager socketManager = null;
    private static Thread socketManagerThread = null;

    SocketManager(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public static SocketManager getInstance() {
        return socketManager;
    }
    public static void startManager(String address, int port) {
        socketManager = new SocketManager(address, port);
        socketManagerThread = new Thread(socketManager);
        socketManagerThread.start();
    }

    String getAddress() {
        return this.address;
    }
    int getPort() {
        return this.port;
    }

    public Runnable getConnectedCallback() {
        return connectedCallback;
    }
    public void setConnectedCallback(Runnable connectedCallback) {
        this.connectedCallback = connectedCallback;
    }

    @Override
    public void run() {
        try {
            this.waitMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws Exception {
        this.outputStream.flush();
        this.outputStream.write(message.getBytes());
        this.outputStream.write('\n');
    }

    private void readMessage(String message) {
        System.out.println("Message received from server:");
        System.out.println(message);
    }

    private void waitMessages() throws Exception {
        try {
            System.out.println("Connecting to host " + this.address + ":" + this.port + "...");
            this.socket = new Socket(this.address, this.port);
            InputStream inputStream = this.socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected!");
            if (this.connectedCallback != null)
                this.connectedCallback.run();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new Exception();
        }

        while (true) {
            try {
                String line = this.bufferedReader.readLine();
                if (line == null || line.equalsIgnoreCase("QUIT")) {
                    this.bufferedReader.close();
                    this.outputStream.close();
                    socket.close();
                }
                else {
                    this.readMessage(line);
                }
            }
            catch (SocketException e) {
                System.out.println("Server disconnected");
                return;
            }
            catch (IOException e) {
                System.out.println("Exception in message reader");
                e.printStackTrace();
                throw new Exception();
            }
        }
    }

}
