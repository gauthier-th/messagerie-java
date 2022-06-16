package fr.gauthierth.messageriejava.client.socket;

/**
 * This interface is used for ChannelsCommandInterpreter and MessagesCommandInterpreter.
 */
public interface CommandInterpreter {

    String executeCommand(String command);

    void onDisconnect();

}
