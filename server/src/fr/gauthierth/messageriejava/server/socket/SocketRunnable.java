package fr.gauthierth.messageriejava.server.socket;

import java.io.*;
import java.net.Socket;

/**
 * This class is a Thread for handling the Socket connection between client and server.
 */
public class SocketRunnable implements Runnable {

    Socket socket;
    SocketManager socketManager;
    String uuid;
    BufferedReader bufferedReader;
    DataOutputStream outputStream;

    public SocketRunnable(Socket socket, SocketManager socketManager, String uuid) {
        this.socket = socket;
        this.socketManager = socketManager;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        try {
            this.waitMessages();
        }
        catch (Exception e) {
            return;
        }
    }

    public void sendMessage(String message) { // Function to send a message to the client socket.
        if (message == null || message.length() == 0)
            return;
        try {
            System.out.println("Send to client:");
            System.out.println(message);
            this.outputStream.flush();
            this.outputStream.write(message.replace("\n", "\f").getBytes());
            this.outputStream.write('\n');
        }
        catch (Exception e) {}
    }

    private void readMessage(String message) { // Function to read a message to the client socket.
        System.out.println("Message received from client:");
        System.out.println(message);
        String result = this.socketManager.getCommandInterpreter().executeCommand(this.uuid, message);
        this.sendMessage(result);
    }

    private void waitMessages() throws Exception { // Function to read new messages on the socket connection.
        System.out.println("New client detected");

        try {
            // Creation of the input and output stream:
            InputStream inputStream = socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("Exception in message reader");
        }
        while (true) { // We continuously wait for new messages on the socket connection.
            try {
                String line = this.bufferedReader.readLine(); // If we detect a new line of the input stream:
                if (line == null || line.equalsIgnoreCase("QUIT")) {
                    // We close the socket connection:
                    try {
                        this.bufferedReader.close();
                        this.outputStream.close();
                        this.socket.close();
                    }
                    catch (Exception exc) {}
                    this.socketManager.runnableDisconnect(uuid);
                }
                else {
                    // We replace \f by \n in order to not confuse new lines (\n) and message end (\f).
                    String message = line.replace("\f", "\n");
                    this.readMessage(message);
                }
            }
            catch (Exception e) {
                System.out.println("Client disconnected");
                // We close the socket connection:
                try {
                    this.bufferedReader.close();
                    this.outputStream.close();
                    this.socket.close();
                }
                catch (Exception exc) {}
                this.socketManager.runnableDisconnect(uuid);
                return;
            }
        }
    }

}
