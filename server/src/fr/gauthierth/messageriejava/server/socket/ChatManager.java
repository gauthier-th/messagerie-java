package fr.gauthierth.messageriejava.server.socket;

import fr.gauthierth.messageriejava.server.ConfigSaver;
import fr.gauthierth.messageriejava.server.Utils;
import fr.gauthierth.messageriejava.server.objects.Channel;
import fr.gauthierth.messageriejava.server.objects.Message;
import fr.gauthierth.messageriejava.server.objects.User;

import java.util.ArrayList;

/**
 * This class makes the link between events (connect, create channel, ...) and data objects (Channel, Message, User).
 */
public class ChatManager {

    ArrayList<User> users;
    ArrayList<Channel> channels;
    ConfigSaver configSaver;

    public ChatManager() {
        this.users = new ArrayList<>();
        this.configSaver = new ConfigSaver("config.txt");
        this.channels = this.configSaver.load();
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public User newUser(String uuid, SocketRunnable socketRunnable) { // We save the user to the user list.
        User user = new User(uuid, socketRunnable);
        users.add(user);
        return user;
    }

    public Channel connectToChannel(User user, String channelUUID) { // Connection to a channel.
        Channel channel = findChannelByUuid(channelUUID);
        if (channel != null)
            channel.userConnect(user);
        this.configSaver.save(this.channels);

        // We send the join channel event to all people in the channel:
        String commandJoinChannel = CommandInterpreter.userJoinToCommand(user);
        for (User usr : channel.getUsersConnected()) {
            if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                usr.getSocketRunnable().sendMessage(commandJoinChannel);
        }
        // We send the list channel event to every user connected (to update the user count of the channel list):
        String commandListChannel = CommandInterpreter.channelListedToCommand(this.channels);
        for (User usr : this.users) {
            if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                usr.getSocketRunnable().sendMessage(commandListChannel);
        }

        return channel;
    }

    public Channel userDisconnect(String uuid) { // Disconnection of the channel.
        User user = findUserByUuid(uuid);
        Channel channel = findUserChannel(user);
        if (channel != null) {
            channel.userDisconnect(user);
            //if (channel.getUsersConnected().size() == 0) // Delete channel if no more user.
            //    channels.remove(channel);
            this.configSaver.save(this.channels);

            // We send the leave channel event to all people in the channel:
            String commandLeaveChannel = CommandInterpreter.userLeaveToCommand(user);
            for (User usr : channel.getUsersConnected()) {
                if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                    usr.getSocketRunnable().sendMessage(commandLeaveChannel);
            }
            // We send the list channel event to every user connected (to update the user count of the channel list):
            String commandListChannel = CommandInterpreter.channelListedToCommand(this.channels);
            for (User usr : this.users) {
                if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                    usr.getSocketRunnable().sendMessage(commandListChannel);
            }
        }

        return channel;
    }

    public Channel createChannel(User user) { // Create a new channel
        Channel channel = new Channel(Utils.getUUID());
        this.channels.add(channel);
        this.configSaver.save(this.channels);

        // We send the list channel event to every user connected (to update the user count of the channel list):
        String command = CommandInterpreter.channelListedToCommand(this.channels);
        for (User usr : this.users) {
            if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                usr.getSocketRunnable().sendMessage(command);
        }

        return channel;
    }

    public Message sendMessage(User user, String content) { // Send a message to a channel
        Channel channel = findUserChannel(user);
        if (channel == null)
            return null;
        Message message = new Message(user, content, channel);
        channel.addMessage(message);
        this.configSaver.save(this.channels);

        // We send the new message event to all people in the channel:
        String command = CommandInterpreter.messageToCommand(message);
        for (User usr : channel.getUsersConnected()) {
            if (!user.getUuid().equalsIgnoreCase(usr.getUuid()))
                usr.getSocketRunnable().sendMessage(command);
        }

        return message;
    }

    public ArrayList<Channel> getChannels() {
        return this.channels;
    }

    public User findUserByUuid(String uuid) { // Utility function: find a User by its UUID.
        for (User user : this.getUsers()) {
            if (user.getUuid().equalsIgnoreCase(uuid))
                return user;
        }
        return null;
    }

    public Channel findChannelByUuid(String uuid) { // Utility function: find a Channel by its UUID.
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

    public Channel findUserChannel(User user) { // Utility function: find the Channel where an User is connected.
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
