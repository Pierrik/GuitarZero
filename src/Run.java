import javax.sound.midi.Soundbank;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
import javax.swing.SpringLayout.Constraints;
//Run//
/**
 * Main Game Call.
 *
 *
 *   Linux/Mac:
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac *.java
 *   $ java Run
 *
 *   Windows:
 *   set CLASSPATH=jinput-2.0.9.jar;.
 *   javac Run.java
 *   java Run
 *
 *
 * @author Harper Ford
 * @author Kamila Hoffmann-Derlacka
 * @author Pierrik Mellab
 * @version 2.0, March 2019.
 */
public class Run {

  private static String OS = System.getProperty("os.name").toLowerCase();

  public static JFrame window;
  static SelectMode   se = null;
  static SlashMode    sl = null;
  static StoreMode    st = null;
  static TutorialMode tu = null;
  static PlayMode     p  = null;

  static String currentBundleDir = "../testBundle";

  // Screen Dimensions
  private static final int GAME_WIDTH  = 1000;
  private static final int GAME_HEIGHT = 563;

  public static JFrame getWindow(){ return window; }

  public static void main(String[] args){
    window = new JFrame("Guitar Zero Game");

    Dimension dims = new Dimension(GAME_WIDTH,GAME_HEIGHT);

    // Game begins in Slash Mode
    sl = new SlashMode();
    window.setContentPane(sl);

    //Set the content to the drawings from the GamePanel object
    window.setPreferredSize(dims);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setResizable(false);
    window.setVisible(true);
  }

  /**
   * Changes which MVC to display in the JFrame
   * @param option: Mode to change to
   */
  public static void changeMode(Mode option) {
    switch (option) {

      // Initialise slash mode - set bounds and display
      case SLASH:
        sl = new SlashMode();
        sl.setBounds(500, 500, 150,150);
        window.setContentPane(sl);
        window.setVisible(true);
        break;

      // Initialise store mode - set bounds and display
      case STORE:
        st = new StoreMode();
        st.setBounds(500, 500, 150,150);
        window.setContentPane(st);
        window.setVisible(true);
        break;

        // Attempt to set up Select Mode, display on screen
      case SELECT:
          try {
            se = new SelectMode();
          } catch (NoSongsException e){
            e.printStackTrace();
          }
        window.setContentPane(se);
        window.setVisible(true);
        break;

        // Attempt to set up play mode
        // Display if no errors occur when initialising
      case PLAY:
        p = new PlayMode(currentBundleDir);
        // Don't open if there are errors when setting up play mode
        if (p.model.errors > 0){
          break;
        }
        window.setContentPane(p);
        window.setVisible(true);

        // Start play mode thread
        Thread t = new Thread(p);
        t.start();
        break;

      // Attempt to set up Select Mode, display on screen
      case TUTORIAL:
        try {
          tu = new TutorialMode();
        } catch (NoPictureException e) {
          e.printStackTrace();
        }
        window.setContentPane(tu);
        window.setVisible(true);
        break;

        // If exit selected, end the game
      case EXIT:
        System.out.println("Exiting..");
        System.exit(0);

      default:
        break;
    }
  }

  // Find the operating system the game is bing ran on
  public static char OSvalidator() {
    if (OS.indexOf("win") >= 0)
      return 'w';
    else if (OS.indexOf("mac") >= 0)
      return 'm';
    else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
      return 'u';
    else {
      System.out.println("OS not recognised.");
      return 'q';
    }
  }
}