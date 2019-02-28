import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.Map;
import java.util.TreeMap;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;
import java.io.FilenameFilter;

/**
 * PlayModeModel
 * Version 1.1
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

  public PlayModeModel( String bundlePath ) {
    this.bundlePath = bundlePath;
    this.multiplier = 1;
    this.streakCount = 0;
    this.totalCurrency = loadTotalCurrency();
    this.currencyEarned = 0;
    this.score = 0;
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

  public Map<Long, String> loadNotesFile() {
    Map<Long, String> m = null;
    try {
      BufferedReader br = new BufferedReader( new FileReader(this.notesFile));
      String line = null;
      m = new TreeMap<>();

      while((line = br.readLine())!=null) {
        String str[] = line.split(",");
        m.put(Long.parseLong(str[0]), str[1]);
      }

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return m;
  }

  public void collectNote() {
    this.streakCount ++;
    setMultiplier();
    this.score += this.multiplier;
    updateCurrency();
  }

  public void dropNote() {
    this.streakCount = 0;
    this.multiplier = 1;
  }

  public void setMultiplier() {
    if( streakCount % 10 == 0 ) {
      this.multiplier = (int) Math.pow( 2, streakCount/10 );
    }
    else if( streakCount == 0 ) {
      this.multiplier = 1;
    }
  }

  public void updateCurrency() {
    if( currencyEarned < 5 ) {
      if( score % 500 == 0) {
        currencyEarned ++;
      }
    }
  }

  /**
   * Play the MIDI song
   * Code from workshop
   */
  public void playSong() {
    try {
      final Sequencer seq = MidiSystem.getSequencer();
      final Transmitter trans  = seq.getTransmitter();

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
    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }
  }


  // Temporary main to test methods
  public static void main(String args[]) {
    PlayModeModel playMode = new PlayModeModel("C:\\Users\\tomma\\Documents\\GuitarZero");
    //System.out.println(playMode.findNotesFile().getPath());
    //playMode.playSong();
    Map<Long, String> m = playMode.loadNotesFile();
    for (Map.Entry<Long, String> entry : m.entrySet()) {

      System.out.println(entry.getKey() + "," + entry.getValue());

    }
  }


}
