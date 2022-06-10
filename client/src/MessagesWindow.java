import javax.swing.*;

public class MessagesWindow {
    private JPanel root;
    private JTextField textField;
    private JLabel titleLabel;
    private JButton sendButton;
    private JPanel messagesPanel;

    private String channelUuid;
    private MessagesCommandInterpreter commandInterpreter;
    private Channel channel = null;

    public JPanel getRoot() {
        return this.root;
    }
    public String getChannelUuid() { return this.channelUuid; }
    public Channel getChannel() { return this.channel; }

    MessagesWindow(String uuid) {
        this.channelUuid = uuid;

        this.commandInterpreter = new MessagesCommandInterpreter(this);
        SocketManager.getInstance().setCommandInterpreter(this.commandInterpreter);
        this.commandInterpreter.reloadChannel();
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
        titleLabel.setText("Salon " + channel.getDisplayName());
        this.commandInterpreter.reloadUsers();
    }

    public void quitChannel() {
        this.commandInterpreter.quitChannel();
    }

    public void updateUsers() {
        for (User user : channel.getUsersConnected()) {
            System.out.println(user.getDisplayName());
        }
    }

    public void newMessage(Message message) {

    }

}
