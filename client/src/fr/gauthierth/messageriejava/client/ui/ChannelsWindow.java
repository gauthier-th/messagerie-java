package fr.gauthierth.messageriejava.client.ui;

import fr.gauthierth.messageriejava.client.objects.Channel;
import fr.gauthierth.messageriejava.client.socket.ChannelsCommandInterpreter;
import fr.gauthierth.messageriejava.client.socket.SocketManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * This class is for the Channels Window.
 */
public class ChannelsWindow {

    private JPanel root;
    private JPanel channelsPanel;
    private JButton createChannel;

    private ChannelsCommandInterpreter commandInterpreter;

    public JPanel getRoot() {
        return this.root;
    }

    public ChannelsWindow() {
        // We start the command interpreter for this window:
        this.commandInterpreter = new ChannelsCommandInterpreter(this);
        SocketManager.getInstance().setCommandInterpreter(this.commandInterpreter);
        this.commandInterpreter.reloadChannels();

        this.channelsPanel.setLayout(new BoxLayout(this.channelsPanel, BoxLayout.Y_AXIS));
        createChannel.addActionListener(e -> { // When click on create channel button:
            String name = JOptionPane.showInputDialog(null, "Entrez le nom du salon :", "Nouveau salon", JOptionPane.QUESTION_MESSAGE);
            if (name != null)
                ChannelsWindow.this.commandInterpreter.createChannel(name);
        });
    }

    public void updateChannels(ArrayList<Channel> channels) { // We update the channels list in the UI.
        System.out.println("update channels: " + channels.size());
        this.channelsPanel.removeAll(); // We remove all labels.
        if (channels.size() > 0) {
            // And we create a new one for each channel:
            for (Channel channel : channels) {
                JLabel label = new JLabel(channel.getDisplayName() + " (" + channel.getUsersConnectedCount() + " connecté" + (channel.getUsersConnectedCount() > 1 ? "s" : "") + ")");
                label.setAlignmentX(0.5f);
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                this.channelsPanel.add(label);

                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        ChannelsWindow.this.joinChannel(channel.getUuid());
                    }
                });
            }
        }
        else {
            JLabel label = new JLabel("Aucun salon disponible pour le moment.");
            label.setAlignmentX(0.5f);
            this.channelsPanel.add(label);
        }

        this.channelsPanel.revalidate();
        this.channelsPanel.repaint();
    }

    public void joinChannel(String channelUuid) { // When an User join a Channel:
        ChannelsWindow.this.commandInterpreter.joinChannel(channelUuid);

        JFrame channelsWindowFrame = (JFrame) SwingUtilities.getRoot(this.root);
        channelsWindowFrame.setVisible(false);

        // We open the Messages Window and hide the Channels Window:
        JFrame frame = new JFrame("Messagerie Java - Discussion");
        MessagesWindow messagesWindow = new MessagesWindow(channelUuid);
        frame.setContentPane(messagesWindow.getRoot());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // When the Messages Window is closed:
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // We show a confirmation:
                int reply = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter le salon ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (reply != JOptionPane.YES_OPTION)
                    return;

                // We close the Messages Window and show the Channels one.
                JFrame channelsWindowFrame = (JFrame) SwingUtilities.getRoot(ChannelsWindow.this.root);
                channelsWindowFrame.setVisible(true);
                SocketManager.getInstance().setCommandInterpreter(ChannelsWindow.this.commandInterpreter);
                messagesWindow.quitChannel();
                ChannelsWindow.this.commandInterpreter.reloadChannels();
            }
        });
    }

    public void socketDisconnect() { // Socket disconnect callback.
        // We open the Main Window and close the Channels one:
        JFrame mainWindowFrame = (JFrame) SwingUtilities.getRoot(MainWindow.getInstance().getRoot());
        mainWindowFrame.setVisible(true);
        JFrame channelsWindowFrame = (JFrame) SwingUtilities.getRoot(this.root);
        channelsWindowFrame.dispose();
        JOptionPane.showMessageDialog(null, "La connexion avec le serveur a été interrompue.", "Déconnecté", JOptionPane.OK_OPTION);
    }

}
