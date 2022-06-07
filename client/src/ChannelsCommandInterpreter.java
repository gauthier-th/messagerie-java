import java.util.ArrayList;

public class ChannelsCommandInterpreter implements CommandInterpreter {

    ChannelsWindow channelsWindow;

    ChannelsCommandInterpreter(ChannelsWindow channelsWindow) {
        this.channelsWindow = channelsWindow;
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

    public void reloadChannels() {
        SocketManager.getInstance().sendMessage("channel list");
    }

    public void createChannel(String name) {
        SocketManager.getInstance().sendMessage("channel create " + name);
        this.reloadChannels();
    }

    private void channel(String args) {
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("listed")) {
            String[] channelTexts = rest.split("\n");
            ArrayList<Channel> channels = new ArrayList<>();
            if (rest.length() > 0) {
                for (String channelText : channelTexts) {
                    String[] items = channelText.split(" ");
                    Channel channel = new Channel(items[0]);
                    if (!items[1].equalsIgnoreCase("null"))
                        channel.setName(items[1]);
                    channel.setUsersConnectedCount(Integer.parseInt(items[2]));
                    channels.add(channel);
                }
            }
            this.channelsWindow.updateChannels(channels);
        }
        else if (subCommand.equalsIgnoreCase("created")) {
            this.channelsWindow.joinChannel(rest);
        }
    }

}
