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
 * @version 3.00, March 2019.
*/
public class PlayModeView extends JPanel{

  ArrayList<Note> notes = new ArrayList<Note>();
  static int frame;

  // Setup background animation values
  static int backgroundFrameCount = 1;
  static int backgroundFrameDelay = 100;

  // Variables to display the current multiplier
  String currentMultiplier = "0";
  JLabel multiplierLabel = new JLabel("");
  boolean multiplierLabelInitialised = false;

  // Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];

  // Variables to display cover art for the song
  String coverArtPath;
  File coverArt;
  ImageIcon resizedCovertArt;

  // Variables to display the currency
  String currencyPath = "0";
  JLabel currencyLabel = new JLabel("");
  boolean currencyLabelInitialised = false;



  // Used to check whether a note has passed the screen and points to the note that needs to be played
  boolean dropNote = false;
  boolean collected = true;
  String currentNotePointer;

  // Variables to display score
  int score = 0;
  JLabel scoreLabel = new JLabel("0");
  boolean scoreLabelInitialised = false;



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

  public void resizeCoverArt() {

    System.out.println("resized image correctly");

    Image image = null;

    try {
      image = ImageIO.read(this.coverArt);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Image resizedImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

    this.resizedCovertArt = new ImageIcon(resizedImage);
  }

  /**
   * addNote
   * @param note the note to be added to the highway, passed from the model
   */
  public void addNote(Note note){
    notes.add(note);
  }

  public void initialiseJLabel(JLabel label, String path, Rectangle rect, boolean initialiseVariable) {
    label.setText(path);
    label.setBounds(rect);
    initialiseVariable = true;
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
    if (resizedCovertArt == null) { resizeCoverArt(); }
    JLabel coverArtLabel = new JLabel(resizedCovertArt);

    Rectangle rect = new Rectangle(50, 50, 150, 150);
    coverArtLabel.setBounds(rect);
    add(coverArtLabel);

    //Display multiplier on screen
    if (multiplierLabelInitialised == false) {
      if (!(currentMultiplier.equals("0"))) {
        multiplierLabel = new JLabel(new ImageIcon(currentMultiplier));
        Rectangle multRect = new Rectangle(75,225,100,100);
        initialiseJLabel(multiplierLabel, currentMultiplier, multRect, multiplierLabelInitialised);

      } else {
        multiplierLabel = new JLabel(currentMultiplier);
      }
      add(multiplierLabel);
      multiplierLabelInitialised = true;
    }

    if (!(multiplierLabel.getText().equals(currentMultiplier))) {

      remove(multiplierLabel);

      if (!(currentMultiplier.equals("0"))) {
        multiplierLabel = new JLabel(new ImageIcon(currentMultiplier));
        Rectangle multRect = new Rectangle(75,225,100,100);

        initialiseJLabel(multiplierLabel, currentMultiplier, multRect, multiplierLabelInitialised);

      } else {
        multiplierLabel = new JLabel(currentMultiplier);
      }

      add(multiplierLabel);
    }



    // Display currency on screen
    if(!currencyLabelInitialised) {
      if(!currencyPath.equals("0")) {
        currencyLabel = new JLabel(new ImageIcon(currencyPath));
        Rectangle currRect = new Rectangle(175, 300, 140, 30);
        initialiseJLabel(currencyLabel, currencyPath, currRect, currencyLabelInitialised);
      }
      else {
        currencyLabel = new JLabel(currencyPath);
      }
      add(currencyLabel);
      currencyLabelInitialised = true;
    }

    if (!(currencyLabel.getText().equals(currencyPath))) {

      remove(currencyLabel);

      if (!(currencyPath.equals("0"))) {
        currencyLabel = new JLabel(new ImageIcon(currencyPath));
        Rectangle currRect = new Rectangle(175, 300, 140, 30);
        initialiseJLabel(currencyLabel, currencyPath, currRect, currencyLabelInitialised);

      } else {
        currencyLabel = new JLabel(currencyPath);
      }

      add(currencyLabel);
    }





    //Display score on screen
    if (scoreLabelInitialised == false) {
      scoreLabel.setFont(new Font("Serif", Font.BOLD, 32));
      scoreLabel.setBounds(75,350, 100, 100);
      scoreLabel.setForeground(Color.white);
      add(scoreLabel);
      scoreLabelInitialised = true;
    }



    //Update score label if necessary
    if (!(scoreLabel.getText().equals(Integer.toString(this.score)))) {
      remove(scoreLabel);
      scoreLabel = new JLabel(Integer.toString(this.score));
      scoreLabel.setFont(new Font("Serif", Font.BOLD, 32));
      scoreLabel.setBounds(75,350, 100, 100);
      scoreLabel.setForeground(Color.white);
      add(scoreLabel);
    }




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
