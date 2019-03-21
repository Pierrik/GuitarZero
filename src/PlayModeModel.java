import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.HashMap;
import java.io.FilenameFilter;
import java.lang.Thread;
import java.util.Map;
import java.util.Scanner;


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
  private File currencyFile;
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
  private String noteToPlay;
  public long startZeroPower;
  public long endZeroPower;
  public boolean startGame;
  public int errors;
  PlaySong playSong;

  // a map of notes to controller buttons' values (the same for all OS)
  Map<Integer, Integer> notesToButtons = new HashMap<>() {{
    put(0, 0); put(1, 1); put(2, 4); put(3, 2); put(4, 5); put(5, 3);
  }};

  // File paths to update assets displayed during the game
  private static final String  ZERO_POWER_PATH   = "../assets/ZeroPowerShield.png";
  private static final String  MULTIPLIER2       = "../assets/times2Multiplier3.png";
  private static final String  MULTIPLIER4       = "../assets/times4Multiplier3.png";
  private static final String  MULTIPLIER8       = "../assets/times8Multiplier3.png";
  private static final String  MULTIPLIER16      = "../assets/times16Multiplier3.png";
  private static final String  MULTIPLIER32      = "../assets/times32Multiplier3.png";
  private static final String  MULTIPLIER64      = "../assets/times64Multiplier3.png";
  private static final String  CURRENCY_PATH     = "../currency/currency.txt";

  // Extensions of files to search for in the bundle
  private static final String  TXT_EXTENSION     = ".txt";
  private static final String  PNG_EXTENSION     = ".png";
  private static final String  MIDI_EXTENSION    = ".mid";

  // The value of no note being played
  private static final String  EMPTY_NOTE        = "000";

  // The amount of time to wait at the end of the game before returning to slash mode
  // Allows the user to see their score
  private static final int     END_OF_GAME_DELAY = 5000;

  // The maximum amount of currency that can be earned during the game
  private static final int     MAX_CURRENCY      = 5;

  // Currency updated every time the user reaches a score that is a multiple of 500
  private static final int     CURRENCY_SCORE    = 500;

  private static final int     DROP_NOTE_REGION  = 390;
  private static final int     MULTIPLIER_SETTER = 10;
  private static final int     ALL_BUTTONS       = 6;
  private static final int     ASCII_DIFF        = 48;
  private static final int     LANES             = 3;
  private static final int     FIRST_WHITE       = 0;
  private static final int     FIRST_BLACK       = 1;
  private static final int     BUTTON_DIFF       = 2;

  public PlayModeModel( String bundlePath, PlayModeView view ) {

    // Set initial values of the game
    this.errors = 0;
    this.view = view;
    this.bundlePath = bundlePath;
    this.multiplier = 1;
    this.streakCount = 0;
    this.currencyEarned = 0;
    this.score = 0;
    this.currentTick = 0;
    this.currentNote = "";
    this.endOfSong = false;

    // If notes file can't be loaded, do not start the game
    try {
      this.notesFile = findNotesFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    // If midi file can't be loaded, do not start the game
    try {
      this.midiFile = findMidiFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    // If the cover art can't be loaded, do not start the game
    try {
      this.coverArt = findCoverArt();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    // If the currency file can't be loaded, do not start the game
    try {
      this.currencyFile = findCurrencyFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    // Load the values in the notes file into a map of notes to be evaluated during the game
    this.notes = new HashMap<>();
    loadNotesFile();

    // If currency file cannot be read properly, do not start the game
    try {
      loadCurrencyFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    // If no errors occur during initialisation, the game can begin
    if (errors == 0 ) {
      this.startGame = true;
    }
  }

  /**
   * run
   * Sets up the JLabels to be displayed during the game
   * Starts the song playing
   */
  @Override
  public void run(){
    //Set up JLabels
    view.setCoverArtLabel(this.coverArt);
    view.setMultiplierLabel();
    view.setCurrencyLabel();
    view.setScoreLabel(this.score);
    view.setStreakLabel();
    view.setZeroPowerLabelInit(ZERO_POWER_PATH);
    view.noteInit();

    playSong();
  }

  /**
   * setNoteToPlay
   * Set the value of the note that is next to be played during the song
   * @param n the note to set
   */
  public void setNoteToPlay(String n){
    this.noteToPlay = n;
  }

  /**
   * getCurrentNote
   * @return the note currently being played during the song
   */
  public String getCurrentNote() {
    return this.currentNote;
  }

  /**
   * getCurrentTick
   * @return the current tick value of the song being played
   */
  public long getCurrentTick() {
    return this.currentTick;
  }

  /**
   * isEndOfSong
   * @return true if the song has finished playing, false otherwise
   */
  public boolean isEndOfSong() {
    return this.endOfSong;
  }

  /**
   * setMultiplier
   * Changes the value of the multiplier if streakCount is multiple of 10 or 0
   * Does nothing if streak count not multiple of 10 or 0
   * Multiplier doubles each time it is increased
   * E.g. score = 10, multiplier = 2
   *      score = 20, multiplier = 4
   *      score = 30, multiplier = 8 etc.
   */

  public void setMultiplier() {
    // Each time streak is a multiple of 10, change the multiplier
    if(this.streakCount % MULTIPLIER_SETTER == 0 ) {
      // Multiplier value doubles each time, e.g. 2, 4, 8, 16, 32, 64 etc.
      String img;

      // Determine which value the multiplier should be
      this.multiplier = (int) Math.pow(2, this.streakCount/10);

      // Set the multiplier labels in the view to the correct assets
      switch(this.multiplier){
        case 2:
          img = MULTIPLIER2;
          break;

        case 4:
          img = MULTIPLIER4;
          break;

        case 8:
          img = MULTIPLIER8;
          break;

        case 16:
          img = MULTIPLIER16;
          break;

        case 32:
          img = MULTIPLIER32;
          break;

        case 64:
          img = MULTIPLIER64;
          break;

        default:
         view.changeMultiplierLabel(null);
          img = "";
      }
      view.changeMultiplierLabel(img);
    }
  }

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
        return name.endsWith(TXT_EXTENSION);
      }
    });

    if ( files.length > 0 ) {
      // Returns first occurrence of a text file, should only be one
      return files[0];
    } else {
      // Throw an exception if there is no notes file
      // If this method throws an exception, the game does not begin
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
        return name.endsWith(MIDI_EXTENSION);
      }
    });
    if ( files.length > 0 ) {
      // Returns first occurrence of MIDI file, should only be one
      return files[0];
    } else {
      // Throw an exception if there is no midi file
      // If this method throws an exception, the game does not begin
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
        return name.endsWith(PNG_EXTENSION);
      }
    });

    if ( files.length > 0 ) {
      // Returns first occurrence of PNG file, should only be one
      return files[0];
    } else {
      // Throw an exception if there is no cover art file
      // If this method throws an exception, the game does not begin
      throw new Exception("No Cover Art In Bundle");
    }

  }

  /**
   * findCurrencyFile
   * @return the currency file for the game
   * @throws Exception
   */
  public File findCurrencyFile() throws Exception {
    // Search the currency directory
    File bundle = new File("../currency");

    // Find text files in the currency folder
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(TXT_EXTENSION);
      }
    });

    if ( files.length > 0 ) {
      // Returns first occurrence of txt file, should only be one
      return files[0];

    } else {
      // If there is no currency file found, create one
      File currencyFile = new File(CURRENCY_PATH);
      currencyFile.createNewFile();

      // Set the score value in the file to 0
      FileWriter writer = new FileWriter(CURRENCY_PATH);
      writer.write(Integer.toString(0));
      writer.close();

      // Returns the created currency file
      return currencyFile;
    }

  }

  /**
   * loadNotesFile
   * Reads the notes file in the bundle and adds the note values to a map
   * Determines when zero power mode is set to start
   * Reads the values for the tick, note and zero power mode by separating the values by commas
   * Zero power mode is determined when the third column of the notes file is set to 1 for a note
   * The first occurrence of a 1 means start zero power mode at this tick and the second occurrence means finish
   */
  public void loadNotesFile() {

    try {
      BufferedReader br = new BufferedReader(new FileReader(this.notesFile));
      String line;
      boolean hasStarted = false;
      boolean hasEnded = false;

      while((line = br.readLine())!=null) {
        // Split notes file by comma, separating ticks and notes
        String str[] = line.split(",");

        // Put the note into the map using the tick as the key value
        this.notes.put(Long.parseLong(str[0]), str[1]);

        // If the third value is a 1, this is when zero power mode starts or ends
        if (Long.parseLong(str[2]) == 1) {
          if (!hasStarted) {
            startZeroPower = Long.parseLong(str[0]);
            hasStarted = true;
          }
          else if (!hasEnded) {
            endZeroPower = Long.parseLong(str[0]);
            hasEnded = true;
          }
        }
      }

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
      startGame = false;
    }

  }

  /**
   * loadCurrencyFile
   * Reads the currency file and updates the total currency to the value contained in the file
   * @throws Exception when the file cannot be read
   */
  public void loadCurrencyFile() throws Exception {

    File inputFile = this.currencyFile;
    int currency;

    Scanner input = new Scanner(inputFile);
    // Iterates through each line of the file
    while (input.hasNextLine()) {
      currency = Integer.valueOf(input.nextLine());
      if (currency > 0)
      {
        this.totalCurrency = currency;
      }
      else {
        this.totalCurrency = 0;
      }
    }
    input.close();
  }

  /**
   * saveCurrencyFile
   * Updates the currency file when the song has finished with any earned currency
   * @throws Exception if the currency file cannot be written
   */
  public void saveCurrencyFile() throws Exception {
    FileWriter writer = new FileWriter(this.currencyFile.getName());
    writer.write(Integer.toString(this.totalCurrency));
    writer.close();
  }

  /**
   * playSong
   * Plays the MIDI song
   * Sets current tick and current note values as the song is played
   * Adds a note object to the view to be displayed on the highway if one is due to be played
   * Starts/ends zero power mode if it should be started or ended at the current tick
   * When the song finishes playing, end the game and save the currency earned to the user's currency file
   */
  public void playSong() {

    // Plays the MIDI song in a separate thread
    this.playSong = new PlaySong(this.midiFile);
    Thread playSongThread = new Thread(this.playSong);
    playSongThread.start();
    boolean hasZeroStarted = false;
    boolean hasZeroEnded = false;

    // While the song is still playing
    while(!playSong.endOfSong.get()){

      // Change the current tick and current note values
      currentTick = playSong.currentTick;
      this.currentNote = changeCurrentNote(currentTick);

      // Start zero power mode if it should be started at the current tick
      if (!hasZeroStarted && currentTick >= startZeroPower) {
        view.setZeroPowerLabelVisible();
        hasZeroStarted = true;

      // End zero power mode if it should finish at the current tick
      } else if (!hasZeroEnded && currentTick >= endZeroPower) {
        view.setZeroPowerLabelFalse();
        hasZeroEnded = true;
      }

      // If there is a not to be played and the note has not already been added to highway
      if(!currentNote.equals(EMPTY_NOTE) && currentTick != lastTick){

        // Add the note to the highway
        Note note = new Note(currentNote, this);
        view.addNote(note);

        // Note for the current tick has been added
        lastTick = currentTick;
      }
    }

    // If the song has finished, end the game
    if(playSong.endOfSong.get()) {
      try {
        // Wait 5 seconds while final score/currency etc. is still displayed
        Thread.sleep(END_OF_GAME_DELAY);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {

        // Update total currency and save
        try {
          saveCurrencyFile();
        } catch (Exception e) {
          e.printStackTrace();
        }
        //Display end stats. Go back to slash mode when the song is over
        this.view.displayEndValues(this.coverArt, this.bundlePath, Integer.toString(this.score), Integer.toString(this.totalCurrency));
      }
    }

  }

  /**
   * changeCurrentNote
   * Changes the current note value to the value of the note that should be played
   * If no note should be played at the current tick, return an empty note
   * @param tick the current tick
   */
  public String changeCurrentNote(long tick) {
    if(this.notes.get(Long.valueOf(tick))!=null){
      return this.notes.get(Long.valueOf(tick));
    }
    else{
      return EMPTY_NOTE;
    }
  }

  /**
   * checkNote
   * Called by the controller, checks whether the note played by the user on the guitar should be played
   * Collect note if correct, otherwise drop the note
   * @param guitarNote the note played on the guitar
   */
  public void checkNote(String guitarNote) {

    //boolean noteCollected = false;

    try {

      // Checks if the user has pressed the note that is at the bottom of the screen
      // If the current note to play is the note pressed by the user, the note has not already been collected
      // Also check whether the value of the note is the value supposed to be played and that the note is in the correct region
      if(this.noteToPlay.equals(guitarNote) && !view.notes.get(0).collected && view.notes.get(0).noteValue.equals(noteToPlay) && view.notes.get(0).getY()>DROP_NOTE_REGION){
        displayCollectedNote(guitarNote);
        collectNote();
        view.notes.get(0).collect();
      }

      // Otherwise, drop the note
      else{
        dropNote();
      }
    } catch (Exception e) {
      dropNote();
    }

  }

  /**
   * collectNote
   * Occurs when the user plays a correct note
   * Updates the streakCount, Multiplier and currencyEarned if necessary
   * Sets the labels displayed on the screen to the correct values
   */
  public void collectNote() {
    this.streakCount ++;
    view.resetStreakLabel(this.streakCount);
    setMultiplier();
    this.score += this.multiplier;
    updateCurrency();
    view.resetScoreLabel(this.score);
  }

  /**
   * dropNote
   * Occurs when the user does not play a correct note or plays a note when no note should be played
   * Resets the streakCount and multiplier
   * Sets the labels displayed on the screen to the correct values
   */
  public void dropNote() {
    this.streakCount = 0;
    view.resetStreakLabel(this.streakCount);
    setMultiplier();
    view.setMultiplierLabel();
  }

  /**
   * updateCurrency
   * Updates the current currency earned during the song
   * Sets the currency label to display the correct amount of stars on the screen
   */
  public void updateCurrency() {

    // Can only earn a maximum currency value of 5 per game
    if(this.currencyEarned < MAX_CURRENCY ) {
      // Currency is earned every time score is a multiple of 500
      if(this.score % CURRENCY_SCORE == 0) {
        this.currencyEarned ++;
        this.totalCurrency ++;
        String img = "../assets/"+Integer.toString(this.currencyEarned)+"Star.png";
        view.changeCurrencyLabel(img);
      }
    }

  }

  /**
   * displayNoteOn
   * When the user presses a note button, show which button they have pressed on the fretboard
   * Makes the game easier as the user can see which buttons they are pressing without looking at the guitar
   * @param pressedButton the button that has been pressed
   */
  public void displayNoteOn(int pressedButton) {
    view.noteDisplay(true, pressedButton);
  }

  /**
   * resetAll
   * Don't show any notes on the fretboard when no buttons are being clicked
   */
  public void resetAll() {
    view.noteDisplay(false, 6);
    view.noteCollected(false, 6);
  }

  /**
   * displayCollectedNote
   * Change the colour of the note on the fretboard when a note has been collected
   * @param guitarNote the note that has been collected
   */

  public void displayCollectedNote(String guitarNote) {
    for (int i = 0; i < LANES; i ++) {
      // 1 black, 2 white, 0 nothing
      int singleNote = (int) guitarNote.charAt(i) - ASCII_DIFF;

      // if 1 then it's black
      if (singleNote == 1) {
        int myNote = FIRST_BLACK + i * BUTTON_DIFF;
        view.noteDisplay(false, ALL_BUTTONS);
        view.noteCollected(true, notesToButtons.get(myNote));
      }
      // if 2 then it's white
      else if (singleNote == 2) {
        int myNote = FIRST_WHITE + i * BUTTON_DIFF;
        view.noteDisplay(false, ALL_BUTTONS);
        view.noteCollected(true, notesToButtons.get(myNote));
      }
    }
  }

}
