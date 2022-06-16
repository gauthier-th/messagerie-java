package fr.gauthierth.messageriejava.server.objects;

import java.util.ArrayList;

/**
 * Channel class, to store Channel infos with Users and Messages.
 */
public class Channel {

    protected String uuid;
    protected String name = null;
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

    public ArrayList<User> getUsersConnected() {
        return usersConnected;
    }
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void userConnect(User user) {
        this.usersConnected.add(user);
    }
    public void userDisconnect(User user) {
        this.usersConnected.removeIf((u) -> user.getUuid().equals(u.getUuid()));
    }
    public void addMessage(Message message) {
        this.messages.add(message);
    }

}
