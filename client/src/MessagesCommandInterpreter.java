import java.sql.Date;
import java.util.ArrayList;

public class MessagesCommandInterpreter implements CommandInterpreter {

    MessagesWindow messagesWindow;

    MessagesCommandInterpreter(MessagesWindow messagesWindow) {
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

    public void reloadChannel() {
        SocketManager.getInstance().sendMessage("channel get " + this.messagesWindow.getUuid());
    }

    public void quitChannel() {
        SocketManager.getInstance().sendMessage("channel disconnect " + this.messagesWindow.getUuid());
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
            String[] items = rest.split(" ");
            Channel channel = new Channel(items[0]);
            if (!items[1].equalsIgnoreCase("null"))
                channel.setName(items[1]);
            channel.setUsersConnectedCount(Integer.parseInt(items[2]));
            this.messagesWindow.updateChannel(channel);
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
        else if (subCommand.equalsIgnoreCase("userlist")) {
            String[] channelTexts = rest.split("\n");
            Channel channel = this.messagesWindow.getChannel();
            if (rest.length() > 0) {
                channel.getUsersConnected().clear();
                for (String channelText : channelTexts) {
                    String[] items = channelText.split(" ");
                    User user = new User(items[0]);
                    if (!items[1].equalsIgnoreCase("null"))
                        user.setName(items[1]);
                    user.setLoggingDate(new Date(Long.parseLong(items[2])));
                    channel.userConnect(user);
                }
            }
            this.messagesWindow.updateUsers();
        }
    }

}
