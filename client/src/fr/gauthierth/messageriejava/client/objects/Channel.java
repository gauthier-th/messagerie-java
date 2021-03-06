package fr.gauthierth.messageriejava.client.objects;

import java.util.ArrayList;

/**
 * Channel class, to store Channel infos with Users and Messages.
 */
public class Channel {

    protected String uuid;
    protected String name = null;
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
    public String getDisplayName() { // Get the name that will be displayed on the UI
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
