package fr.gauthierth.messageriejava.client.ui;

import fr.gauthierth.messageriejava.client.ConfigSaver;
import fr.gauthierth.messageriejava.client.socket.SocketManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class is for the Main Window.
 */
public class MainWindow {

    private static MainWindow instance; // For the Singleton
    private JPanel root;
    private JTextField textFieldAddress;
    private JTextField textFieldPort;
    private JTextField textFieldUsername;
    private JButton buttonConnect;
    private ConfigSaver configSaver = new ConfigSaver("config.txt");

    public MainWindow() {
        MainWindow.instance = this;
        // We load the previous values from the config:
        configSaver.load();
        textFieldAddress.setText(configSaver.getHost());
        textFieldPort.setText(String.valueOf(configSaver.getPort()));
        textFieldUsername.setText(configSaver.getUsername());

        buttonConnect.addActionListener(e -> { // When click on the connect button:
            MainWindow.this.connect();
        });
        textFieldUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                // When Enter key is pressed:
                if (e.getKeyCode() == 10)
                    MainWindow.this.connect();
            }
        });
        textFieldUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                // We prevent spaces characters:
                if (String.valueOf(e.getKeyChar()).replaceAll("\\s+", "").length() == 0)
                    textFieldUsername.setText(textFieldUsername.getText().replaceAll("\\s+", ""));
            }
        });
    }

    public static MainWindow getInstance() {
        return MainWindow.instance;
    }

    public JPanel getRoot() {
        return this.root;
    }

    public void connect() {
        try {
            // We get the values from the UI:
            String address = textFieldAddress.getText();
            String portText = textFieldPort.getText();
            String username = textFieldUsername.getText();
            int port = Integer.parseInt(portText, 10);

            // Save the values in the config:
            configSaver.setHost(address);
            configSaver.setPort(port);
            configSaver.setUsername(username);
            configSaver.save();

            if (address.length() > 0 && port > 0) {
                // We connect to the server with the Socket Manager:
                SocketManager.startManager(address, port, username);
                SocketManager.getInstance().setConnectedCallback(() -> {
                    // We hide the Main Window:
                    JFrame mainWindowFrame = (JFrame) SwingUtilities.getRoot(MainWindow.this.root);
                    mainWindowFrame.setVisible(false);
                    // And show the Channels Windows:
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

    public static void main(String[] args) {
        // We create the Main Window:
        JFrame frame = new JFrame("Messagerie Java");
        frame.setContentPane(new MainWindow().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
