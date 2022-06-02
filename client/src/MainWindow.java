import javax.swing.*;

public class MainWindow {

    private JPanel root;
    private JTextField textFieldAddress;
    private JTextField textFieldPort;
    private JButton buttonConnect;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
