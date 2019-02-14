import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class StoreManager extends JFrame {

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
            return fileFinder();
        } else {
            return null;
        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Store Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        //Creating the top panels and adding components
        JPanel topPanel = new JPanel(new BorderLayout()); // the panel is not visible in output
        JPanel topFirstPanel = new JPanel(new BorderLayout()); // the panel is not visible in output
        JPanel topSecondPanel = new JPanel(new BorderLayout()); // the panel is not visible in output
        JPanel topThirdPanel = new JPanel(new BorderLayout()); // the panel is not visible in output


        JLabel titleLabel = new JLabel("Title:       ");
        JLabel coverArtLabel = new JLabel("Cover art:");
        JLabel musicLabel = new JLabel("Music:     ");

        JTextField titleField = new JTextField(10); // accepts upto 10 characters
        JTextField coverArtField = new JTextField(10); // accepts upto 10 characters
        JTextField musicField = new JTextField(10); // accepts upto 10 characters

        JButton titleBrowse = new JButton("Browse");
        JButton coverArtBrowse = new JButton("Browse");
        JButton musicBrowse = new JButton("Browse");


        topFirstPanel.add(BorderLayout.LINE_START, titleLabel);
        topFirstPanel.add(BorderLayout.CENTER, titleField);
        topFirstPanel.add(BorderLayout.LINE_END, titleBrowse);

        topSecondPanel.add(BorderLayout.LINE_START, coverArtLabel);
        topSecondPanel.add(BorderLayout.CENTER, coverArtField);
        topSecondPanel.add(BorderLayout.LINE_END, coverArtBrowse);

        topThirdPanel.add(BorderLayout.LINE_START, musicLabel);
        topThirdPanel.add(BorderLayout.CENTER, musicField);
        topThirdPanel.add(BorderLayout.LINE_END, musicBrowse);

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
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
                String filePath = file.getPath();
                titleField.setText(String.format(filePath));

                System.out.println("TESTER1234567890");
            }

        });

    }
}
