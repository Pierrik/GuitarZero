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

/**
 * Store Manager.
 *
 * @author  Pierrik Mellab
 * @author  John Mercer
 * @author  Harper Ford (Javadoc)
 * @author  John Merer (Added bundle/preview functionality)
 * @version 1.5, March 2019.
 *
 */
public class StoreManager extends JFrame {

  // server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;

  static File titleFile = null;
  static File coverArtFile = null;
  static File musicFile = null;

  /**
   * Sets up the JFrame
   */
  public StoreManager() {
    setTitle("Store Manager");
    setContentPane(new JLabel(new ImageIcon("background.png")));
  }

  /**
   * Lets a user browse for a file on their computer
   * @return The selected file or null if file not approved
   */
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

  /**
   * Creates a JPanel
   * @param label:     A JLabel to add to the JPanel
   * @param textField: A JTextField to add to the JPanel
   * @param button:    A JButton to add to the JPanel
   * @return           The JPanel containing all the passed parameters
   */
  public static JPanel panelCreator (JLabel label, JTextField textField, JButton button) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(BorderLayout.LINE_START, label);
    panel.add(BorderLayout.CENTER, textField);
    panel.add(BorderLayout.LINE_END, button);
    return panel;
  }

  /**
   * Reads a text File
   * @param textFile:    The file to read
   * @return             The text from the given file
   * @throws IOException If the file is not found or corrupt
   */
  public static String textFileReader(File textFile) throws IOException {

    FileReader in = new FileReader(textFile);
    BufferedReader br = new BufferedReader(in);

    String text = br.readLine();
    in.close();

    return text;
  }

  /**
   * Creates a 'bundle' (zip file) from three specified files.
   * @param titleFile:    .TXT file containing a song title
   * @param coverArtFile: .PNG file containing a songs cover art
   * @param musicFile:    .MIDI file containing the songs
   * @return              The contents of titleFile
   * @throws IOException  In case any of the files are corrupt or can't be found
   */
  public static String bundleZipper (File titleFile, File coverArtFile, File musicFile) throws IOException {

    MidiToNotes.writeFile(musicFile.getPath());         // creates noteFile.txt in current directory
    File noteFile = new File("noteFile.txt");

    List<File> srcFiles = Arrays.asList(coverArtFile, musicFile, noteFile);          // files to be zipped go here

    String songName = textFileReader(titleFile);
    songName += "(bundle).zip";

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

  /**
   * Creates a preview (title and cover image) zip file from two specified files.
   * @param titleFile:    .TXT file containing a song title
   * @param coverArtFile: .PNG file containing a songs cover art
   * @return              The contents of titleFile
   * @throws IOException  In case any of the files are corrupt or can't be found
   */
  public static String previewZipper (File titleFile, File coverArtFile) throws IOException {

    String songName = textFileReader(titleFile);
    songName += "(preview).zip";

    FileOutputStream fos = new FileOutputStream(songName);
    ZipOutputStream zipOut = new ZipOutputStream(fos);
    FileInputStream fis = new FileInputStream(coverArtFile);

    ZipEntry zipEntry = new ZipEntry(coverArtFile.getName());
    zipOut.putNextEntry(zipEntry);

    byte[] bytes = new byte[1024];
    int length;
    while((length = fis.read(bytes)) >= 0) {
      zipOut.write(bytes, 0, length);
    }

    fis.close();
    zipOut.close();
    fos.close();

    return songName;
  }

  /**
   * Deletes specified file
   * @param filePath: File to delete
   */
  public static void deleteFile(String filePath){
    File file = new File(filePath);
    file.delete();
  }


  /**
   * Creates a JFrame then populates it with JPanels
   * @param args: Any arguments that need passing
   */
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
      /**
       * Listens for button press, sets titleFile
       * @param e: The event that triggered the function
       */
      public void actionPerformed(ActionEvent e) {
        File file = fileFinder();
        String filePath = file.getPath();
        titleField.setText(filePath);
        titleFile = file;
      }
    });

    coverArtBrowse.addActionListener(new ActionListener() {
      @Override
      /**
       * Listens for button press, sets coverArtFile
       * @param e: The event that triggered the function
       */
      public void actionPerformed(ActionEvent e) {
        File file = fileFinder();
        String filePath = file.getPath();
        coverArtField.setText(filePath);
        coverArtFile = file;
      }
    });

    musicBrowse.addActionListener(new ActionListener() {

      @Override
      /**
       * Listens for button press, sets musicFile
       * @param e: The event that triggered the function
       */
      public void actionPerformed(ActionEvent e) {
        File file = fileFinder();
        String filePath = file.getPath();
        musicField.setText(filePath);
        musicFile = file;
      }
    });


    save.addActionListener(new ActionListener() {
      @Override
      /**
       * Listens for button press, zips then sends titleFile, coverArtFile, musicFile to server
       * @param e: The event that triggered the function
       */
      public void actionPerformed(ActionEvent e) {
        try {
          if (titleFile != null && coverArtFile !=null && musicFile != null){
            // zipping files and sending to server
            String bundlePath = bundleZipper(titleFile, coverArtFile, musicFile);
            String previewPath = previewZipper(titleFile, coverArtFile);

            MockClient client = new MockClient(HOST, PORT);
            client.uploadFile(bundlePath, "UPLOAD_BUNDLE");
            client.uploadFile(previewPath, "UPLOAD_PREVIEW");

            // cleaning up (deleting zips and noteFile on client-side)
            deleteFile(bundlePath);
            deleteFile(previewPath);
            deleteFile("noteFile.txt");

          }
        } catch (IOException e1) {
          e1.printStackTrace();
        }

      }
    });

  }
}
