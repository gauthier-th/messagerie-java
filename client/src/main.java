import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 3000;
        SocketManager socketManager = new SocketManager(address, port);
        new Thread(socketManager).start();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            String message = scanner.nextLine();
            if (message.length() == 0)
                continue;
            try {
                socketManager.sendMessage(message);
            }
            catch (Exception e) {
                System.out.println("Unable to send message.");
                e.printStackTrace();
            }
        }
    }

}
