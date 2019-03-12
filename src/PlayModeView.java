import java.awt.Dimension;
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
 * @version 3.00, March 2019.
*/
public class PlayModeView extends JPanel{

  ArrayList<Note> notes = new ArrayList<Note>();
  static int frame;

  // Setup background animation values
  static int backgroundFrameCount = 1;
  static int backgroundFrameDelay = 100;

  String currentMultiplier;

  // Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];

  String coverArtPath;


  // Used to check whether a note has passed the screen and points to the note that needs to be played
  boolean dropNote = false;
  boolean collected = true;
  String currentNotePointer;

  //JLabel multiplierLabel;


  public PlayModeView(){
    frame = 0;
    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File("../assets/bg" + i + ".bmp"));
      }



      System.out.println("tester debug");

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

    // Display cover art on screen
    JLabel coverArtLabel = new JLabel(new ImageIcon(coverArtPath));
    coverArtLabel.setBounds(50, 50, 150, 150);
    add(coverArtLabel);

    //Display multiplier on screen
    JLabel  multiplierLabel = new JLabel(new ImageIcon(currentMultiplier));

    Dimension multDim = new Dimension(100, 100);

    multiplierLabel.setMaximumSize(multDim);

    multiplierLabel.setSize(multDim);

    multiplierLabel.setBounds(75, 225, 100, 100);
    add(multiplierLabel);

    for(int i=0; i<len; i++){
      // Draw the note
      notes.get(i).paintComponent(g);

      // If the note is in the region at the bottom of the screen
      // RANGE ONLY FOR TESTING COLLECTION OF NOTES, WILL CHANGE WHEN FRET BOARD IS ADDED
      if( notes.get(i).getY() > 350 && notes.get(i).getY() < 500 ) {
        currentNotePointer = notes.get(i).noteValue;
      }

      // If the note has passed the screen
      if(notes.get(i).getY() > 500){
        if(!collected) {
          // Note has not been played
          dropNote = true;
        }
        // Reset pointer, stop user from collecting same note multiple times
        currentNotePointer="";

        // Remove from the notes ArrayList as no longer needed
        notes.remove(i);
        i--;
        len--;
        collected = false;
      }
    }
    frame++;
  }
}
