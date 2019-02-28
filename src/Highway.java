import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.Dimension;
import java.lang.Thread;

/**
 * Main frame with background.
 *
 * @author  Harper Ford
 * @version 1.1, February 2019.
 *
 */
public class Highway extends JPanel {
  //Setup background animation values
  static int backgroundFrameCount = 1;
  static int backgroundFrameDelay = 10;
  //Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];
  /**
   * Setup background animation frames and JFrame
   */
  public Highway() {
    //Try to set the background frames to the corresponding .bmp images from ../assets
    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File("../assets/bg"+i+".bmp"));
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    //Create JFrame
    JFrame window = new JFrame("Guitar Zero");
    //Set size to background image dimensions
    window.setPreferredSize(new Dimension(bg[0].getWidth(null), bg[0].getHeight(null)));
    //Set the content to the drawings from the GamePanel object
    window.setContentPane(this);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
  }
  @Override
  /**
    * Draws all neccesary GUI elements on the JPanel
    * @param g: The the graphics object associated with the JPanel
    */
  public void paint(Graphics g, Int frame) {
    //Draw the background animation frame depending on the current frame/10%(number of frames in the animation)
    g.drawImage(this.bg[((frame/this.backgroundFrameDelay)%this.backgroundFrameCount)], 0, 0, null);
  }
}
