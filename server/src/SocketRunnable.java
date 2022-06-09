import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class SocketRunnable implements Runnable {

    Socket socket;
    SocketManager socketManager;
    String uuid;
    BufferedReader bufferedReader;
    DataOutputStream outputStream;

    SocketRunnable(Socket socket, SocketManager socketManager, String uuid) {
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

    public void sendMessage(String message) {
        if (message == null || message.length() == 0)
            return;
        try {
            this.outputStream.flush();
            this.outputStream.write(message.replace("\n", "\f").getBytes());
            this.outputStream.write('\n');
        }
        catch (Exception e) {}
    }

    private void readMessage(String message) {
        System.out.println("Message received from client:");
        System.out.println(message);
        String result = this.socketManager.getCommandInterpreter().executeCommand(this.uuid, message);
        System.out.println("Result:");
        System.out.println(result);
        this.sendMessage(result);
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
                    this.bufferedReader.close();
                    this.outputStream.close();
                    socket.close();
                }
                else {
                    this.readMessage(line.replace("\f", "\n"));
                }
            }
            catch (SocketException e) {
                System.out.println("Client disconnected");
                this.socketManager.runnableDisconnect(uuid);
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
