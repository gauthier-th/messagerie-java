package fr.gauthierth.messageriejava.client.socket;

import fr.gauthierth.messageriejava.client.ui.ChannelsWindow;
import fr.gauthierth.messageriejava.client.objects.Channel;

import java.util.ArrayList;

/**
 * This class parse and interprets the text command received by the server on the Channels Window.
 * A command is a text message (which may contains newlines) with this structure: CommandName Arg1 Arg2 ... ArgN
 */
public class ChannelsCommandInterpreter implements CommandInterpreter {

    ChannelsWindow channelsWindow;

    public ChannelsCommandInterpreter(ChannelsWindow channelsWindow) {
        this.channelsWindow = channelsWindow;
    }

    public String executeCommand(String command) { // Find the name of the command:
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

    public void onDisconnect() { // Disconnect callback
        this.channelsWindow.socketDisconnect();
    }

    // Various sending of command event:
    public void reloadChannels() {
        SocketManager.getInstance().sendMessage("channel list");
    }
    public void createChannel(String name) {
        SocketManager.getInstance().sendMessage("channel create " + name);
        this.reloadChannels();
    }
    public void joinChannel(String channelUuid) {
        SocketManager.getInstance().sendMessage("channel connect " + channelUuid);
    }

    private void channel(String args) { // Subcommand that manages the channels events.
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        if (parts.length > 1)
            rest = parts[1].trim();
        else
            rest = "";
        if (subCommand.equalsIgnoreCase("listed")) { // Subcommand which contains channels list.
            String[] channelTexts = rest.split("\n");
            ArrayList<Channel> channels = new ArrayList<>();
            if (rest.length() > 0) {
                for (String channelText : channelTexts) {
                    String[] items = channelText.split(" ", 3);
                    Channel channel = new Channel(items[0]);
                    channel.setUsersConnectedCount(Integer.parseInt(items[1]));
                    if (!items[2].equalsIgnoreCase("null"))
                        channel.setName(items[2]);
                    channels.add(channel);
                }
            }
            this.channelsWindow.updateChannels(channels);
        }
        else if (subCommand.equalsIgnoreCase("created")) { // Subcommand when a new channel is created.
            this.channelsWindow.joinChannel(rest);
        }
    }

}
