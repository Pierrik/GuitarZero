import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;

/**
 * Notes to be displayed on the highway
 *
 * @author Harper Ford
 * @author Tom Mansfield
 * @version 3.00, March 2019.
*/
public class Note{

  // Constants used to paint the notes and bounds to collect notes
  private static final int INITIAL_Y = 225;
  private static final int VELOCITY = 4;
  private static final int SCREEN_WIDTH = 1000;
  private static final int SCREEN_HEIGHT = 563;
  private static final int LEFT_INITIAL_X = 412;
  private static final int RIGHT_INITIAL_X = 510;
  private static final int WHITE_VALUE = 2;
  private static final int BLACK_VALUE = 1;
  private static final double GRADIENT = 0.4;
  private static final int COLLECT_START_BOUND = 350;
  private static final int COLLECT_END_BOUND = 460;

  //Load note sprites
  private Dimension screenSize = new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT);
  private Image blackNote = new ImageIcon("../assets/BlackNote.png").getImage();
  private Image whiteNote = new ImageIcon("../assets/WhiteNote.png").getImage();

  //Setup position/speed variables
  int y = INITIAL_Y;
  String noteValue;
  int[][] positions = new int[3][2];
  boolean collected = false;
  PlayModeModel model;

  public int getY(){ return this.y; }

  public Note(String notes, PlayModeModel model){
    this.model = model;
    this.positions[0][0] = LEFT_INITIAL_X;
    this.positions[1][0] = (screenSize.width/2) - blackNote.getWidth(null)/2;
    this.positions[2][0] = RIGHT_INITIAL_X;

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
      if(each[1] == WHITE_VALUE){
        resizedImage = whiteNote;
      }
      else if(each[1] == BLACK_VALUE){
        resizedImage = blackNote;
      }
      else{}
      if(resizedImage != null){
        int dim = (int)((this.y)*resizedImage.getHeight(null));
        g.drawImage(resizedImage,each[0], this.y, null);
      }
    }

    incY(VELOCITY);
    //Set X position
    this.positions[0][0] -= Math.ceil(GRADIENT);
    this.positions[2][0] += Math.ceil(GRADIENT);
  }

  public void collect(){
    this.collected = true;
  }

  // Sets the note tp play when the note is in a certain area
  private void incY(int v){
    this.y += v;
    if(this.y>COLLECT_START_BOUND && y< COLLECT_END_BOUND && !this.collected){
      model.setNoteToPlay(this.noteValue);
    }
    // Drop note when it falls past the collection area
    else if(this.y > COLLECT_END_BOUND && !this.collected){
      model.dropNote();
      model.setNoteToPlay("");
    }
  }
}
