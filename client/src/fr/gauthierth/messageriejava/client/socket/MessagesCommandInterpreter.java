package fr.gauthierth.messageriejava.client.socket;

import fr.gauthierth.messageriejava.client.ui.MessagesWindow;
import fr.gauthierth.messageriejava.client.objects.Channel;
import fr.gauthierth.messageriejava.client.objects.Message;
import fr.gauthierth.messageriejava.client.objects.User;

import java.sql.Date;

public class MessagesCommandInterpreter implements CommandInterpreter {

    MessagesWindow messagesWindow;

    public MessagesCommandInterpreter(MessagesWindow messagesWindow) {
        this.messagesWindow = messagesWindow;
    }

    public String executeCommand(String command) {
        String commandName, rest;
        String[] parts = command.split(" ", 2);
        commandName = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";

        if (commandName.equalsIgnoreCase("channel"))
            this.channel(rest);
        else if (commandName.equalsIgnoreCase("message"))
            this.message(rest);

        return null;
    }

    public void onDisconnect() {
        this.messagesWindow.socketDisconnect();
    }

    public void reloadChannel() {
        SocketManager.getInstance().sendMessage("channel get " + this.messagesWindow.getChannelUuid());
    }

    public void leaveChannel() {
        SocketManager.getInstance().sendMessage("channel disconnect " + this.messagesWindow.getChannelUuid());
    }

    public void reloadUsers() {
        SocketManager.getInstance().sendMessage("channel users " + this.messagesWindow.getChannelUuid());
    }

    public void sendMessage(String message) {
        SocketManager.getInstance().sendMessage("message create " + message);
    }

    private void channel(String args) {
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("infos")) {
            String[] items = rest.split(" ", 3);
            Channel channel = new Channel(items[0]);
            channel.setUsersConnectedCount(Integer.parseInt(items[1]));
            if (!items[2].equalsIgnoreCase("null"))
                channel.setName(items[2]);
            this.messagesWindow.updateChannel(channel);
        }
        else if (subCommand.equalsIgnoreCase("userlist")) {
            String[] userTexts = rest.split("\n");
            Channel channel = this.messagesWindow.getChannel();
            if (rest.length() > 0) {
                channel.getUsersConnected().clear();
                for (String userText : userTexts) {
                    System.out.println(userText);
                    String[] items = userText.split(" ", 3);
                    User user = new User(items[0]);
                    user.setLoggingDate(new Date(Long.parseLong(items[1])));
                    if (!items[2].equalsIgnoreCase("null"))
                        user.setName(items[2].trim());
                    channel.userConnect(user);
                }
                channel.setUsersConnectedCount(channel.getUsersConnected().size());
            }
            this.messagesWindow.updateUsers();
        }
        else if (subCommand.equalsIgnoreCase("join")) {
            String[] items = rest.split(" ", 3);
            User user = new User(items[0]);
            user.setLoggingDate(new Date(Long.parseLong(items[1])));
            if (!items[2].equalsIgnoreCase("null"))
                user.setName(items[2].trim());
            Channel channel = this.messagesWindow.getChannel();
            channel.userConnect(user);
            this.messagesWindow.userJoin(user);
        }
        else if (subCommand.equalsIgnoreCase("leave")) {
            Channel channel = this.messagesWindow.getChannel();
            String userUuid = rest;
            User user = channel.getUsersConnected().stream().filter(usr -> usr.getUuid().equalsIgnoreCase(userUuid)).findFirst().get();
            channel.userDisconnect(user);
            this.messagesWindow.userLeave(user);
        }
    }

    public void message(String args) {
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("created")) {
            String[] items = rest.split(" ", 2);
            String userUuid = items[0];
            String content = items[1];
            Channel channel = this.messagesWindow.getChannel();
            User user = channel.getUsersConnected().stream().filter(usr -> usr.getUuid().equalsIgnoreCase(userUuid)).findFirst().get();
            Message message = new Message(user, content, channel);
            this.messagesWindow.newMessage(message);
        }
    }

}
