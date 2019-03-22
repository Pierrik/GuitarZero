import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
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
 * @version 5.1, March 2019.
*/
public class PlayModeView extends JPanel{

  ArrayList<Note> notes = new ArrayList<Note>();
  private static int frame;

  // Setup background animation values
  private static int backgroundFrameCount = 1;
  private static int backgroundFrameDelay = 100;

  // Create BufferedImage array to store the background frames
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];

  // Initialise JLabels to display notes and game values
  private JLabel coverArtLabel;
  private JLabel multiplierLabel;
  private JLabel currencyLabel;
  private JLabel scoreLabel;
  private JLabel streakLabel;
  private JLabel zeroPowerLabel;
  private JLabel noteLabel;
  private JLabel noteCollected;
  private JLabel endStatisticsBackground;
  private JLabel endStatisticsImg;
  private JLabel endStatisticsTitle;
  private JLabel endStatisticsScore;
  private JLabel endStatisticsCurrency;
  private JLabel endStatisticsNote;
  private JLabel[] activeNotes = new JLabel[6];
  private JLabel[] collectedNotes = new JLabel[6];

  // Path to the background of the game
  private static final String HIGHWAY_PATH = "../assets/Done/Highway.bmp";

  // Dimensions for displaying the end statistics
  private static final int END_STATISTICS_WIDTH = 400;
  private static final int END_STATISTICS_HEIGHT = 400;
  private static final int END_STATISTICS_X = 300;
  private static final int END_STATISTICS_Y = 100;
  private static final int END_STATISTIC_MARGIN = 20;
  private static final int END_STATISTIC_TEXT_HEIGHT = 40;
  // Dimensions for displaying the cover art
  private static final int COVER_ART_WIDTH = 200;
  private static final int COVER_ART_HEIGHT = 200;
  private static final int COVER_ART_X = 50;
  private static final int COVER_ART_Y = 28;

  // Dimensions for displaying the score multiplier
  private static final int MULTIPLIER_WIDTH = 100;
  private static final int MULTIPLIER_HEIGHT = 100;
  private static final int MULTIPLIER_X = 50;
  private static final int MULTIPLIER_Y = 250;

  // Dimensions for displaying the currency earned during the game
  private static final int CURRENCY_WIDTH = 140;
  private static final int CURRENCY_HEIGHT = 30;
  private static final int CURRENCY_X = 200;
  private static final int CURRENCY_Y = 437;

  // Dimensions for displaying the zero power icon during zero power mode
  private static final int ZERO_POWER_WIDTH = 174;
  private static final int ZERO_POWER_HEIGHT = 192;
  private static final int ZERO_POWER_X = 726;
  private static final int ZERO_POWER_Y = 140;

  // Dimensions for displaying the current score during the game
  private static final int SCORE_X = 50;
  private static final int SCORE_Y = 400;
  private static final int SCORE_WIDTH = 200;
  private static final int SCORE_HEIGHT = 100;

  // Size of the text for the score label and the streak label
  private static final int TEXT_SIZE = 32;

  // Dimensions for displaying the current note streak
  private static final int STREAK_WIDTH = 200;
  private static final int STREAK_HEIGHT = 200;
  private static final int STREAK_X = 440;
  private static final int STREAK_Y = 28;

  // Dimensions and asset paths for displaying the notes on the fretboard at the bottom of the highway
  private static final int FRETBOARD_Y = 395;
  private static final String FRETBOARD_WHITE = "../assets/Done/WhiteNoteToPlay.png";
  private static final String FRETBOARD_BLACK = "../assets/Done/BlackNoteToPlay.png";
  private static final String FRETBOARD_WHITE_COLLECTED = "../assets/Done/WhiteNoteCollected.png";
  private static final String FRETBOARD_BLACK_COLLECTED = "../assets/Done/BlackNoteCollected.png";
  private static final int FRETBOARD_NOTE_WIDTH = 100;
  private static final int FRETBOARD_NOTE_HEIGHT = 100;
  private static final int FRETBOARD_LEFT_X = 350;
  private static final int FRETBOARD_MIDDLE_X = 450;
  private static final int FRETBOARD_RIGHT_X = 550;

  // Amount of note buttons on the guitar
  private static final int TOTAL_NOTE_BUTTONS = 6;

  // The y coordinate that causes a note to be removed from the screen if it falls past this point
  private static final int DROP_NOTE_BOUND = 463;

  public PlayModeView(){
    frame = 0;
    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File(HIGHWAY_PATH));
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    this.setLayout(null);
  }

  /**
   * displayEndValues
   * @param img The cover art of the current song
   * @param songTitle The current songs name
   * @param score The final score of the game
   * @param currency The users currency total
   */
  public void displayEndValues(File img, String songTitle, String score, String currency){
    String[] segments = songTitle.split("/");
    String idStr = segments[segments.length-1];
    songTitle = idStr;
    endStatisticsBackground = new JLabel(new ImageIcon("../assets/emptyBox.jpeg"));
    endStatisticsBackground.setBounds(END_STATISTICS_X,END_STATISTICS_Y,END_STATISTICS_WIDTH,END_STATISTICS_HEIGHT);

    //SONG NAME
    endStatisticsTitle = new JLabel("Song: "+songTitle);
    endStatisticsTitle.setBounds(END_STATISTICS_X + (END_STATISTICS_WIDTH/2) - (COVER_ART_WIDTH/2), END_STATISTICS_Y+COVER_ART_HEIGHT+(2*END_STATISTIC_MARGIN), COVER_ART_WIDTH, END_STATISTIC_TEXT_HEIGHT);
    endStatisticsTitle.setForeground(Color.BLACK);
    scoreLabel.setFont(new Font("Serif", Font.BOLD, TEXT_SIZE));
    endStatisticsTitle.setVisible(true);
    add(endStatisticsTitle);

    //GAME SCORE
    endStatisticsScore = new JLabel("Score: "+score);
    endStatisticsScore.setBounds(END_STATISTICS_X + (END_STATISTICS_WIDTH/2) - (COVER_ART_WIDTH/2), END_STATISTICS_Y+COVER_ART_HEIGHT+(3*END_STATISTIC_MARGIN), COVER_ART_WIDTH, END_STATISTIC_TEXT_HEIGHT);
    endStatisticsScore.setForeground(Color.BLACK);
    scoreLabel.setFont(new Font("Serif", Font.BOLD, TEXT_SIZE));
    endStatisticsScore.setVisible(true);
    add(endStatisticsScore);

    //TOTAL CURRENCY
    endStatisticsCurrency = new JLabel("Currency: "+currency);
    endStatisticsCurrency.setBounds(END_STATISTICS_X + (END_STATISTICS_WIDTH/2) - (COVER_ART_WIDTH/2), END_STATISTICS_Y+COVER_ART_HEIGHT+(4*END_STATISTIC_MARGIN), COVER_ART_WIDTH, END_STATISTIC_TEXT_HEIGHT);
    endStatisticsCurrency.setForeground(Color.BLACK);
    scoreLabel.setFont(new Font("Serif", Font.BOLD, TEXT_SIZE));
    endStatisticsCurrency.setVisible(true);
    add(endStatisticsCurrency);

    //FOOT NOTE
    endStatisticsNote = new JLabel("Exit Button (O) - Main Menu");
    endStatisticsNote.setBounds(END_STATISTICS_X + (END_STATISTICS_WIDTH/2) - (COVER_ART_WIDTH/2), END_STATISTICS_Y+END_STATISTICS_HEIGHT-40, COVER_ART_WIDTH,40);
    endStatisticsNote.setForeground(Color.BLACK);
    scoreLabel.setFont(new Font("Serif", Font.BOLD, TEXT_SIZE));
    endStatisticsNote.setVisible(true);
    add(endStatisticsNote);

    //COVER ART
    endStatisticsImg = new JLabel(new ImageIcon(resizeImage(img, COVER_ART_WIDTH, COVER_ART_HEIGHT)));
    endStatisticsImg.setBounds((int) (END_STATISTICS_X + (END_STATISTICS_WIDTH/2) - (COVER_ART_WIDTH/2)), END_STATISTICS_Y+50,COVER_ART_WIDTH,COVER_ART_HEIGHT);
    add(endStatisticsImg);
    add(endStatisticsBackground);
  }

  /**
   * setCoverArtLabel
   * Sets the cover art to be displayed on the screen during the game
   * Resizes the image file to fit the size of the JLabel
   * @param coverFile
   */
  public void setCoverArtLabel(File coverFile) {
    Image resizedImage = resizeImage(coverFile,COVER_ART_WIDTH,COVER_ART_HEIGHT);
    coverArtLabel = new JLabel(new ImageIcon(resizedImage));
    coverArtLabel.setBounds(COVER_ART_X, COVER_ART_Y, COVER_ART_WIDTH, COVER_ART_HEIGHT);
    add(coverArtLabel);
  }

  /**
   * Reads an image file and resizes it to desired dimensions
   * @param file
   * @param width
   * @param height
   * @return Resized Image as Image
   */
  public Image resizeImage(File file, int width, int height){
    Image image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    return resizedImage;
  }

  /**
   * setMultiplierLabel
   * Initialises the multiplier label to display the user's score multiplier
   * Also used to set a blank multiplier label when the user loses their score multiplier
   */
  public void setMultiplierLabel() {
    multiplierLabel = new JLabel();
    multiplierLabel.setBounds(MULTIPLIER_X, MULTIPLIER_Y, MULTIPLIER_WIDTH, MULTIPLIER_HEIGHT);
    multiplierLabel.setVisible(false);
    add(multiplierLabel);
  }

  /**
   * changeMultiplier
   * Called when the user gains a new score multiplier or loses their multiplier
   * Changes the multiplier label displayed on the screen
   * @param path the path of the multiplier asset to display
   */
  public void changeMultiplierLabel(String path) {
      multiplierLabel.setIcon(new ImageIcon(path));
      multiplierLabel.setVisible(true);
  }

  /**
   * setCurrencyLabel
   * Initialises tbe JLabel to display the currency stars
   */
  public void setCurrencyLabel() {
    currencyLabel = new JLabel();
    currencyLabel.setBounds(CURRENCY_X, CURRENCY_Y, CURRENCY_WIDTH, CURRENCY_HEIGHT);
    currencyLabel.setVisible(false);
    add(currencyLabel);
  }

  /**
   * Change currency label
   * Called when the user earns currency
   * Updates the amount of stars displayed on the screen
   * @param path the path to the asset corresponding to the amount of stars to be displayed
   */
  public void changeCurrencyLabel(String path) {
    currencyLabel.setIcon(new ImageIcon(path));
    currencyLabel.setVisible(true);
  }

  /**
   * setZeroPowerLabelInit
   * Initialises the zero power JLabel
   * @param path the path of the zero power image
   */
  public void setZeroPowerLabelInit(String path) {
    zeroPowerLabel = new JLabel(new ImageIcon(path));
    zeroPowerLabel.setBounds(ZERO_POWER_X, ZERO_POWER_Y, ZERO_POWER_WIDTH, ZERO_POWER_HEIGHT);
  }

  /**
   * setZeroPowerLabelVisible
   * Shows the zero power icon on the screen when zero power mode is active
   */
  public void setZeroPowerLabelVisible() {
    zeroPowerLabel.setVisible(true);
    add(zeroPowerLabel);
  }

  public void setZeroPowerLabelFalse() {
    zeroPowerLabel.setVisible(false);
  }

  /**
   * setScoreLabel
   * Initialises the Jlabel to display the user's current score
   * @param score
   */
  public void setScoreLabel(Integer score) {
    scoreLabel = new JLabel("Score: " + Integer.toString(score));
    scoreLabel.setFont(new Font("Serif", Font.BOLD, TEXT_SIZE));
    scoreLabel.setBounds(SCORE_X, SCORE_Y, SCORE_WIDTH, SCORE_HEIGHT);
    scoreLabel.setForeground(Color.white);
    scoreLabel.setVisible(true);
    add(scoreLabel);
  }

  /**
   * resetScoreLabel
   * Sets the value of the score being displayed
   * @param score the new score to be displayed
   */
  public void resetScoreLabel(Integer score) {
    scoreLabel.setText("Score: " + Integer.toString(score));
  }

  /**
   * setStreakLabel
   * Initialises the JLabel to display the user's note streak
   */
  public void setStreakLabel() {
    streakLabel = new JLabel("Streak:  " + Integer.toString(0));
    streakLabel.setFont(new Font("Serif", Font.BOLD, TEXT_SIZE));
    streakLabel.setBounds(STREAK_X,STREAK_Y, STREAK_WIDTH, STREAK_HEIGHT);
    streakLabel.setForeground(Color.white);
    add(streakLabel);
  }

  /**
   * resetStreakLabel
   * Sets the value of the streak being displayed
   * @param streak the new streak to display
   */
  public void resetStreakLabel(Integer streak) {
    streakLabel.setText("Streak:  " + streak);
  }

  /**
   * noteInit
   * Initialises the notes to be shown on the fretboard when the user presses a note button
   * Initialises the coloured note image for when the user collects a note
   */
  public void noteInit() {
    // WHITE1 - 0 in controller
    noteLabel = new JLabel(new ImageIcon(FRETBOARD_WHITE));
    noteLabel.setBounds(FRETBOARD_LEFT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteLabel.setVisible(false);
    activeNotes[0] = noteLabel;

    noteCollected = new JLabel(new ImageIcon(FRETBOARD_WHITE_COLLECTED));
    noteCollected.setBounds(FRETBOARD_LEFT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteCollected.setVisible(false);
    collectedNotes[0] = noteCollected;

    //BLACK1 - 1 in controller
    noteLabel = new JLabel(new ImageIcon(FRETBOARD_BLACK));
    noteLabel.setBounds(FRETBOARD_LEFT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteLabel.setVisible(false);
    activeNotes[1] = noteLabel;

    noteCollected = new JLabel(new ImageIcon(FRETBOARD_BLACK_COLLECTED));
    noteCollected.setBounds(FRETBOARD_LEFT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteCollected.setVisible(false);
    collectedNotes[1] = noteCollected;

    //BLACK2 - 2 in controller
    noteLabel = new JLabel(new ImageIcon(FRETBOARD_BLACK));
    noteLabel.setBounds(FRETBOARD_MIDDLE_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteLabel.setVisible(false);
    activeNotes[2] = noteLabel;

    noteCollected = new JLabel(new ImageIcon(FRETBOARD_BLACK_COLLECTED));
    noteCollected.setBounds(FRETBOARD_MIDDLE_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteCollected.setVisible(false);
    collectedNotes[2] = noteCollected;

    //BLACK3 - 3 in controller
    noteLabel = new JLabel(new ImageIcon(FRETBOARD_BLACK));
    noteLabel.setBounds(FRETBOARD_RIGHT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteLabel.setVisible(false);
    activeNotes[3] = noteLabel;

    noteCollected = new JLabel(new ImageIcon(FRETBOARD_BLACK_COLLECTED));
    noteCollected.setBounds(FRETBOARD_RIGHT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteCollected.setVisible(false);
    collectedNotes[3] = noteCollected;

    //WHITE2 - 4 in controller
    noteLabel = new JLabel(new ImageIcon(FRETBOARD_WHITE));
    noteLabel.setBounds(FRETBOARD_MIDDLE_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteLabel.setVisible(false);
    activeNotes[4] = noteLabel;

    noteCollected = new JLabel(new ImageIcon(FRETBOARD_WHITE_COLLECTED));
    noteCollected.setBounds(FRETBOARD_MIDDLE_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteCollected.setVisible(false);
    collectedNotes[4] = noteCollected;

    //WHITE3 - 5 in controller
    noteLabel = new JLabel(new ImageIcon(FRETBOARD_WHITE));
    noteLabel.setBounds(FRETBOARD_RIGHT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteLabel.setVisible(false);
    activeNotes[5] = noteLabel;

    noteCollected = new JLabel(new ImageIcon(FRETBOARD_WHITE_COLLECTED));
    noteCollected.setBounds(FRETBOARD_RIGHT_X, FRETBOARD_Y, FRETBOARD_NOTE_WIDTH, FRETBOARD_NOTE_HEIGHT);
    noteCollected.setVisible(false);
    collectedNotes[5] = noteCollected;

    for (int i = 0; i < TOTAL_NOTE_BUTTONS; i ++) {
      add(activeNotes[i]);
      add(collectedNotes[i]);
    }
  }

  /**
   * noteDisplay
   * Displays a note on the fretboard when the user presses the button
   * Note corresponds with the button pressed on the guitar
   * @param display true if note should be displayed, false otherwise
   * @param pressedButton the button pressed by the user
   */
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

  /**
   * noteCollected
   * Displays a coloured version of a note on the fretboard if a note has been collected
   * Note displayed on the fretboard corresponds to the note collected
   * @param display true if the collected note should be displayed, false otherwise
   * @param pressedButton the button pressed by the user when collecting the note
   */
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
    * Removes a note if it has passed a certain boundary
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
      if(notes.get(i).getY() > DROP_NOTE_BOUND || notes.get(i).collected)
        notes.remove(i);
      }

    frame++;
    }

  }

