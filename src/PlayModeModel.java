import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.HashMap;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;
import java.io.FilenameFilter;

/**
 * PlayModeModel
 * Version 2.1, February 2019
 * @author Tom Mansfield
 */

public class PlayModeModel {
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
  private HashMap<Long, String> notesCollected;
  private boolean endOfSong;
  private PlayModeView view;

  public PlayModeModel( String bundlePath, PlayModeView view ) {
    this.view = view;
    this.bundlePath = bundlePath;
    this.multiplier = 1;
    this.streakCount = 0;
    this.totalCurrency = loadTotalCurrency();
    this.currencyEarned = 0;
    this.score = 0;
    this.currentTick = 0;
    try {
      this.notesFile = findNotesFile();
    } catch (NotesFileNotFoundException e) {
      e.printStackTrace();
      // Exit and go back to slash mode
    }
    try {
      this.midiFile = findMidiFile();
    } catch (MidiFileNotFoundException e) {
      e.printStackTrace();
      // Exit and go back to slash mode
    }
    try {
      this.coverArt = findCoverArt();
    } catch (CoverArtNotFoundException e) {
      e.printStackTrace();
      // Exit and go back to slash mode
    }
    this.notes = new HashMap<>();
    loadNotesFile();
    this.currentNote = "";
    this.endOfSong = false;
  }

  public long getCurrentTick() {
    return this.currentTick;
  }

  public HashMap<Long, String> getNotes() {
    return this.notes;
  }

  public HashMap<Long, String> getNotesCollected() {
    return this.notesCollected;
  }

  public boolean isEndOfSong() {
    return endOfSong;
  }

  public String getCurrentNote() {
    return this.currentNote;
  }

  /**
   * findNotesFile
   * @return the notes file in the bundle
   * @throws NotesFileNotFoundException when notes file is not found
   */
  public File findNotesFile() throws NotesFileNotFoundException {

    File bundle = new File(this.bundlePath);

    // Find text files in the directory
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".txt");
      }
    });

    if ( files.length > 0 ) {
      return files[0];
    } else {
      throw new NotesFileNotFoundException("No Notes File In Bundle");
    }
  }

  /**
   * findMidiFile
   * @return the MIDI file in the bundle
   * @throws MidiFileNotFoundException when MIDI file is not found
   */
  public File findMidiFile() throws MidiFileNotFoundException {

    File bundle = new File(this.bundlePath);

    // Find MIDI files in the directory
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".mid");
      }
    });

    if ( files.length > 0 ) {
      return files[0];
    } else {
      throw new MidiFileNotFoundException("No MIDI File In Bundle");
    }
  }

  /**
   * findCoverArt
   * @return the cover art file in the bundle
   * @throws CoverArtNotFoundException when a cover art file cannot be found
   */
  public File findCoverArt() throws CoverArtNotFoundException {

    File bundle = new File(this.bundlePath);

    // Find MIDI files in the directory
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png");
      }
    });

    if ( files.length > 0 ) {
      return files[0];
    } else {
      throw new CoverArtNotFoundException("No Cover Art In Bundle");
    }
  }


  public int loadTotalCurrency() {
    // Get the user's currency from a text file
    return 0;
  }

  /**
   * loadNotesFile
   * Reads the notes file in the bundle and adds notes to a map
   */
  public void loadNotesFile() {
    try {
      BufferedReader br = new BufferedReader( new FileReader(this.notesFile));
      String line = null;

      while((line = br.readLine())!=null) {
        String str[] = line.split(",");
        this.notes.put(Long.parseLong(str[0]), str[1]);
      }

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * collectNote
   * Occurs when the user plays a correct note
   * Updates the streakCount, Multiplier and currencyEarned if necessary
   */
  public void collectNote() {
    this.notesCollected.put(Long.valueOf(currentTick), "collected");
    this.streakCount ++;
    setMultiplier();
    this.score += this.multiplier;
    updateCurrency();
  }

  /**
   * dropNote
   * Occurs when the user does not play a correct note or plays a note when no note should be played
   * Resets the streakCount and multiplier
   */
  public void dropNote() {
    this.streakCount = 0;
    this.multiplier = 1;
  }

  /**
   * setMultiplier
   * changes the value of the multiplier if streakCount is multiple of 10 or 0
   */
  public void setMultiplier() {
    if( streakCount % 10 == 0 ) {
      this.multiplier = (int) Math.pow( 2, streakCount/10 );
    }
    else if( streakCount == 0 ) {
      this.multiplier = 1;
    }
  }

  /**
   * updateCurrency
   * updates the current currency earned during the song
   */
  public void updateCurrency() {
    if( currencyEarned < 5 ) {
      if( score % 500 == 0) {
        currencyEarned ++;
      }
    }
  }

  /**
   * Play the MIDI song
   * Sets current tick and current note values as the song is played
   */
  public void playSong() {
    try {
      final Sequencer seq = MidiSystem.getSequencer();
      final Transmitter trans  = seq.getTransmitter();
      long currentTick;
      AddNoteToHighway addNoteToHighway= new AddNoteToHighway(this, view);

      seq.open();

      seq.setSequence( MidiSystem.getSequence( midiFile ) );

      seq.addMetaEventListener( new MetaEventListener() {
        public void meta( MetaMessage msg ) {
          if ( msg.getType() == 0x2F ) {
            seq.close();
          }
        }
      });

      seq.start();
      addNoteToHighway.run();


      // Set the current tick pointer to the current tick of the song
      while(seq.isRunning()){
        currentTick = seq.getTickPosition();
        this.currentTick = currentTick;
        changeCurrentNote(currentTick);

      }

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }
    this.endOfSong = true;
  }

  /**
   * changeCurrentNote
   * changes the current note to the note that should be played
   * @param tick the current tick
   */
  public void changeCurrentNote(long tick) {
    if(this.notes.get(Long.valueOf(tick))!=null){
      this.currentNote = this.notes.get(Long.valueOf(tick));
    }
    else{
      this.currentNote = "000";
    }
  }

  /**
   * checkCollected
   * Checks that a note that should've been played was collected
   * Otherwise, drop the note
   */
  public void checkCollected(){
    if(this.notes.get(Long.valueOf(currentTick))!=null){
      if(this.notesCollected.get(Long.valueOf(currentTick))!= "collected") {
        dropNote();
      }
    }
  }

  /**
   * checkNote
   * checks whether a note played on the guitar is correct
   * collect note if correct, otherwise drop the note
   * @param guitarNote the note played on the guitar
   */
  public void checkNote(String guitarNote) {
    if( this.currentNote != "000" ) {
      if(guitarNote.equals(this.currentNote)) {
        collectNote();
      }
      else {
        dropNote();
      }
    }
    else {
      // No note should have been played, end streak and reset multiplier
      dropNote();
    }

  }


  // Temporary main to test methods
  /*public static void main(String args[]) {
    PlayModeModel playMode = new PlayModeModel("C:\\Users\\tomma\\Documents\\GuitarZero\\testBundle");
    //System.out.println(playMode.findNotesFile().getPath());
    //playMode.playSong();

    //for (Map.Entry<Long, String> entry : playMode.notes.entrySet()) {

      //System.out.println(entry.getKey() + "," + entry.getValue());

    //}

    playMode.playSong();

    //System.out.println(playMode.notesFile.getName());
  }*/


}
