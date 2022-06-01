import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int port = 3000;
        ChatManager chatManager = new ChatManager();
        SocketManager socketManager = new SocketManager(port, chatManager);
        new Thread(socketManager).start();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            String message = scanner.nextLine();
            if (message.length() == 0)
                continue;
            try {
                User user = chatManager.getUsers().get(0);
                socketManager.getSocketRunnable(user.getUuid()).sendMessage(message);
            }
            catch (Exception e) {
                System.out.println("Unable to send message.");
                e.printStackTrace();
            }
        }
    }

}
