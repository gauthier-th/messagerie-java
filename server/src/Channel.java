import java.util.ArrayList;

public class Channel {

    protected ChannelOptions channelOptions;
    private ArrayList<User> usersConnected;
    private ArrayList<Message> messages;

    Channel(String name) {
        this(new ChannelOptions(name));
    }
    Channel(ChannelOptions channelOptions) {
        this.channelOptions = channelOptions;
        this.usersConnected = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public ChannelOptions getChannelOptions() {
        return this.channelOptions;
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
