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
 * @version 2.0, February 2019.
 *
 */
public class StoreManagerView extends JFrame {

  static File titleFile = null;
  static File coverArtFile = null;
  static File musicFile = null;

  /**
   * Setups up the JFrame
   * @param storeManagerModel
   * @param storeManagercontroller
   */
  public StoreManagerView ( StoreManagerController storeManagercontroller) {
    setTitle("Store Manager");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(300, 200);

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


    //Creating the three sub-top panels to the top panel
    JPanel topPanel = new JPanel(new BorderLayout());

    JPanel topFirstPanel = panelCreator(titleLabel, titleField, titleBrowse);
    JPanel topSecondPanel = panelCreator(coverArtLabel, coverArtField, coverArtBrowse);
    JPanel topThirdPanel = panelCreator(musicLabel, musicField, musicBrowse);

    topPanel.add(BorderLayout.NORTH, topFirstPanel);
    topPanel.add(BorderLayout.CENTER, topSecondPanel);
    topPanel.add(BorderLayout.SOUTH, topThirdPanel);


    //Creating the bottom panel and adding the 'save' button
    JPanel bottomPanel = new JPanel();
    JButton save = new JButton("Save");
    bottomPanel.add(save);


    //Adding the main top and bottom panels to the frame
    getContentPane().add(BorderLayout.NORTH, topPanel);
    getContentPane().add(BorderLayout.SOUTH, bottomPanel);


    titleBrowse.addActionListener(storeManagercontroller);

    /*
    titleBrowse.addActionListener(new ActionListener() {
      @Override

      public void actionPerformed(ActionEvent e) {
        File file = StoreManagerModel.fileFinder();
        String filePath = file.getPath();
        titleField.setText(filePath);
        titleFile = file;
      }
    });
    */

    coverArtBrowse.addActionListener(new ActionListener() {
      @Override
      /**
       * Listens for button press, sets coverArtFile
       * @param e: The event that triggered the function
       */
      public void actionPerformed(ActionEvent e) {
        File file = StoreManagerModel.fileFinder();
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
        File file = StoreManagerModel.fileFinder();
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
            String bundlePath = StoreManagerModel.bundleZipper(titleFile, coverArtFile, musicFile);
            String previewPath = StoreManagerModel.previewZipper(titleFile, coverArtFile);
            StoreManagerModel.sendFileToServer(bundlePath, "UPLOAD_BUNDLE");
            StoreManagerModel.sendFileToServer(previewPath, "UPLOAD_PREVIEW");
          }
        } catch (IOException e1) {
          e1.printStackTrace();
        }

      }
    });


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


}