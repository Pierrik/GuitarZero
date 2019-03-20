//import static com.sun.tools.doclint.Entity.ge;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.Buffer;
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

  int width = 1000;
  int height = 563;

  // Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];

  // Used to check whether a note has passed the screen and points to the note that needs to be played
  boolean dropNote = false;
  boolean collected = true;
  String currentNotePointer;

  //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

  JLabel coverArtLabel;
  JLabel multiplierLabel;
  JLabel currencyLabel;
  JLabel scoreLabel;
  JLabel streakLabel;
  JLabel zeroPowerLabel;
  JLabel noteLabel;
  JLabel noteCollected;
  JLabel[] activeNotes = new JLabel[6];
  JLabel[] collectedNotes = new JLabel[6];



  public PlayModeView(){
    frame = 0;


    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File("../assets/Done/Highway.bmp"));
      }

    }
    catch(Exception e){
      e.printStackTrace();
    }

    this.setLayout(null);
  }

  public void setCoverArtLabel(File coverFile) {
    Image image = null;

    try {
      image = ImageIO.read(coverFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Image resizedImage = image.getScaledInstance(200, 200, Image.SCALE_DEFAULT);

    coverArtLabel = new JLabel(new ImageIcon(resizedImage));
    coverArtLabel.setBounds((int)(0.05*width), (int)(0.05*height), 200, 200);
    add(coverArtLabel);
  }

  // Sets a multiplier label
  public void setMultiplierLabel(String path) {
    multiplierLabel = new JLabel(new ImageIcon(path));
    multiplierLabel.setBounds((int)(0.15*width), (int)(0.45*height), 100, 100);
    multiplierLabel.setVisible(true);
    add(multiplierLabel);
  }

  // Sets a blank multiplier label when the multiplier is 1
  public void setMultiplierLabel() {
    multiplierLabel = new JLabel(new ImageIcon());
    multiplierLabel.setBounds((int)(0.15*width), (int)(0.45*height), 100, 100);
    multiplierLabel.setVisible(false);
    add(multiplierLabel);
  }

  public void changeMultiplierLabel(String path) {
    multiplierLabel.setIcon(new ImageIcon(path));
    multiplierLabel.setVisible(true);
  }


  public void setCurrencyLabel() {
    currencyLabel = new JLabel(new ImageIcon());
    currencyLabel.setBounds(200, 437, 140, 30);
    currencyLabel.setVisible(false);
    add(currencyLabel);
  }

  public void changeCurrencyLabel(String path) {
    currencyLabel.setIcon(new ImageIcon(path));
    currencyLabel.setVisible(true);
  }

  public void setZeroPowerLabelInit(String path) {
    zeroPowerLabel = new JLabel(new ImageIcon(path));
    zeroPowerLabel.setBounds((int)(0.726*width), (int)(0.25*height), 174, 192);
    //zeroPowerLabel.setVisible(false);
    //add(zeroPowerLabel);
  }

  public void setZeroPowerLabelVisible() {
    zeroPowerLabel.setVisible(true);
    add(zeroPowerLabel);
  }

  public void setZeroPowerLabelFalse() {
    zeroPowerLabel.setVisible(false);
  }

  public void setScoreLabel(Integer score) {
    scoreLabel = new JLabel("Score: " + Integer.toString(score));
    scoreLabel.setFont(new Font("Serif", Font.BOLD, 32));
    scoreLabel.setBounds(50, 400, 200, 100);
    scoreLabel.setForeground(Color.white);
    scoreLabel.setVisible(true);
    add(scoreLabel);
  }

  public void resetScoreLabel(Integer score) {
    scoreLabel.setText("Score: " + Integer.toString(score));
  }

  public void setStreakLabel() {
    streakLabel = new JLabel("Streak:  " + Integer.toString(0));
    streakLabel.setFont(new Font("Serif", Font.BOLD, 32));
    streakLabel.setBounds(440,28, 200, 200);
    streakLabel.setForeground(Color.white);
    add(streakLabel);
  }

  public void resetStreakLabel(Integer streak) {
    streakLabel.setText("Streak:  " + Integer.toString(streak));
  }

  public void noteInit() {

    // WHITE1 - 0 in controller
    noteLabel = new JLabel(new ImageIcon("../assets/Done/WhiteCollectLeft.png"));
    noteLabel.setBounds(300, 350, 100, 100);
    noteLabel.setVisible(false);
    activeNotes[0] = noteLabel;

    noteCollected = new JLabel(new ImageIcon("../assets/Done/WhiteLeft.png"));
    noteCollected.setBounds(300, 350, 100, 100);
    noteCollected.setVisible(false);
    collectedNotes[0] = noteCollected;

    //BLACK1 - 1 in controller
    noteLabel = new JLabel(new ImageIcon("../assets/Done/BlackCollectLeft.png"));
    noteLabel.setBounds(300, 350, 100, 100);
    noteLabel.setVisible(false);
    activeNotes[1] = noteLabel;

    noteCollected = new JLabel(new ImageIcon("../assets/Done/BlackLeft.png"));
    noteCollected.setBounds(300, 350, 100, 100);
    noteCollected.setVisible(false);
    collectedNotes[1] = noteCollected;

    //BLACK2 - 2 in controller
    noteLabel = new JLabel(new ImageIcon("../assets/Done/BlackCollectMiddle.png"));
    noteLabel.setBounds(450, 350, 100, 100);
    noteLabel.setVisible(false);
    activeNotes[2] = noteLabel;

    noteCollected = new JLabel(new ImageIcon("../assets/Done/BlackMiddle.png"));
    noteCollected.setBounds(450, 350, 100, 100);
    noteCollected.setVisible(false);
    collectedNotes[2] = noteCollected;

    //BLACK3 - 3 in controller
    noteLabel = new JLabel(new ImageIcon("../assets/Done/BlackCollectRight.png"));
    noteLabel.setBounds(575, 350, 100, 100);
    noteLabel.setVisible(false);
    activeNotes[3] = noteLabel;

    noteCollected = new JLabel(new ImageIcon("../assets/Done/BlackRight.png"));
    noteCollected.setBounds(575, 350, 100, 100);
    noteCollected.setVisible(false);
    collectedNotes[3] = noteCollected;

    //WHITE2 - 4 in controller
    noteLabel = new JLabel(new ImageIcon("../assets/Done/WhiteCollectMiddle.png"));
    noteLabel.setBounds(450, 350, 100, 100);
    noteLabel.setVisible(false);
    activeNotes[4] = noteLabel;

    noteCollected = new JLabel(new ImageIcon("../assets/Done/WhiteMiddle.png"));
    noteCollected.setBounds(450, 350, 100, 100);
    noteCollected.setVisible(false);
    collectedNotes[4] = noteCollected;

    //WHITE3 - 5 in controller
    noteLabel = new JLabel(new ImageIcon("../assets/Done/WhiteCollectRight.png"));
    noteLabel.setBounds(575, 350, 100, 100);
    noteLabel.setVisible(false);
    activeNotes[5] = noteLabel;

    noteCollected = new JLabel(new ImageIcon("../assets/Done/WhiteRight.png"));
    noteCollected.setBounds(575, 350, 100, 100);
    noteCollected.setVisible(false);
    collectedNotes[5] = noteCollected;

    for (int i = 0; i < 6; i ++) {
      add(activeNotes[i]);
      add(collectedNotes[i]);
    }
  }

  public void noteDisplay(boolean display, int pressedButton) {
    if (display) {
      activeNotes[pressedButton].setVisible(true);
    } else {
      if(noteLabel != null) {
        for (int i = 0; i < pressedButton; i++)
          activeNotes[i].setVisible(false);
      }
    }
  }

  public void noteCollected(boolean display, int pressedButton) {
    if (display) {
      collectedNotes[pressedButton].setVisible(true);
    } else {
      if(noteCollected != null) {
        for (int i = 0; i < pressedButton; i++)
          collectedNotes[i].setVisible(false);
      }
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
    g.drawImage(this.bg[((frame/this.backgroundFrameDelay)%this.backgroundFrameCount)], 0, 0, null);
    int len = notes.size();


    for(int i=len-1; i>-1; i--){
      // Draw the note
      notes.get(i).paintComponent(g);

        // Remove from the notes ArrayList as no longer needed
      if(notes.get(i).getY() > (int)(0.82 * height) || notes.get(i).collected)
        notes.remove(i);
      }

    frame++;
    }

  }

