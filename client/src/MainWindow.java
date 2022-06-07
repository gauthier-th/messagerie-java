import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {

    private JPanel root;
    private JTextField textFieldAddress;
    private JTextField textFieldPort;
    private JButton buttonConnect;

    public MainWindow() {

        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String address = textFieldAddress.getText();
                    String portText = textFieldPort.getText();
                    int port = Integer.parseInt(portText, 10);
                    if (address.length() > 0 && port > 0) {
                        SocketManager.startManager(address, port);
                        SocketManager.getInstance().setConnectedCallback(() -> {
                            JFrame mainWindowFrame = (JFrame) SwingUtilities.getRoot(MainWindow.this.root);
                            mainWindowFrame.setVisible(false);

                            JFrame frame = new JFrame("Messagerie Java - Salons");
                            frame.setContentPane(new ChannelsWindow().getRoot());
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.pack();
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                        });
                    }
                }
                catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de la connexion au serveur.");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Messagerie Java");
        frame.setContentPane(new MainWindow().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
