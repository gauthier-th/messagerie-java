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

    public User newUser(String uuid, SocketRunnable socketRunnable) {
        User user = new User(uuid, socketRunnable);
        users.add(user);
        return user;
    }

    public Channel connectToChannel(User user, String channelUUID) {
        Channel channel = findChannelByUuid(channelUUID);
        if (channel != null)
            channel.userConnect(user);

        String commandJoinChannel = CommandInterpreter.userJoinToCommand(user);
        for (User usr : channel.getUsersConnected()) {
            if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                usr.getSocketRunnable().sendMessage(commandJoinChannel);
        }
        String commandListChannel = CommandInterpreter.channelListedToCommand(this.channels);
        for (User usr : this.users) {
            if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                usr.getSocketRunnable().sendMessage(commandListChannel);
        }

        return channel;
    }

    public Channel userDisconnect(String uuid) {
        User user = findUserByUuid(uuid);
        Channel channel = findUserChannel(user);
        if (channel != null) {
            channel.userDisconnect(user);
            if (channel.getUsersConnected().size() == 0)
                channels.remove(channel);

            String commandLeaveChannel = CommandInterpreter.userLeaveToCommand(user);
            for (User usr : channel.getUsersConnected()) {
                if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                    usr.getSocketRunnable().sendMessage(commandLeaveChannel);
            }
            String commandListChannel = CommandInterpreter.channelListedToCommand(this.channels);
            for (User usr : this.users) {
                if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                    usr.getSocketRunnable().sendMessage(commandListChannel);
            }
        }

        return channel;
    }

    public Channel createChannel(User user) {
        Channel channel = new Channel(Utils.getUUID());
        this.channels.add(channel);

        String command = CommandInterpreter.channelListedToCommand(this.channels);
        for (User usr : this.users) {
            if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                usr.getSocketRunnable().sendMessage(command);
        }

        return channel;
    }

    public Message sendMessage(User user, String content) {
        Channel channel = findUserChannel(user);
        if (channel == null)
            return null;
        Message message = new Message(user, content, channel);
        channel.addMessage(message);

        String command = CommandInterpreter.messageToCommand(message);
        for (User usr : channel.getUsersConnected()) {
            usr.getSocketRunnable().sendMessage(command);
        }

        return message;
    }

    public ArrayList<Channel> getChannels() {
        return this.channels;
    }

    public User findUserByUuid(String uuid) {
        for (User user : this.getUsers()) {
            if (user.getUuid().equalsIgnoreCase(uuid))
                return user;
        }
        return null;
    }

    public Channel findChannelByUuid(String uuid) {
        Channel channel = null;
        for (Channel chan : this.channels) {
            if (chan.getUuid().equalsIgnoreCase(uuid)) {
                channel = chan;
                break;
            }
        }
        for (User usr : this.users) {
            if (usr.getUuid().equalsIgnoreCase(uuid)) {
                channel = usr;
                break;
            }
        }
        return channel;
    }

    public Channel findUserChannel(User user) {
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
