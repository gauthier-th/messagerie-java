package fr.gauthierth.messageriejava.client.ui;

import fr.gauthierth.messageriejava.client.objects.Channel;
import fr.gauthierth.messageriejava.client.objects.Message;
import fr.gauthierth.messageriejava.client.objects.User;
import fr.gauthierth.messageriejava.client.socket.MessagesCommandInterpreter;
import fr.gauthierth.messageriejava.client.socket.SocketManager;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class is for the Messages Window.
 */
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

        // We start the command interpreter for this window:
        this.commandInterpreter = new MessagesCommandInterpreter(this);
        SocketManager.getInstance().setCommandInterpreter(this.commandInterpreter);
        this.commandInterpreter.reloadChannel();

        this.messagesPane.setLayout(new BoxLayout(this.messagesPane, BoxLayout.Y_AXIS));

        sendButton.addActionListener(e -> { // When click on send message button:
            String message = textField.getText();
            if (message.length() > 0)
                this.commandInterpreter.sendMessage(message);
            textField.setText("");
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                // When Enter key is pressed:
                if (e.getKeyCode() == 10) {
                    String message = textField.getText();
                    if (message.length() > 0)
                        MessagesWindow.this.commandInterpreter.sendMessage(message);
                    textField.setText("");
                }
            }
        });
    }

    public void updateChannel(Channel channel) { // This function is executed once the user is connected to the Channel.
        this.channel = channel;
        titleLabel.setText("Salon " + channel.getDisplayName());
        this.commandInterpreter.reloadUsers();
        this.addLabel("<html><font color='#888'>[info] Connexion au salon réussie.</font></html>");
    }

    public void quitChannel() {
        this.commandInterpreter.leaveChannel();
    }

    public void updateUsers() { // We send the user count in the chat:
        if (channel.getUsersConnected().size() <= 1)
            this.addLabel("<html><font color='#888'>[info] 1 utilisateur dans le salon.</font></html>");
        else
            this.addLabel("<html><font color='#888'>[info] " + channel.getUsersConnected().size() + " utilisateurs dans le salon.</font></html>");
    }

    public void userJoin(User user) { // We send a message when a User join the Channel.
        this.addLabel("<html><font color='#" + user.getHexColor() + "'>[" + user.getDisplayName() + "] <font color='#00b02c'>a rejoint le salon.</font></html>");
    }
    public void userLeave(User user) { // We send a message when a User join the Channel.
        this.addLabel("<html><font color='#" + user.getHexColor() + "'>[" + user.getDisplayName() + "] <font color='#ff1f1f'>a quitté le salon.</font></html>");
    }

    public void newMessage(Message message) { // We send the message received in the Channel.
        this.addLabel("<html><font color='#" + message.getAuthor().getHexColor() + "'>[" + message.getAuthor().getDisplayName() + "]</font> " + message.getContent() + "</html>");
    }

    private void addLabel(String text) { // Add a new label in the chat.
        JLabel label = new JLabel(text);
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        this.messagesPane.add(label);
        this.messagesPane.revalidate();
        this.messagesPane.repaint();
    }

    public void socketDisconnect() { // Socket disconnect callback.
        // We open the Main Window and close the Messages one:
        JFrame channelsWindowFrame = (JFrame) SwingUtilities.getRoot(this.root);
        channelsWindowFrame.setVisible(false);
        JFrame mainWindowFrame = (JFrame) SwingUtilities.getRoot(MainWindow.getInstance().getRoot());
        mainWindowFrame.setVisible(true);
        JOptionPane.showMessageDialog(null, "La connexion avec le serveur a été interrompue.", "Déconnecté", JOptionPane.OK_OPTION);
    }

}
