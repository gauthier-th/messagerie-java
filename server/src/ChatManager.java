import java.util.ArrayList;

public class ChatManager {

    ArrayList<User> users;
    ArrayList<Channel> channels;

    ChatManager() {
        this.users = new ArrayList<>();
        this.channels = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public User newUser(String uuid) {
        User user = new User(uuid);
        users.add(user);
        return user;
    }

    public Channel connectToChannel(User user, String channelUUID) {
        Channel channel = findChannelByUuid(channelUUID);
        if (channel != null)
            channel.userConnect(user);
        return channel;
    }

    public void userDisconnect(User user) {
        Channel channel = findUserChannel(user);
        if (channel != null)
            channel.userDisconnect(user);
    }

    private Channel findChannelByUuid(String uuid) {
        Channel channel = null;
        for (Channel chan : this.channels) {
            if (chan.getUuid() == uuid) {
                channel = chan;
                break;
            }
        }
        for (User usr : this.users) {
            if (usr.getUuid() == uuid) {
                channel = usr;
                break;
            }
        }
        return channel;
    }

    private Channel findUserChannel(User user) {
        Channel channel = null;
        for (Channel chan : this.channels) {
            if (chan.getUsersConnected().contains(user)) {
                channel = chan;
                break;
            }
        }
        for (User usr : this.users) {
            if (usr.getUsersConnected().contains(user)) {
                channel = usr;
                break;
            }
        }
        return channel;
    }

}
