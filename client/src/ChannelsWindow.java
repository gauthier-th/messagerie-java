import javax.swing.*;

public class ChannelsWindow {

    private JPanel root;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ChannelsWindow");
        frame.setContentPane(new ChannelsWindow().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getRoot() {
        return this.root;
    }

}
