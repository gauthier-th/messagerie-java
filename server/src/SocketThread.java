import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {

    Socket socket;
    BufferedReader bufferedReader;
    DataOutputStream outputStream;

    SocketThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            this.waitMessages();
        }
        catch (Exception e) {
            return;
        }
    }

    public void sendMessage(String message) throws Exception {
        this.outputStream.writeBytes(message);
        this.outputStream.flush();
    }

    private void readMessage(String message) {
        System.out.println("Message received from client:");
        System.out.println(message);
    }

    private void waitMessages() throws Exception {
        System.out.println("New client detected");

        try {
            InputStream inputStream = socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("Exception in message reader");
            e.printStackTrace();
            throw new Exception();
        }
        while (true) {
            try {
                String line = this.bufferedReader.readLine();
                if (line == null || line.equalsIgnoreCase("QUIT")) {
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
