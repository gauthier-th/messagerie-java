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

        this.messagesPanel.setLayout(new BoxLayout(this.messagesPanel, BoxLayout.Y_AXIS));
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
        titleLabel.setText("Salon " + channel.getDisplayName());
        this.commandInterpreter.reloadUsers();
        this.addLabel("<html><font color='#888'>[info] Connexion au salon réussie.</font></html>");
    }

    public void quitChannel() {
        this.commandInterpreter.leaveChannel();
    }

    public void updateUsers() {
        if (channel.getUsersConnected().size() <= 1)
            this.addLabel("<html><font color='#888'>[info] 1 utilisateur dans le salon.</font></html>");
        else
            this.addLabel("<html><font color='#888'>[info] " + channel.getUsersConnected().size() + " utilisateurs dans le salon.</font></html>");
    }

    public void userJoin(User user) {
        this.addLabel("<html><font color='#00b02c'>[" + user.getDisplayName() + "] a rejoint le salon.</font></html>");
    }
    public void userLeave(User user) {
        this.addLabel("<html><font color='#ff1f1f'>[" + user.getDisplayName() + "] a quitté le salon.</font></html>");
    }

    public void newMessage(Message message) {

    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        messagesPanel.add(label);
    }

}
