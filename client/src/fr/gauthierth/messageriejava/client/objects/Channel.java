package fr.gauthierth.messageriejava.client.objects;

import java.util.ArrayList;

public class Channel {

    protected String uuid;
    protected String name = null;
    protected String password = null;
    protected int maxMessageCount = 100;
    private int usersConnectedCount;
    private ArrayList<User> usersConnected;
    private ArrayList<Message> messages;

    public Channel(String uuid) {
        this.uuid = uuid;
        this.usersConnectedCount = 0;
        this.usersConnected = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }
    public String getDisplayName() {
        if (this.name == null)
            return "Sans nom";
        else
            return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getUsersConnectedCount() {
        return usersConnectedCount;
    }
    public void setUsersConnectedCount(int usersConnectedCount) {
        this.usersConnectedCount = usersConnectedCount;
    }

    public ArrayList<User> getUsersConnected() {
        return usersConnected;
    }
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public boolean userConnect(User user) {
        if (this.password != null)
            return false;
        this.usersConnected.add(user);
        return true;
    }
    public void userDisconnect(User user) {
        this.usersConnected.removeIf((u) -> user.getUuid().equals(u.getUuid()));
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

}
