import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;

/**
 * Notes to be displayed on the highway
 *
 * @author Harper Ford
 * @version 2.00, March 2019.
*/
public class Note{
  //Load note sprites
  Image blackNote = new ImageIcon("../assets/BlackNote.png").getImage();
  Image whiteNote = new ImageIcon("../assets/WhiteNote.png").getImage();
  Image sprite;
  //Setup position/speed variables
  int velocity = 4;
  int y = 250;
  int x;
  String noteValue;
  int[][] positions = new int[3][2];
  boolean collected = false;

  public int getY(){ return this.y; }
  /**
   *
   */
  public Note(String notes){
    //Set X position
    this.positions[0][0] = (1000/2) - blackNote.getWidth(null)*2;
    this.positions[1][0] = (1000/2) - blackNote.getWidth(null)/2;
    this.positions[2][0] = (1000/2) + blackNote.getWidth(null)/2;
    //Set object sprite to correct color
    this.positions[0][1] = Character.getNumericValue(notes.charAt(0));
    this.positions[1][1] = Character.getNumericValue(notes.charAt(1));
    this.positions[2][1] = Character.getNumericValue(notes.charAt(2));

    this.noteValue = notes;
  }

  /*
  Override function to draw sprite at position x,y
  */
  public void paintComponent(Graphics g){
    for(int[] each : positions){
      if(each[1] == 2){
        g.drawImage(whiteNote,each[0], this.y, null);
      }
      else if(each[1] == 1){
        g.drawImage(blackNote,each[0], this.y, null);
      }
      else{}
    }

    y += velocity;
    //Set X position
    this.positions[0][0] -= Math.ceil(0.4);
    this.positions[2][0] += Math.ceil(0.4);
  }
}
