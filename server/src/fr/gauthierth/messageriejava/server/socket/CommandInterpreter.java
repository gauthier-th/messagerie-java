package fr.gauthierth.messageriejava.server.socket;

import fr.gauthierth.messageriejava.server.objects.Channel;
import fr.gauthierth.messageriejava.server.objects.Message;
import fr.gauthierth.messageriejava.server.objects.User;

import java.util.ArrayList;

/**
 * This class interprets the text command received by clients and send response events.
 * A command is a text message (which may contains newlines) with this structure: CommandName Arg1 Arg2 ... ArgN
 */
public class CommandInterpreter {

    ChatManager chatManager;

    public CommandInterpreter(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public String executeCommand(String userUuid, String command) { // Find the name of the command of an User UUID:
        String commandName, rest;
        String[] parts = command.split(" ", 2);
        commandName = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";

        User user = this.chatManager.findUserByUuid(userUuid);

        // We send the arguments to the corresponding function and return a response command:
        if (commandName.equalsIgnoreCase("channel"))
            return this.channel(user, rest);
        else if (commandName.equalsIgnoreCase("message"))
            return this.message(user, rest);
        else if (commandName.equalsIgnoreCase("username"))
            return this.username(user, rest);
        else
            return "error Unknown command";
    }

    private String channel(User user, String args) { // Function that manages the channel commands.
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("connect")) { // Subcommand that manages connection.
            Channel channel = this.chatManager.connectToChannel(user, rest);
            if (channel != null)
                return "channel connected " + channel.getUuid();
            else
                return "error Unknown channel";
        }
        else if (subCommand.equalsIgnoreCase("disconnect")) { // Subcommand that manages disconnection.
            Channel channel = this.chatManager.userDisconnect(user.getUuid());
            if (channel != null)
                return "channel disconnected " + channel.getUuid();
            else
                return "error Not connected to channel";
        }
        else if (subCommand.equalsIgnoreCase("get")) { // Subcommand which send channel informations.
            Channel channel = this.chatManager.findChannelByUuid(rest);
            if (channel != null)
                return "channel infos " + channel.getUuid() + " " + channel.getUsersConnected().size() + " " + channel.getName();
            else
                return "error Unknown channel";
        }
        else if (subCommand.equalsIgnoreCase("create")) { // Subcommand that manages the creation of channels.
            Channel channel = this.chatManager.createChannel(user);
            if (rest.length() > 0)
                channel.setName(rest);
            return "channel created " + channel.getUuid();
        }
        else if (subCommand.equalsIgnoreCase("list")) { // Subcommand which send channels list.
            return channelListedToCommand(this.chatManager.getChannels());
        }
        else if (subCommand.equalsIgnoreCase("users")) { // Subcommand which send users in a channel.
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

    private String message(User user, String args) { // Function that manages the message commands.
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("create")) { // Subcommand that manages the creation of messages.
            Message message = this.chatManager.sendMessage(user, rest);
            if (message != null)
                return messageToCommand(message);
            else
                return "error Not connected to channel";
        }
        else
            return "error Unknown subcommand";
    }

    private String username(User user, String args) { // Function that manages the username command.
        if (args.length() > 0)
            user.setName(args.replaceAll("\\s+", ""));
        return "";
    }

    public static String userJoinToCommand(User user) { // Get the corresponding text command to this event.
        return "channel join " + user.getUuid() + " " + user.getLoggingDate().getTime() + " " + user.getName();
    }
    public static String userLeaveToCommand(User user) { // Get the corresponding text command to this event.
        return "channel leave " + user.getUuid();
    }
    public static String messageToCommand(Message message) { // Get the corresponding text command to this event.
        return "message created " + message.getAuthor().getUuid() + " " + message.getContent();
    }
    public static String channelListedToCommand(ArrayList<Channel> channels)  { // Get the corresponding text command to this event.
        String result = "";
        for (Channel channel : channels) {
            result += "\n" + channel.getUuid() + " " + channel.getUsersConnected().size() + " " + channel.getName();
        }
        return ("channel listed " + result.trim()).trim();
    }

}
