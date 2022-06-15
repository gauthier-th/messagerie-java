package fr.gauthierth.messageriejava;

import java.util.ArrayList;

public class Channel {

    protected String uuid;
    protected String name = null;
    protected ChannelOptions channelOptions;
    private int usersConnectedCount;
    private ArrayList<User> usersConnected;
    private ArrayList<Message> messages;

    Channel(String uuid) {
        this.uuid = uuid;
        this.channelOptions = new ChannelOptions();
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

    public ChannelOptions getChannelOptions() {
        return this.channelOptions;
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
        if (this.channelOptions.getPassword() != null)
            return false;
        this.usersConnected.add(user);
        return true;
    }
    public void userDisconnect(User user) {
        this.usersConnected.removeIf((u) -> user.getUuid().equals(u.getUuid()));
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        if (this.messages.size() > this.channelOptions.getMaxMessageCount())
            this.messages.remove(0);
    }

}