package fr.gauthierth.messageriejava.client.socket;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

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

    public static SocketManager getInstance() {
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

    public void sendMessage(String message) {
        try {
            this.outputStream.flush();
            this.outputStream.write(message.replace("\n", "\f").getBytes());
            this.outputStream.write('\n');
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readMessage(String message) {
        System.out.println("Message received from server:");
        System.out.println(message);
        if (this.commandInterpreter != null)
            commandInterpreter.executeCommand(message);
    }

    private void waitMessages() throws Exception {
        try {
            System.out.println("Connecting to host " + this.address + ":" + this.port + "...");
            this.socket = new Socket(this.address, this.port);
            InputStream inputStream = this.socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            if (username != null)
                this.sendMessage("username " + username);
            System.out.println("Connected!");
            if (this.connectedCallback != null) {
                this.connectedCallback.run();
                this.connectedCallback = null;
            }
        }
        catch (IOException e) {
            System.out.println("Connexion refused.");
            JOptionPane.showMessageDialog(null, "Impossible de se connecter au serveur.");
            return;
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
                    this.readMessage(line.replace("\f", "\n"));
                }
            }
            catch (Exception e) {
                System.out.println("Server disconnected");
                if (this.commandInterpreter != null)
                    commandInterpreter.onDisconnect();
                try {
                    this.socket.close();
                }
                catch (Exception exc) {}
                return;
            }
        }
    }

}
