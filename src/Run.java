import javax.sound.midi.Soundbank;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
/**
 * Main Game Call.
 *
 *
 *   Linux/Mac:
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac Run.java
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
 * @version 1.1, March 2019.
 */
public class Run {

  private static String OS = System.getProperty("os.name").toLowerCase();

  static JFrame window;
  static SelectMode se = null;
  static SlashMode sl = null;
  static StoreMode st = null;
  static PlayMode p = null;

  static String currentBundleDir = "../testBundle";


  public static void main(String[] args){
    window = new JFrame("Guitar Zero Game");

    Dimension dims = new Dimension(1000,500);

    sl = new SlashMode();
    window.setContentPane(sl);

    //Set the content to the drawings from the GamePanel object
    window.setPreferredSize(dims);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setResizable(false);
    window.setVisible(true);
  }

  /*
   * Changes which MVC to display in the JFrame
   * @param option: Mode to change to
   */
  public static void changeMode(Mode option) {
    JPanel mode = null;

    switch (option) {

      case SLASH:
        sl = new SlashMode();
        window.setContentPane(sl);
        window.setVisible(true);
        break;

      case STORE:
        StoreMode st = new StoreMode();
        window.setContentPane(st);
        window.setVisible(true);
        break;

      case SELECT:
          try {
            se = new SelectMode();
          } catch (NoSongsException e){
            e.printStackTrace();
          }
        window.setContentPane(se);
        window.setVisible(true);
        break;

      case PLAY:
        PlayMode d = new PlayMode(currentBundleDir);
        window.setContentPane(d);
        window.setVisible(true);

        Thread t = new Thread(d);
        t.start();
        break;

      case TUTORIAL:
        System.out.println("Tutorial mode is still under progress...");
        window.setVisible(true);
        break;

      case EXIT:
        System.out.println("Exiting..");
        System.exit(0);

      default:
        //window.close();
        break;
    }
  }


  public static char OSvalidator() {
    if (OS.indexOf("win") >= 0)
      return 'w';
    else if (OS.indexOf("mac") >= 0)
      return 'm';
    else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
      return 'u';
    else {
      System.out.println("OS not recognised.");
      // throw an exception??
      return 'q';
    }
  }
}