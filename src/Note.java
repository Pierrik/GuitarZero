import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;

public class Note{
  //Load note sprites
  Image blackNote = new ImageIcon("../assets/BlackNote.png").getImage();
  Image whiteNote = new ImageIcon("../assets/WhiteNote.png").getImage();
  Image sprite;
  //Setup position/speed variables
  int velocity = 3;
  int y = 0;
  int[][] positions = new int[3][2];

  /**
   *
   */
  public Note(String notes){
    //Set X position
    this.positions[0][0] = (1000/4);
    this.positions[1][0] = (1000/2) - blackNote.getWidth(null)/2;
    this.positions[2][0] = (1000/4) + (1000/2) - blackNote.getWidth(null)/2;
    //Set object sprite to correct color
    this.positions[0][1] = Character.getNumericValue(notes.charAt(0));
    this.positions[1][1] = Character.getNumericValue(notes.charAt(1));
    this.positions[2][1] = Character.getNumericValue(notes.charAt(2));
  }

  /*
  Override function to draw sprite at position x,y
  */
  public void paintComponent(Graphics g){
    for(int[] each : positions){
      System.out.println(each[0]);
      if(each[1] == 1){
        g.drawImage(whiteNote,each[0], this.y, null);
      }
      else if(each[1] == 2){
        g.drawImage(blackNote,each[0], this.y, null);
      }
      else{}
    }

    y += velocity;
    //Reset note --!!REMOVE LATER!!--
    if(y>500){y=0;}
  }
}
