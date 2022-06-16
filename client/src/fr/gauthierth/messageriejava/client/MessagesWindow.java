package fr.gauthierth.messageriejava.client;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MessagesWindow {
    private JPanel root;
    private JTextField textField;
    private JLabel titleLabel;
    private JButton sendButton;
    private JPanel messagesPane;
    private JScrollPane scrollPane;

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

        this.messagesPane.setLayout(new BoxLayout(this.messagesPane, BoxLayout.Y_AXIS));

        sendButton.addActionListener(e -> {
            String message = textField.getText();
            if (message.length() > 0)
                this.commandInterpreter.sendMessage(message);
            textField.setText("");
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 10) {
                    String message = textField.getText();
                    if (message.length() > 0)
                        MessagesWindow.this.commandInterpreter.sendMessage(message);
                    textField.setText("");
                }
            }
        });
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
        this.addLabel("<html><font color='#" + user.getHexColor() + "'>[" + user.getDisplayName() + "] <font color='#00b02c'>a rejoint le salon.</font></html>");
    }
    public void userLeave(User user) {
        this.addLabel("<html><font color='#" + user.getHexColor() + "'>[" + user.getDisplayName() + "] <font color='#ff1f1f'>a quitté le salon.</font></html>");
    }

    public void newMessage(Message message) {
        this.addLabel("<html><font color='#" + message.getAuthor().getHexColor() + "'>[" + message.getAuthor().getDisplayName() + "]</font> " + message.getContent() + "</html>");
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        this.messagesPane.add(label);
        this.messagesPane.revalidate();
        this.messagesPane.repaint();
    }

}
