package fr.gauthierth.messageriejava.server;

import fr.gauthierth.messageriejava.server.socket.ChatManager;
import fr.gauthierth.messageriejava.server.socket.SocketManager;

public class main {

    public static void main(String[] args) {
        int port = 3001;
        ChatManager chatManager = new ChatManager();
        SocketManager socketManager = new SocketManager(port, chatManager);
        new Thread(socketManager).start();
    }

}
