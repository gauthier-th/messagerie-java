import java.io.*;
import java.net.Socket;

public class SocketManager {

    String address;
    int port;
    Socket socket;
    BufferedReader bufferedReader;
    DataOutputStream outputStream;

    SocketManager(String address, int port) {
        this.address = address;
        this.port = port;

        try {
            this.waitMessages();
        }
        catch (Exception e) {
            return;
        }
    }

    String getAddress() {
        return this.address;
    }
    int getPort() {
        return this.port;
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
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            System.out.println("Connected!");
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
            catch (IOException e) {
                System.out.println("Exception in message reader");
                e.printStackTrace();
                throw new Exception();
            }
        }
    }

}
