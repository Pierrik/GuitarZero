import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.util.ArrayList;

/**
 * Play Mode View.
 *
 * @author Harper Ford
 * @version 2.00, March 2019.
*/
public class PlayModeView extends JPanel{
  ArrayList<Note> notes = new ArrayList<Note>();
  static int frame;
  //Setup background animation values
  static int backgroundFrameCount = 1;
  static int backgroundFrameDelay = 100;
  //Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];
  public PlayModeView(){
    frame = 0;
    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File("../assets/bg"+i+".bmp"));
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }



  public void addNote(String note){
    notes.add(new Note(note));
  }

  /**
    * Draws all neccesary GUI elements on the JPanel
    * @param g: The the graphics object associated with the JPanel
    */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    //Draw the background animation frame depending on the current frame/10%(number of frames in the animation)
    g.drawImage(this.bg[((frame/this.backgroundFrameDelay)%this.backgroundFrameCount)], 0, 0,null);
    int len = notes.size();
    for(int i=0; i<len; i++){
      notes.get(i).paintComponent(g);
      if(notes.get(i).getY() > 500){
        notes.remove(i);
        i--;
        len--;
      }
    }
    frame++;
  }
}
