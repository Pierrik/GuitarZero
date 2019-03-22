import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.*;

/**
 * Notes to be displayed on the highway
 *
 * @author Harper Ford
 * @author Tom Mansfield
 * @version 4.00, March 2019.
*/
public class Note{

  // Constants used to paint the notes and bounds to collect notes
  // Starting Y coordinate of notes
  private static final int    INITIAL_Y           = 175;

  // Speed the notes fall down the screen
  private static final int    VELOCITY            = 4;

  // Screen dimensions
  private static final int    SCREEN_WIDTH        = 1000;
  private static final int    SCREEN_HEIGHT       = 563;

  // Initial X coordinates for notes in the left and right highway
  private static final int    LEFT_INITIAL_X      = 412;
  private static final int    RIGHT_INITIAL_X     = 510;

  // Value of black and white notes
  private static final int    WHITE_VALUE         = 2;
  private static final int    BLACK_VALUE         = 1;

  // Amount that the left and right notes on the highway move on the x axis for each screen refresh
  private static final double GRADIENT            = 0.4;

  // Y coordinate bounds to collect notes in
  private static final int    COLLECT_START_BOUND = 250;
  private static final int    COLLECT_END_BOUND   = 460;

  //Load note sprites
  private Dimension screenSize = new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT);
  private Image blackNote = new ImageIcon("../assets/BlackNote.png").getImage();
  private Image whiteNote = new ImageIcon("../assets/WhiteNote.png").getImage();

  //Setup position/speed variables
  int y = INITIAL_Y;
  int[][] positions = new int[3][2];
  boolean collected = false;
  PlayModeModel model;

  // Numerical note value
  String noteValue;

  public int getY(){ return this.y; }

  public Note(String notes, PlayModeModel model){
    this.model = model;

    // Set initial x coordinates for notes
    this.positions[0][0] = LEFT_INITIAL_X;
    this.positions[1][0] = (screenSize.width/2) - blackNote.getWidth(null)/2;
    this.positions[2][0] = RIGHT_INITIAL_X;

    //Set object sprite to correct color
    this.positions[0][1] = Character.getNumericValue(notes.charAt(0));
    this.positions[1][1] = Character.getNumericValue(notes.charAt(1));
    this.positions[2][1] = Character.getNumericValue(notes.charAt(2));

    this.noteValue = notes;
  }

  /**
   * paintComponent
   * Draw the notes on each screen refresh
   * @param g
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

      // Draw the note if it has been set an image
      if(resizedImage != null){
        int dim = (int)((this.y/SCREEN_HEIGHT)*resizedImage.getHeight(null));
        g.drawImage(resizedImage,each[0], this.y, null);
      }
    }

    // Move the note according to the speed that the note should move
    incY(VELOCITY);
    // Move note in x direction by value of the gradient
    this.positions[0][0] -= Math.ceil(GRADIENT);
    this.positions[2][0] += Math.ceil(GRADIENT);
  }

  /**
   * collect
   * Occurs when the correct note has been played, sets the collected value of the note to true
   */
  public void collect(){
    this.collected = true;
  }

  /**
   * incY
   * @param v the velocity of the note as it moves down the screen
   */
  private void incY(int v){
    // Increase the y coordinate by the velocity value
    this.y += v;

    // If the note is in the range to collect notes, set the note to play in the model to the value of the note
    if(this.y>COLLECT_START_BOUND && y< COLLECT_END_BOUND && !this.collected){
      model.setNoteToPlay(this.noteValue);
    }
    // Drop note when it falls past the collection area and hasn't been collected
    else if(this.y > COLLECT_END_BOUND && !this.collected){
      model.dropNote();
      // Remove the note to play value in the model
      model.setNoteToPlay("");
    }
  }
}
