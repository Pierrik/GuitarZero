import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
/**
 * Main Game Call.
 *
 * @author Harper Ford
 * @version 1.00, March 2019.
*/
public class Run{
  public static void main(String[] args){
    JFrame window = new JFrame("GZ");
    PlayMode p = new PlayMode(window);
    //Set the content to the drawings from the GamePanel object
    window.setPreferredSize(new Dimension(1000,500));
    window.setContentPane(p);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);

  }
}
