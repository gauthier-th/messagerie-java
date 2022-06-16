package fr.gauthierth.messageriejava.client.ui;

import fr.gauthierth.messageriejava.client.ConfigSaver;
import fr.gauthierth.messageriejava.client.socket.SocketManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow {

    private JPanel root;
    private JTextField textFieldAddress;
    private JTextField textFieldPort;
    private JTextField textFieldUsername;
    private JButton buttonConnect;
    private ConfigSaver configSaver = new ConfigSaver("config.txt");

    public MainWindow() {
        configSaver.load();
        textFieldAddress.setText(configSaver.getHost());
        textFieldPort.setText(String.valueOf(configSaver.getPort()));
        textFieldUsername.setText(configSaver.getUsername());

        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.this.connect();
            }
        });
        textFieldUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 10)
                    MainWindow.this.connect();
            }
        });
        textFieldUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (String.valueOf(e.getKeyChar()).replaceAll("\\s+", "").length() == 0)
                    textFieldUsername.setText(textFieldUsername.getText().replaceAll("\\s+", ""));
            }
        });
    }

    public void connect() {
        try {
            String address = textFieldAddress.getText();
            String portText = textFieldPort.getText();
            String username = textFieldUsername.getText();
            int port = Integer.parseInt(portText, 10);

            configSaver.setHost(address);
            configSaver.setPort(port);
            configSaver.setUsername(username);
            configSaver.save();

            if (address.length() > 0 && port > 0) {
                SocketManager.startManager(address, port, username);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Messagerie Java");
        frame.setContentPane(new MainWindow().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
