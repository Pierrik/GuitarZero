import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StoreManager extends JFrame {

    File tester = null;

    public StoreManager() {
        setTitle("Store Manager");
        setContentPane(new JLabel(new ImageIcon("background.png")));
        //setLayout( null );

    }

    public static File fileFinder () {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
            return selectedFile;
        } else {
            return null;
        }
    }


    public static JPanel panelCreator (JLabel label, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(BorderLayout.LINE_START, label);
        panel.add(BorderLayout.CENTER, textField);
        panel.add(BorderLayout.LINE_END, button);
        return panel;
    }


    public static void main(String args[]) {
        JFrame frame = new JFrame("Store Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JLabel titleLabel = new JLabel("Title:       ");
        JLabel coverArtLabel = new JLabel("Cover art:");
        JLabel musicLabel = new JLabel("Music:     ");

        JTextField titleField = new JTextField(10); // accepts upto 10 characters
        JTextField coverArtField = new JTextField(10); // accepts upto 10 characters
        JTextField musicField = new JTextField(10); // accepts upto 10 characters

        JButton titleBrowse = new JButton("Browse");
        JButton coverArtBrowse = new JButton("Browse");
        JButton musicBrowse = new JButton("Browse");


        //Creating the top panels and adding components
        JPanel topPanel = new JPanel(new BorderLayout()); // the panel is not visible in output

        JPanel topFirstPanel = panelCreator(titleLabel, titleField, titleBrowse);
        JPanel topSecondPanel = panelCreator(coverArtLabel, coverArtField, coverArtBrowse);
        JPanel topThirdPanel = panelCreator(musicLabel, musicField, musicBrowse);

        topPanel.add(BorderLayout.NORTH, topFirstPanel);
        topPanel.add(BorderLayout.CENTER, topSecondPanel);
        topPanel.add(BorderLayout.SOUTH, topThirdPanel);


        //Creating the bottom panel and adding "save" button
        JPanel bottomPanel = new JPanel(); // the panel is not visible in output
        JButton save = new JButton("Save");
        bottomPanel.add(save);


        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, topPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);

        frame.setVisible(true);

        titleBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = fileFinder();
                String filePath = file.getPath();
                titleField.setText(filePath);
            }
        });

        coverArtBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = fileFinder();
                String filePath = file.getPath();
                coverArtField.setText(filePath);
            }
        });

        musicBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = fileFinder();
                String filePath = file.getPath();
                musicField.setText(filePath);
            }
        });


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                



            }
        });

    }
}
