import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.HashMap;
import java.io.FilenameFilter;
import java.lang.Thread;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.Color;
import java.awt.*;
/**
 * PlayModeModel
 * Version 3.2, March 2019
 * @author Tom Mansfield
 * @author Harper Ford
 * @author Kamila Hoffmann-Derlacka
 */

public class PlayModeModel implements Runnable{
  private String bundlePath;
  private File midiFile;
  private File notesFile;
  private File coverArt;
  private int multiplier;
  private int streakCount;
  private int totalCurrency;
  private int currencyEarned;
  private int score;
  private String currentNote;
  private long currentTick;
  private HashMap<Long, String> notes;
  private boolean endOfSong;
  private PlayModeView view;
  private long lastTick = 0;
  private int bpm;
  private String noteToPlay;

  //JLabels
  JLabel coverArtLabel;
  JLabel multiplierLabel;
  JLabel currencyLabel;
  JLabel scoreLabel;
  JLabel streakLabel;


  //Constructor
  public PlayModeModel( String bundlePath, PlayModeView view ) {

    // Set initial values of the game
    this.view = view;
    this.bundlePath = bundlePath;
    this.multiplier = 1;
    this.streakCount = 0;
    this.totalCurrency = Currency.loadTotalCurrency();
    this.currencyEarned = 0;
    this.score = 0;
    this.currentTick = 0;
    this.currentNote = "";
    this.endOfSong = false;

    try {
      this.notesFile = findNotesFile();
    } catch (Exception e) {
      e.printStackTrace();
      // NEED TO GO BACK TO SLASH MODE
    }

    try {
      this.midiFile = findMidiFile();
    } catch (Exception e) {
      e.printStackTrace();
      // NEED TO GO BACK TO SLASH MODE
    }

    try {
      this.coverArt = findCoverArt();
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.notes = new HashMap<>();
    loadNotesFile();
  }


  @Override
  public void run(){
    //Setup JLabels
    view.setCoverArtLabel(this.coverArt);

    multiplierLabel = new JLabel(new ImageIcon("../assets/times2Multiplier3.png"));
    multiplierLabel.setBounds(75, 225, 100, 100);
    multiplierLabel.setVisible(false);
    this.view.add(multiplierLabel);

    currencyLabel = new JLabel(new ImageIcon("../assets/1Star.png"));
    currencyLabel.setBounds(175, 300, 140, 30);
    currencyLabel.setVisible(false);
    this.view.add(currencyLabel);

    scoreLabel = new JLabel(Integer.toString(this.score));
    scoreLabel.setFont(new Font("Serif", Font.BOLD, 32));
    scoreLabel.setBounds(75,350, 100, 100);
    scoreLabel.setForeground(Color.white);
    this.view.add(scoreLabel);

    streakLabel = new JLabel("Streak:  " + Integer.toString(this.streakCount));
    streakLabel.setFont(new Font("Serif", Font.BOLD, 32));
    streakLabel.setBounds(75,450, 100, 100);
    streakLabel.setForeground(Color.white);
    this.view.add(streakLabel);

    playSong();
  }

  //*region Accessors
  public void setNoteToPlay(String n){
    this.noteToPlay = n;
  }

  public String getCurrentNote() {
    return this.currentNote;
  }

  public HashMap<Long, String> getNotes() {
    return this.notes;
  }

  public long getCurrentTick() {
    return this.currentTick;
  }

  public boolean isEndOfSong() {
    return this.endOfSong;
  }

  /**
   * setMultiplier
   * Changes the value of the multiplier if streakCount is multiple of 10 or 0
   * Does nothing if streak count not multiple of 10 or 0
   */
  public void setMultiplier() {

    // Each time streak is a multiple of 10, change the multiplier
    if(this.streakCount % 10 == 0 ) {
      // Multiplier value doubles each time, e.g. 2, 4, 8, 16, 32, 64 etc.
      String img;
      this.multiplierLabel.setVisible(true);
      this.multiplier = (int) Math.pow(2, this.streakCount/10);
      switch(this.multiplier){
        case 2:
          img = "../assets/times2Multiplier3.png";
          break;

        case 4:
          img = "../assets/times4Multiplier3.png";
          break;

        case 8:
          img = "../assets/times8Multiplier3.png";
          break;

        case 16:
          img = "../assets/times16Multiplier3.png";
          break;

        case 32:
          img = "../assets/times32Multiplier3.png";
          break;

        case 64:
          img = "../assets/times64Multiplier3.png";
          break;

        default:
          this.multiplierLabel.setVisible(false);
          img = "";
      }
      this.multiplierLabel.setIcon(new ImageIcon(img));
    }
  }
  //*endregion

  //*region Parse Zip
  /**
   * findNotesFile
   * @return the notes file in the bundle
   * @throws Exception when notes file is not found
   */
  public File findNotesFile() throws Exception {

    File bundle = new File(this.bundlePath);

    // Find text files in the directory
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".txt");
      }
    });

    if ( files.length > 0 ) {

      // Returns first occurrence of a text file, should only be one
      return files[0];

    } else {

      throw new Exception("No Notes File In Bundle");

    }

  }

  /**
   * findMidiFile
   * @return the MIDI file in the bundle
   * @throws Exception when MIDI file is not found
   */
  public File findMidiFile() throws Exception {

    File bundle = new File(this.bundlePath);

    // Find MIDI files in the directory
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".mid");
      }
    });
    if ( files.length > 0 ) {
      // Returns first occurrence of MIDI file, should only be one
      return files[0];
    } else {
      throw new Exception("No MIDI File In Bundle");
    }
  }

  /**
   * findCoverArt
   * @return the cover art file in the bundle
   * @throws Exception when a cover art file cannot be found
   */
  public File findCoverArt() throws Exception {

    File bundle = new File(this.bundlePath);

    // Find PNG files in the bundle
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png");
      }
    });

    if ( files.length > 0 ) {

      // Returns first occurrence of PNG file, should only be one
      return files[0];

    } else {

      throw new Exception("No Cover Art In Bundle");

    }

  }

  /**
   * loadNotesFile
   * Reads the notes file in the bundle and adds notes to a map
   */
  public void loadNotesFile() {

    try {
      BufferedReader br = new BufferedReader( new FileReader(this.notesFile));
      String line;
      while((line = br.readLine())!=null) {
        // Split notes file by comma, separating ticks and notes
        String str[] = line.split(",");
        this.notes.put(Long.parseLong(str[0]), str[1]);
      }

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
      // NEED TO GO BACK TO SLASH MODE
    }

  }
  //*endregion


  /**
   * playSong
   * Play the MIDI song
   * Sets current tick and current note values as the song is played
   */
  public void playSong() {

    // Plays the MIDI song in a separate thread
    PlaySong playSong = new PlaySong(this.midiFile);
    Thread playSongThread = new Thread(playSong);
    playSongThread.start();

    // While the song is still playing
    while(!playSong.endOfSong){

      // Change the current tick and current note values
      currentTick = playSong.currentTick;
      this.currentNote = changeCurrentNote(currentTick);

      // If there is a not to be played and the note has not already been added to highway
      if(!currentNote.equals("000") && currentTick != lastTick){

        // Add the note to the highway
        Note note = new Note(currentNote, this);
        view.addNote(note);

        // Note for the current tick has been added
        lastTick = currentTick;
      }
    }

    if(playSong.endOfSong) {
      // OUTPUT END OF GAME MESSAGE ?? RETURN TO SLASH MODE?? WAIT FOR USER TO ESCAPE??
    }

  }

  /**
   * changeCurrentNote
   * Changes the current note to the note that should be played
   * @param tick the current tick
   */
  public String changeCurrentNote(long tick) {
    if(this.notes.get(Long.valueOf(tick))!=null){
      return this.notes.get(Long.valueOf(tick));
    }
    else{
      return "000";
    }
  }

  /**
   * checkNote
   * Called by the controller, checks whether a note played on the guitar is correct
   * Collect note if correct, otherwise drop the note
   * @param guitarNote the note played on the guitar
   */
  public void checkNote(String guitarNote) {

    boolean noteCollected = false;

    try {

      // Checks if the user has pressed the note that is at the bottom of the screen
      if(this.noteToPlay.equals(guitarNote) && !view.notes.get(0).collected){
          collectNote();
          view.notes.get(0).collect();
          System.out.println(Integer.toString(score));
      }
      else{
        dropNote();
      }

    } catch (Exception e) {

      // If there is an error, continue game and don't crash
      // Very rare but may need to revisit how this is dealt with
      dropNote();

    }

    // User has not pressed the right note or attempted to play a note when none should be played
    if ( !noteCollected ) {
      dropNote();
      view.collected = false;
    }
  }

  /**
   * collectNote
   * Occurs when the user plays a correct note
   * Updates the streakCount, Multiplier and currencyEarned if necessary
   */
  public void collectNote() {
    this.streakCount ++;
    streakLabel.setText("Streak:  " + Integer.toString(this.streakCount));
    setMultiplier();
    this.score += this.multiplier;
    updateCurrency();
    scoreLabel.setText(Integer.toString(this.score));

  }

  /**
   * dropNote
   * Occurs when the user does not play a correct note or plays a note when no note should be played
   * Resets the streakCount and multiplier
   */
  public void dropNote() {
    this.streakCount = 0;
    streakLabel.setText("Streak:  " + Integer.toString(this.streakCount));
    setMultiplier();
  }

  /**
   * updateCurrency
   * Updates the current currency earned during the song
   */
  public void updateCurrency() {

    // Can only earn a maximum currency value of 5 per game
    if(this.currencyEarned < 5 ) {
      // Currency is earned every time score is a multiple of 500
      if(this.score % 500 == 0) {
        this.currencyEarned ++;
        String img = "..assets/"+Integer.toString(this.currencyEarned)+"Star.png";
        this.currencyLabel.setIcon(new ImageIcon(img));
        this.currencyLabel.setVisible(true);
      }
    }

  }

  public ImageIcon resizeCoverArt(File cA) {

    Image image = null;

    try {
      image = ImageIO.read(cA);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Image resizedImage = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

    return new ImageIcon(resizedImage);
  }

}
