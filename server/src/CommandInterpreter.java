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
        else if (subCommand.equalsIgnoreCase("create")) {
            Channel channel = this.chatManager.createChannel(user);
            return "channel created " + channel.getUuid();
        }
        else if (subCommand.equalsIgnoreCase("list")) {
            ArrayList<Channel> channels = this.chatManager.getChannels();
            String result = "channel listed";
            for (Channel channel : channels) {
                result += "\n" + channel.getUuid() + " " + channel.getName() + " " + channel.getUsersConnected().size();
            }
            return result.trim();
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
            parts = rest.split(" ", 2);
            String channelUuid = parts[0];
            String content = parts[1];
            Message message = this.chatManager.sendMessage(user, channelUuid, content);
            return messageToCommand(message);
        }
        else
            return "error Unknown subcommand";
    }

    public static String messageToCommand(Message message) {
        return "message created " + message.getAuthor().getUuid() + " " + message.getContent();
    }

}
