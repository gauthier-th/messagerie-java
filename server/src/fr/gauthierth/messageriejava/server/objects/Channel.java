package fr.gauthierth.messageriejava.server.objects;

import java.util.ArrayList;

public class Channel {

    protected String uuid;
    protected String name = null;
    protected String password = null;
    protected int maxMessageCount = 100;
    private ArrayList<User> usersConnected;
    private ArrayList<Message> messages;

    public Channel(String uuid) {
        this.uuid = uuid;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxMessageCount() {
        return maxMessageCount;
    }
    public void setMaxMessageCount(int maxMessageCount) {
        this.maxMessageCount = maxMessageCount;
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
        if (this.messages.size() > this.maxMessageCount)
            this.messages.remove(0);
    }

}
