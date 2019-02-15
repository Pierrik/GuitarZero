import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * Store Manager.
 *
 * @author  Pierrik Mellab
 * @author  John Mercer
 * @version 1.1, February 2019.
 */

public class StoreManager extends JFrame {

    static File titleFile = null;
    static File coverArtFile = null;
    static File musicFile = null;

    public StoreManager() {
        setTitle("Store Manager");
        setContentPane(new JLabel(new ImageIcon("background.png")));
    }

    public static File fileFinder () {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
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



    public static String textFileReader(File textFile) throws IOException {

        FileReader in = new FileReader(textFile);
        BufferedReader br = new BufferedReader(in);

        String text = br.readLine();
        in.close();

        return text;
    }

    public static String fileZipper (File titleFile, File coverArtFile, File musicFile) throws IOException {

        List<File> srcFiles =
                Arrays.asList(
                        coverArtFile, musicFile);          // files to be zipped go here

        String songName = textFileReader(titleFile);

        songName += ".zip";

        FileOutputStream fos = new FileOutputStream(songName);   // title goes here

        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (File srcFile : srcFiles) {
            FileInputStream fis = new FileInputStream(srcFile);
            ZipEntry zipEntry = new ZipEntry(srcFile.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();

        return songName;
    }

    public static void sendZipToServer(String filePath){
        MockClient client = new MockClient("localhost", 8888, filePath);
        client.sendFile();
    }


    public static void main(String args[]) {
        JFrame frame = new JFrame("Store Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        //Creating components to populate the frame
        JLabel titleLabel = new JLabel("Title:       ");
        JLabel coverArtLabel = new JLabel("Cover art:");
        JLabel musicLabel = new JLabel("Music:     ");

        JTextField titleField = new JTextField(10);
        JTextField coverArtField = new JTextField(10);
        JTextField musicField = new JTextField(10);

        JButton titleBrowse = new JButton("Browse");
        JButton coverArtBrowse = new JButton("Browse");
        JButton musicBrowse = new JButton("Browse");


        //Creating the top panels and adding the components
        JPanel topPanel = new JPanel(new BorderLayout()); // the panel is not visible in output

        JPanel topFirstPanel = panelCreator(titleLabel, titleField, titleBrowse);
        JPanel topSecondPanel = panelCreator(coverArtLabel, coverArtField, coverArtBrowse);
        JPanel topThirdPanel = panelCreator(musicLabel, musicField, musicBrowse);

        topPanel.add(BorderLayout.NORTH, topFirstPanel);
        topPanel.add(BorderLayout.CENTER, topSecondPanel);
        topPanel.add(BorderLayout.SOUTH, topThirdPanel);


        //Creating the bottom panel and adding the 'save' button
        JPanel bottomPanel = new JPanel(); // the panel is not visible in output
        JButton save = new JButton("Save");
        bottomPanel.add(save);


        //Adding panels to the frame
        frame.getContentPane().add(BorderLayout.NORTH, topPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);

        frame.setVisible(true);

        titleBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = fileFinder();
                String filePath = file.getPath();
                titleField.setText(filePath);
                titleFile = file;
            }
        });

        coverArtBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = fileFinder();
                String filePath = file.getPath();
                coverArtField.setText(filePath);
                coverArtFile = file;
            }
        });

        musicBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = fileFinder();
                String filePath = file.getPath();
                musicField.setText(filePath);
                musicFile = file;
            }
        });


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (titleFile != null && coverArtFile !=null && musicFile != null){
                        String zipFilePath = fileZipper(titleFile, coverArtFile, musicFile);
                        sendZipToServer(zipFilePath);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

    }
}
