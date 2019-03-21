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
  private int errors;
  PlaySong playSong;
  // a map of notes to controller buttons' values (the same for all OS)
  Map<Integer, Integer> notesToButtons = new HashMap<Integer, Integer>() {{
    put(0, 0); put(1, 1); put(2, 4); put(3, 2); put(4, 5); put(5, 3);
  }};

  // Constants
  private static final String ZERO_POWER_PATH = "../assets/ZeroPowerShield.png";
  private static final String MULTIPLIER2 = "../assets/times2Multiplier3.png";
  private static final String MULTIPLIER4 = "../assets/times4Multiplier3.png";
  private static final String MULTIPLIER8 = "../assets/times8Multiplier3.png";
  private static final String MULTIPLIER16 = "../assets/times16Multiplier3.png";
  private static final String MULTIPLIER32 = "../assets/times32Multiplier3.png";
  private static final String MULTIPLIER64 = "../assets/times64Multiplier3.png";
  private static final String TXT_EXTENSION = ".txt";
  private static final String PNG_EXTENSION = ".png";
  private static final String MIDI_EXTENSION = ".mid";
  private static final String CURRENCY_PATH = "../currency/currency.txt";
  private static final String EMPTY_NOTE = "000";
  private static final int END_OF_GAME_DELAY = 5000;
  private static final int MAX_CURRENCY = 5;
  private static final int CURRENCY_SCORE = 500;




  // Constructor
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

    try {
      this.notesFile = findNotesFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    try {
      this.midiFile = findMidiFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    try {
      this.coverArt = findCoverArt();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    try {
      this.currencyFile = findCurrencyFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    this.notes = new HashMap<>();
    loadNotesFile();

    try {
      loadCurrencyFile();
    } catch (Exception e) {
      e.printStackTrace();
      startGame = false;
      errors ++;
    }

    if (errors == 0 ) {
      this.startGame = true;
    }
  }


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
      //this.multiplierLabel.setVisible(true);
      this.multiplier = (int) Math.pow(2, this.streakCount/10);
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

      throw new Exception("No Cover Art In Bundle");

    }

  }

  public File findCurrencyFile() throws Exception {
    File bundle = new File("../currency");

    // Find PNG files in the bundle
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
      File currencyFile = new File(CURRENCY_PATH);
      currencyFile.createNewFile();
      return currencyFile;
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
      boolean hasStarted = false;
      boolean hasEnded = false;

      while((line = br.readLine())!=null) {
        // Split notes file by comma, separating ticks and notes
        String str[] = line.split(",");
        this.notes.put(Long.parseLong(str[0]), str[1]);

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
   * Reads the currency file and updates the total currency
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
   * @throws Exception
   */
  public void saveCurrencyFile() throws Exception {
    FileWriter writer = new FileWriter(this.currencyFile.getName());
    writer.write(Integer.toString(this.totalCurrency));
    writer.close();
  }

  /**
   * playSong
   * Play the MIDI song
   * Sets current tick and current note values as the song is played
   */
  public void playSong() {

    // Plays the MIDI song in a separate thread
    this.playSong = new PlaySong(this.midiFile);
    Thread playSongThread = new Thread(this.playSong);
    playSongThread.start();
    boolean hasZeroStarted = false;
    boolean hasZeroEnded = false;

    // While the song is still playing
    while(!playSong.endOfSong){

      // Change the current tick and current note values
      currentTick = playSong.currentTick;
      this.currentNote = changeCurrentNote(currentTick);

      if (!hasZeroStarted && currentTick >= startZeroPower) {
        view.setZeroPowerLabelVisible();
        hasZeroStarted = true;
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

    if(playSong.endOfSong) {
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
        // Go back to slash mode when the song is over
        Run.changeMode(Mode.SLASH);
      }
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
      return EMPTY_NOTE;
    }
  }

  /**
   * checkNote
   * Called by the controller, checks whether a note played on the guitar is correct
   * Collect note if correct, otherwise drop the note
   * @param guitarNote the note played on the guitar
   */
  public void checkNote(String guitarNote) {

    //boolean noteCollected = false;

    try {

      // Checks if the user has pressed the note that is at the bottom of the screen
      if(this.noteToPlay.equals(guitarNote) && !view.notes.get(0).collected && view.notes.get(0).noteValue.equals(noteToPlay) && view.notes.get(0).getY()>390){
        displayCollectedNote(guitarNote);
        collectNote();
        view.notes.get(0).collect();
      }
      else{
        dropNote();
      }

    } catch (Exception e) {

      // If there is an error, continue game and don't crash
      // Very rare but may need to revisit how this is dealt with
      dropNote();

    }

  }

  /**
   * collectNote
   * Occurs when the user plays a correct note
   * Updates the streakCount, Multiplier and currencyEarned if necessary
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

  public void displayNoteOn(int pressedButton) {
    view.noteDisplay(true, pressedButton);
  }

  public void resetAll() {
    view.noteDisplay(false, 6);
    view.noteCollected(false, 6);
  }

  public void displayCollectedNote(String guitarNote) {
    for (int i = 0; i < 3; i ++) {
      // 1 black, 2 white, 0 nothing
      int singleNote = (int) guitarNote.charAt(i) - 48;
      if (singleNote == 1) {
        // then black i
        int myNote = 1 + i * 2;
        view.noteDisplay(false, 6);
        view.noteCollected(true, notesToButtons.get(myNote));
      } else if (singleNote == 2) {
        // then white i
        int myNote = 0 + i * 2;
        view.noteDisplay(false, 6);
        view.noteCollected(true, notesToButtons.get(myNote));
      }
    }
  }

}
