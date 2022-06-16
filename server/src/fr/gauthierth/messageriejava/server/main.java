package fr.gauthierth.messageriejava.server;

import fr.gauthierth.messageriejava.server.socket.ChatManager;
import fr.gauthierth.messageriejava.server.socket.SocketManager;

public class main {

    public static void main(String[] args) {
        ChatManager chatManager = new ChatManager(); // We start the ChatManager.

        // We start the SocketManager in a new Thread on port 3000.
        int port = 3000;
        SocketManager socketManager = new SocketManager(port, chatManager);
        new Thread(socketManager).start();
    }

}
