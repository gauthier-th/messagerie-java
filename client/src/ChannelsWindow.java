import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ChannelsWindow {

    private JPanel root;
    private JPanel channelsPanel;
    private JButton createChannel;

    private ChannelsCommandInterpreter commandInterpreter;

    public JPanel getRoot() {
        return this.root;
    }

    public ChannelsWindow() {
        this.commandInterpreter = new ChannelsCommandInterpreter(this);
        SocketManager.getInstance().setCommandInterpreter(this.commandInterpreter);
        this.commandInterpreter.reloadChannels();
        this.channelsPanel.setLayout(new BoxLayout(this.channelsPanel, BoxLayout.Y_AXIS));
        createChannel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(null, "Entrez le nom du salon :", "Nouveau salon", JOptionPane.QUESTION_MESSAGE);
                if (name != null)
                    ChannelsWindow.this.commandInterpreter.createChannel(name);
            }
        });
    }

    public void updateChannels(ArrayList<Channel> channels) {
        this.channelsPanel.removeAll();
        if (channels.size() > 0) {
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

    public void joinChannel(String uuid) {
        System.out.println("Join channel " + uuid);
    }

}
