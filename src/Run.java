import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
/**
 * Main Game Call.
 *
 * @author Harper Ford
 * @author Kamila Hoffmann-Derlacka
 * @version 1.1, March 2019.
*/
public class Run {

  private static String OS = System.getProperty("os.name").toLowerCase();

  public static void main(String[] args){
    JFrame window = new JFrame("GZ");
    //SlashMode sl = new SlashMode();
    //StoreMode st  = new StoreMode();
    PlayMode p = new PlayMode("C:\\Users\\tomma\\Documents\\GuitarZero\\assets\\testBundle");
    Dimension dims = new Dimension(1000,500);
    //Set the content to the drawings from the GamePanel object
    window.setPreferredSize(dims);
    window.setContentPane(p);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
    p.run();
  }

  /*public void changeMode(String option){
    JPanel mode;

    switch(option){
      case "Slash": mode = sl;
      break;
      case "Store": mode = st;
      break;
      case "Select": mode = se;
      break;
      case "Play": mode = p;
      break;
      case "Tutorial": mode = t;
      break;
      default: window.close();
      break;
    }
    window.setContentPane(mode);

  }*/
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
