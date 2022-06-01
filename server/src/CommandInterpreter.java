public class CommandInterpreter {

    ChatManager chatManager;

    CommandInterpreter(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public String executeCommand(User user, String command) {
        String commandName, rest;
        String[] parts = command.split(" ", 2);
        commandName = parts[0].trim();
        rest = parts[1].trim();

        if (commandName.equalsIgnoreCase("channel"))
            return this.channel(user, rest);
        else
            return "error Unknown command";
    }

    private String channel(User user, String args) {
        String subCommand, rest;
        String[] parts = args.split(" ", 2);
        subCommand = parts[0].trim();
        rest = parts[1].trim();
        if (subCommand.equalsIgnoreCase("connect")) {
            Channel channel = this.chatManager.connectToChannel(user, rest);
            if (channel != null)
                return "channel connected " + channel.getUuid();
            else
                return "error Unknown channel";
        }
        else
            return "error Unknown subcommand";
    }

}
