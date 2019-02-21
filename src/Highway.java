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
 * Main JFrame with background.
 *
 * @author  Harper Ford
 * @version 1.1, February 2019.
 *
 */
public class Highway extends JPanel {
  //Setup background animation values
  static int backgroundFrameCount = 4;
  static int backgroundFrameDelay = 10;
  //Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];
  //Current frame counter
  static int frame = 0;
  //TEST NOTES --!!REMOVE!!--
  static Note[] notes = new Note[1];

  /**
   * Setsup background animation frames and JFrame
   * @param args: Any arguements that need passing
   */
  public static void main(String[] args) {
    //Try to set the background frames to the corresponding .bmp images from ../assets
    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File("../assets/bg"+i+".bmp"));
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    //Assign notes --!!REMOVE!!--
    notes[0] = new Note(true, 2);
    //notes[1] = new Note(false, 1);
    //Create JFrame
    JFrame window = new JFrame("Guitar Zero");
    //Set size to background image dimensions
    window.setPreferredSize(new Dimension(bg[0].getWidth(null), bg[0].getHeight(null)));
    //Set the content to the drawings from the GamePanel object
    window.setContentPane(new GamePanel());
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
  }

  /**
   * JPanel class that draws Note objects and a background image
   */
  static class GamePanel extends JPanel implements ActionListener, Runnable {

    //Create a Thread
    private Thread thread;
    private int fps = 60;

    //Create a timer to trigger an action listner every 1000ms/fps
    Timer timer=new Timer((1000/fps), this);


    public GamePanel(){
      if(thread == null){
        thread = new Thread(this);
        thread.start();
      }
    }

    /**
     * When the Thread starts start the Timer
     */
    public void run(){
      timer.start();
    }

    /**
     * When an action is triggered if its from the timer repaint the JPanel
     * @param ev: The event that triggered the function
     */
    public void actionPerformed(ActionEvent ev){
      if(ev.getSource()==timer){
        repaint();
      }
    }

    @Override
    /**
     * Draws all neccesary GUI elements on the JPanel
     * @param g: The the graphics object associated with the JPanel
     */
    public void paint(Graphics g) {
      //Increment frame count
      frame++;
      //Draw the background animation frame depending on the current frame/10%(number of frames in the animation)
      g.drawImage(bg[((frame/backgroundFrameDelay)%backgroundFrameCount)], 0, 0, null);
      //For each note object draw it
      for(Note n : notes){
        n.paintComponent(g);
      }
    }
  }
}
