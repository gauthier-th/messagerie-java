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
        return null;
    }

    public void reloadChannel() {
        SocketManager.getInstance().sendMessage("channel get " + this.messagesWindow.getUuid());
    }

    public void quitChannel() {
        SocketManager.getInstance().sendMessage("channel disconnect " + this.messagesWindow.getUuid());
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

}
