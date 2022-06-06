import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int port = 3000;
        ChatManager chatManager = new ChatManager();
        SocketManager socketManager = new SocketManager(port, chatManager);
        new Thread(socketManager).start();
    }

}
