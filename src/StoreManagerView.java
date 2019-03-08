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
 * StoreManagerView.
 *
 * @author  Pierrik Mellab
 * @version 2.3, March 2019
 *
 */
public class StoreManagerView extends JFrame {

  static File titleFile = null;
  static File coverArtFile = null;
  static File musicFile = null;

  static JTextField titleField = new JTextField(10);
  static JTextField coverArtField = new JTextField(10);
  static JTextField musicField = new JTextField(10);

  /**
   * Sets up the JFrame.
   * @param storeManagercontroller1: Title browse controller
   * @param storeManagerController2: Cover browse controller
   * @param storeManagerController3: Midi browse controller
   * @param storeManagerController4: Save button controller
   */
  public StoreManagerView ( StoreManagerController1 storeManagercontroller1,
      StoreManagerController2 storeManagerController2,
      StoreManagerController3 storeManagerController3,
      StoreManagerController4 storeManagerController4) {

    setTitle("Store Manager");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(300, 200);

    // Creating components to populate the frame
    JLabel titleLabel = new JLabel("Title:       ");
    JLabel coverArtLabel = new JLabel("Cover art:");
    JLabel musicLabel = new JLabel("Music:     ");


    JButton titleBrowse = new JButton("Browse");
    JButton coverArtBrowse = new JButton("Browse");
    JButton musicBrowse = new JButton("Browse");


    // Creating the three sub-top panels to the top panel
    JPanel topPanel = new JPanel(new BorderLayout());

    JPanel topFirstPanel = panelCreator(titleLabel, titleField, titleBrowse);
    JPanel topSecondPanel = panelCreator(coverArtLabel, coverArtField, coverArtBrowse);
    JPanel topThirdPanel = panelCreator(musicLabel, musicField, musicBrowse);

    topPanel.add(BorderLayout.NORTH, topFirstPanel);
    topPanel.add(BorderLayout.CENTER, topSecondPanel);
    topPanel.add(BorderLayout.SOUTH, topThirdPanel);


    // Creating the bottom panel and adding the 'save' button
    JPanel bottomPanel = new JPanel();
    JButton save = new JButton("Save");
    bottomPanel.add(save);


    // Adding the main top and bottom panels to the frame
    getContentPane().add(BorderLayout.NORTH, topPanel);
    getContentPane().add(BorderLayout.SOUTH, bottomPanel);


    titleBrowse.addActionListener(storeManagercontroller1);
    coverArtBrowse.addActionListener(storeManagerController2);
    musicBrowse.addActionListener(storeManagerController3);
    save.addActionListener(storeManagerController4);

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


  public static void setTitleTitle (String filePath) { titleField.setText(filePath); }

  public static void setCoverArtTitle (String filePath) { coverArtField.setText(filePath); }

  public static void setMusicTitle (String filePath) { musicField.setText(filePath); }


}