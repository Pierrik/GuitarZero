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

  JLabel coverArtLabel;
  JLabel multiplierLabel;
  JLabel currencyLabel;
  JLabel scoreLabel;
  JLabel streakLabel;
  JLabel zeroPowerLabel;




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

    this.setLayout(null);
  }

  public void setCoverArtLabel(File coverFile) {
    Image image = null;

    try {
      image = ImageIO.read(coverFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Image resizedImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

    coverArtLabel = new JLabel(new ImageIcon(resizedImage));
    coverArtLabel.setBounds(50, 50, 150, 150);
    add(coverArtLabel);
  }

  // Sets a multiplier label
  public void setMultiplierLabel(String path) {
    multiplierLabel = new JLabel(new ImageIcon(path));
    multiplierLabel.setBounds(75, 225, 100, 100);
    multiplierLabel.setVisible(true);
    add(multiplierLabel);
  }

  // Sets a blank multiplier label when the multiplier is 1
  public void setMultiplierLabel() {
    multiplierLabel = new JLabel(new ImageIcon());
    multiplierLabel.setBounds(75, 225, 100, 100);
    multiplierLabel.setVisible(false);
    add(multiplierLabel);
  }

  public void changeMultiplierLabel(String path) {
    /*remove(multiplierLabel);
    multiplierLabel = new JLabel(new ImageIcon(path));
    //multiplierLabel.setIcon(new ImageIcon(path));
    multiplierLabel.setVisible(true);
    add(multiplierLabel);*/
    multiplierLabel.setIcon(new ImageIcon(path));
    multiplierLabel.setVisible(true);
  }


  public void setCurrencyLabel() {
    currencyLabel = new JLabel(new ImageIcon());
    currencyLabel.setBounds(175, 385, 140, 30);
    currencyLabel.setVisible(false);
    add(currencyLabel);
  }

  public void changeCurrencyLabel(String path) {
    currencyLabel.setIcon(new ImageIcon(path));
    currencyLabel.setVisible(true);
    System.out.println("Currency label changed");
  }

  public void setZeroPowerLabelInit(String path) {
    zeroPowerLabel = new JLabel(new ImageIcon(path));
    zeroPowerLabel.setBounds(726, 125, 174, 192);
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
    scoreLabel.setBounds(25,350, 200, 100);
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
    streakLabel.setBounds(400,0, 200, 200);
    streakLabel.setForeground(Color.white);
    add(streakLabel);
  }

  public void resetStreakLabel(Integer streak) {
    streakLabel.setText("Streak:  " + Integer.toString(streak));
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

    /*scoreLabel.setBounds(75,350, 100, 100);
    streakLabel.setBounds(75,450, 100, 100);
    zeroPowerLabel.setBounds(0, 0, 174, 192);
    coverArtLabel.setBounds(50, 50, 150, 150);
    multiplierLabel.setBounds(75, 225, 100, 100);*/


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

