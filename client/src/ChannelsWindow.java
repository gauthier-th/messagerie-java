import javax.swing.*;
import java.util.ArrayList;

public class ChannelsWindow {

    private JPanel root;
    private JPanel channelsPanel;
    private JButton créerUnSalonButton;

    private ChannelsCommandInterpreter commandInterpreter;

    public JPanel getRoot() {
        return this.root;
    }

    public ChannelsWindow() {
        this.commandInterpreter = new ChannelsCommandInterpreter(this);
        SocketManager.getInstance().setCommandInterpreter(this.commandInterpreter);
        this.commandInterpreter.reloadChannels();
        this.channelsPanel.setLayout(new BoxLayout(this.channelsPanel, BoxLayout.Y_AXIS));
    }

    public void updateChannels(ArrayList<Channel> channels) {
        if (channels.size() > 0) {
            for (Channel channel : channels) {
                JLabel label = new JLabel(channel.getDisplayName());
                label.setAlignmentX(0.5f);
                this.channelsPanel.add(label);
            }
        }
        else {
            JLabel label = new JLabel("Aucun salon disponible pour le moment.");
            label.setAlignmentX(0.5f);
            this.channelsPanel.add(label);
        }
    }

}
