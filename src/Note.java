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
  Dimension screenSize = new Dimension(1000,563);
  Image blackNote = new ImageIcon("../assets/BlackNote.png").getImage();
  Image whiteNote = new ImageIcon("../assets/WhiteNote.png").getImage();
  Image sprite;
  //Setup position/speed variables
  int velocity = 4;
  int y = (int)(0.4*screenSize.height);
  int x;
  String noteValue;
  int[][] positions = new int[3][2];
  boolean collected = false;
  PlayModeModel model;

  public int getY(){ return this.y; }
  /**
   *
   */
  public Note(String notes, PlayModeModel model){
    this.model = model;
    //Set X position
    this.positions[0][0] = (screenSize.width/2) - blackNote.getWidth(null)*2;
    this.positions[1][0] = (screenSize.width/2) - blackNote.getWidth(null)/2;
    this.positions[2][0] = (screenSize.width/2) + blackNote.getWidth(null)/2;
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
      Image resizedImage = null;
      if(each[1] == 2){
        resizedImage = whiteNote;
      }
      else if(each[1] == 1){
        resizedImage = blackNote;
      }
      else{}
      if(resizedImage != null){
        int dim = (int)((this.y)*resizedImage.getHeight(null));
        g.drawImage(resizedImage,each[0], this.y, null);
      }
    }

    incY(velocity);
    //Set X position
    this.positions[0][0] -= Math.ceil(0.4);
    this.positions[2][0] += Math.ceil(0.4);
  }

  public void collect(){
    this.collected = true;
  }

  private void incY(int v){
    this.y += v;
    if(this.y>(int)(350) && y< (int)(450) && !this.collected){
      model.setNoteToPlay(this.noteValue);
      System.out.println(this.noteValue);
    }
    else if(this.y > (int)(450) && !this.collected){
      model.dropNote();
      model.setNoteToPlay("");
    }
  }
}
