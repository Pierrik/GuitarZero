//import static com.sun.tools.doclint.Entity.ge;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;


/**
 * PlayModeView
 *
 * @author Harper Ford
 * @author Tom Mansfield
 * @author Kamila Hoffmann-Derlacka
 * @version 3.2, March 2019.
*/
public class PlayModeView extends JPanel{

  ArrayList<Note> notes = new ArrayList<Note>();
  static int frame;

  // Setup background animation values
  static int backgroundFrameCount = 1;
  static int backgroundFrameDelay = 100;

  // Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];

  // Used to check whether a note has passed the screen and points to the note that needs to be played
  boolean dropNote = false;
  boolean collected = true;
  String currentNotePointer;

  public PlayModeView(){
    frame = 0;
    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File("../assets/bg" + i + ".bmp"));
      }


    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  /**
   * addNote
   * @param note the note to be added to the highway, passed from the model
   */
  public void addNote(Note note){
    notes.add(note);
  }

  /**
    * paintComponent
    * Draws all necessary GUI elements on the JPanel
    * Also checks whether a note is in the correct region to be played
    * @param g: The the graphics object associated with the JPanel
    */
  @Override
  public synchronized void paintComponent(Graphics g) {

    super.paintComponent(g);
    //Draw the background animation frame depending on the current frame/10%(number of frames in the animation)
    g.drawImage(this.bg[((frame/this.backgroundFrameDelay)%this.backgroundFrameCount)], 0, 0,null);
    int len = notes.size();

    for(int i=len-1; i>-1; i--){
      // Draw the note
      notes.get(i).paintComponent(g);

        // Remove from the notes ArrayList as no longer needed
      if(notes.get(i).getY() > 500 || notes.get(i).collected)
        notes.remove(i);
      }

    frame++;
    }

  }

