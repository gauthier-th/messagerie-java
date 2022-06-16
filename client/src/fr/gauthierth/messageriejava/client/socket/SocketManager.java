package fr.gauthierth.messageriejava.client.socket;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class manages socket connection between the client and the server.
 */
public class SocketManager implements Runnable {

    String address;
    int port;
    String username = null;
    Socket socket;
    BufferedReader bufferedReader;
    DataOutputStream outputStream;

    private Runnable connectedCallback = null;
    private CommandInterpreter commandInterpreter = null;

    private static SocketManager socketManager = null;
    private static Thread socketManagerThread = null;

    public SocketManager(String address, int port, String username) {
        this.address = address;
        this.port = port;
        if (username != null && username.length() > 0)
            this.username = username;
    }

    public static SocketManager getInstance() { // Singleton of SocketManager
        return socketManager;
    }
    public static void startManager(String address, int port, String username) {
        socketManager = new SocketManager(address, port, username);
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

    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }
    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    @Override
    public void run() {
        try {
            this.waitMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) { // Function to send a message to the server socket.
        try {
            this.outputStream.flush();
            this.outputStream.write(message.replace("\n", "\f").getBytes());
            this.outputStream.write('\n');
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readMessage(String message) { // Function to read a message to the socket socket.
        System.out.println("Message received from server:");
        System.out.println(message);
        if (this.commandInterpreter != null)
            commandInterpreter.executeCommand(message);
    }

    private void waitMessages() throws Exception { // Function to read new messages on the socket connection.
        try {
            // Creation of the socket and the input and output stream:
            System.out.println("Connecting to host " + this.address + ":" + this.port + "...");
            this.socket = new Socket(this.address, this.port);
            InputStream inputStream = this.socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            if (username != null)
                this.sendMessage("username " + username);
            System.out.println("Connected!");
            if (this.connectedCallback != null) { // Callback when successfully connected:
                this.connectedCallback.run();
                this.connectedCallback = null;
            }
        }
        catch (IOException e) {
            System.out.println("Connexion refused.");
            JOptionPane.showMessageDialog(null, "Impossible de se connecter au serveur.");
            return;
        }

        while (true) { // We continuously wait for new messages on the socket connection.
            try {
                String line = this.bufferedReader.readLine(); // If we detect a new line of the input stream:
                if (line == null || line.equalsIgnoreCase("QUIT")) {
                    // We close the socket connection:
                    this.bufferedReader.close();
                    this.outputStream.close();
                    this.socket.close();
                }
                else {
                    // We replace \f by \n in order to not confuse new lines (\n) and message end (\f).
                    this.readMessage(line.replace("\f", "\n"));
                }
            }
            catch (Exception e) {
                System.out.println("Server disconnected");
                // We close the socket connection:
                if (this.commandInterpreter != null) // Disconnect callback:
                    commandInterpreter.onDisconnect();
                try {
                    this.bufferedReader.close();
                    this.outputStream.close();
                    this.socket.close();
                }
                catch (Exception exc) {}
                return;
            }
        }
    }

}
