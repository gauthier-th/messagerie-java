import javax.swing.*;

public class MessagesWindow {
    private JPanel root;

    private String uuid;
    private MessagesCommandInterpreter commandInterpreter;
    private Channel channel = null;

    public JPanel getRoot() {
        return this.root;
    }
    public String getUuid() { return this.uuid; }

    MessagesWindow(String uuid) {
        this.uuid = uuid;

        this.commandInterpreter = new MessagesCommandInterpreter(this);
        SocketManager.getInstance().setCommandInterpreter(this.commandInterpreter);
        this.commandInterpreter.reloadChannel();
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;

        JFrame messagesWindowFrame = (JFrame) SwingUtilities.getRoot(this.root);
        messagesWindowFrame.setTitle("Discussion - " + channel.getDisplayName());
    }

    public void quitChannel() {
        this.commandInterpreter.quitChannel();
    }

}
