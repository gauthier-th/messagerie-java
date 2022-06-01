import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int port = 3000;
        SocketManager socketManager = new SocketManager(port);
        new Thread(socketManager).start();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            String message = scanner.nextLine();
            if (message.length() == 0)
                continue;
            try {
                socketManager.getThreads().get(0).sendMessage(message);
            }
            catch (Exception e) {
                System.out.println("Unable to send message.");
                e.printStackTrace();
            }
        }
    }

}
