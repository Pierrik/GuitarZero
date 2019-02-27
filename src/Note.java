import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;

/**
 * Note object
 * @author Harper Ford
 */
public class Note extends Highway {
  //Load note sprites
  Image blackNote = new ImageIcon("assets\\BlackNote.png").getImage();
  Image whiteNote = new ImageIcon("assets\\WhiteNote.png").getImage();
  Image sprite;
  //Setup position/speed variables
  int x;
  int y = 0;
  int velocity = 3;

  /**
   * Sets object variables
   * @param white: Whether the sprite should be white or black
   * @param lane: The lane the note will appear in on screen
   */
  public Note(boolean white, int lane){
    //Set object sprite to correct color
    if(white){ this.sprite = whiteNote; }
    else{ this.sprite = blackNote; }
    //Assign intial x value depending on given lane
    switch(lane){
      case 1:
      x = (bg[0].getWidth(null)/4);
      break;
      case 2:
        x = (bg[0].getWidth(null)/2) - blackNote.getWidth(null)/2;
        break;
      case 3: x = (bg[0].getWidth(null)/4) + (bg[0].getWidth(null)/2) - blackNote.getWidth(null)/2;
      break;
    }
  }

  /**
   * Override function to draw sprite at position x,y
   * @param g: The graphics to draw with
   */
  public void paintComponent(Graphics g){
    g.drawImage(sprite,x, y, null);
    y += velocity;
    //Reset note --!!REMOVE LATER!!--
    if(y>bg[0].getHeight(null)){y=0;}
  }
}
