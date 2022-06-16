package fr.gauthierth.messageriejava.server;

import java.util.ArrayList;

public class CommandInterpreter {

    ChatManager chatManager;

    CommandInterpreter(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public String executeCommand(String uuid, String command) {
        String commandName, rest;
        String[] parts = command.split(" ", 2);
        commandName = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";

        User user = this.chatManager.findUserByUuid(uuid);

        if (commandName.equalsIgnoreCase("channel"))
            return this.channel(user, rest);
        else if (commandName.equalsIgnoreCase("message"))
            return this.message(user, rest);
        else if (commandName.equalsIgnoreCase("username"))
            return this.username(user, rest);
        else
            return "error Unknown command";
    }

    private String channel(User user, String args) {
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("connect")) {
            Channel channel = this.chatManager.connectToChannel(user, rest);
            if (channel != null)
                return "channel connected " + channel.getUuid();
            else
                return "error Unknown channel";
        }
        else if (subCommand.equalsIgnoreCase("disconnect")) {
            Channel channel = this.chatManager.userDisconnect(user.getUuid());
            if (channel != null)
                return "channel disconnected " + channel.getUuid();
            else
                return "error Not connected to channel";
        }
        else if (subCommand.equalsIgnoreCase("get")) {
            Channel channel = this.chatManager.findChannelByUuid(rest);
            if (channel != null)
                return "channel infos " + channel.getUuid() + " " + channel.getUsersConnected().size() + " " + channel.getName();
            else
                return "error Unknown channel";
        }
        else if (subCommand.equalsIgnoreCase("create")) {
            Channel channel = this.chatManager.createChannel(user);
            if (rest.length() > 0)
                channel.setName(rest);
            return "channel created " + channel.getUuid();
        }
        else if (subCommand.equalsIgnoreCase("list")) {
            return channelListedToCommand(this.chatManager.getChannels());
        }
        else if (subCommand.equalsIgnoreCase("users")) {
            Channel channel = this.chatManager.findChannelByUuid(rest);
            if (channel == null)
                return "error Not connected to channel";
            ArrayList<User> users = channel.getUsersConnected();
            String result = "";
            for (User usr : users) {
                result += "\n" + usr.getUuid() + " " + usr.getLoggingDate().getTime() + " " + usr.getName();
            }
            return ("channel userlist " + result.trim()).trim();
        }
        else
            return "error Unknown subcommand";
    }

    private String message(User user, String args) {
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("create")) {
            Message message = this.chatManager.sendMessage(user, rest);
            if (message != null)
                return messageToCommand(message);
            else
                return "error Not connected to channel";
        }
        else
            return "error Unknown subcommand";
    }

    private String username(User user, String args) {
        if (args.length() > 0)
            user.setName(args);
        return "";
    }

    public static String userJoinToCommand(User user) {
        return "channel join " + user.getUuid() + " " + user.getLoggingDate().getTime() + " " + user.getName();
    }
    public static String userLeaveToCommand(User user) {
        return "channel leave " + user.getUuid();
    }
    public static String messageToCommand(Message message) {
        return "message created " + message.getAuthor().getUuid() + " " + message.getContent();
    }
    public static String channelListedToCommand(ArrayList<Channel> channels)  {
        String result = "";
        for (Channel channel : channels) {
            result += "\n" + channel.getUuid() + " " + channel.getUsersConnected().size() + " " + channel.getName();
        }
        return ("channel listed " + result.trim()).trim();
    }

}
