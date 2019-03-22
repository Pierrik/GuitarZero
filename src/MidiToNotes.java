import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.Map;

/**
 * MidiToNotes
 * Revisited class
 * Converts a MIDI file into a notes file
 *
 * @author Tom Mansfield
 * @author Kamila Hoffmann-Derlacka
 * @author Harper Ford
 *
 * @version 3.0, March 2019
 */
public class MidiToNotes {

  /**
   * Gets the amount of notes played by an instrument
   * @param seq   The sequence to be analysed
   * @param instrumentNumber    The instrument number to search for total of notes played by
   * @return    The number of notes played by the instrument in the song
   */

  final static int  FIRST_GUITAR     = 24;
  final static int  LAST_GUITAR      = 39;
  final static int  ZERO_POWER_TIME  = 10;
  final static int  SECONDS          = 60;
  final static int  BUTTONS          = 6;
  final static int  LANES            = 3;

  /**
   * getNotes
   * Gets the total amount of notes played by one instrument channel during a song
   * Loops through each track in the MIDI sequence and collects how many times a channel outputs a "Note On" message for the correct instrument
   * @param seq the MIDI sequence to check
   * @param instrumentNumber the number of the instrument to check for note occurrences
   * @return the total number of notes played by the instrument
   */
  public static int getNotes ( Sequence seq , int instrumentNumber ){
    // Total notes played by the instrument
    int totalNotes = 0;

    // Channel that the guitar is on
    int currentChannel = 0;

    Track[] trks = seq.getTracks();

    // Loops through all tracks in the song
    for( int i = 0; i < trks.length; i++) {
      // Get the notes for the instrument on each track
      for( int j = 0; j < trks[i].size(); j++){
        MidiEvent evt = trks[i].get( j );
        MidiMessage msg = evt.getMessage();
        if ( msg instanceof ShortMessage ) {
          final ShortMessage smsg = (ShortMessage) msg;
          final int chan = smsg.getChannel();
          final int cmd = smsg.getCommand();
          final int dat1 = smsg.getData1();

          switch ( cmd ) {

            case ShortMessage.PROGRAM_CHANGE :
              // Checks if the channel is set to the instrument looking for
              if ( dat1 == instrumentNumber ) {
                currentChannel = chan;
              }
              break;

            case ShortMessage.NOTE_ON :
              // If a note is played by the instrument being searched for, increment the total
              if( chan == currentChannel ) {
                totalNotes++;
              }
          }
        }
      }
    }

    return totalNotes;

  }

  /**
   * mostNotes
   * Finds the instrument that plays the most notes in the song using getNotes
   * Loop through each guitar instrument number
   * If a guitar plays more notes than the number of notes in highestNotesPlayed, set this value to the amount of notes the guitar plays
   * @param seq The sequence of the MIDI file
   * @return The program number of the instrument playing the most notes
   */
  public static int mostNotes ( Sequence seq) throws InvalidMIDIFileException {
    int instrumentNumber = 0;
    int highestNotesPlayed = 0;

    /* Guitar program numbers range from 24 to 39
     * Could filter through all instruments but would take longer to compute
     */
    try {
      for(int i = FIRST_GUITAR; i < LAST_GUITAR; i++) {
        // Get the amount of notes the instrument plays in the song
        int notes = getNotes( seq, i );

        // set highestNotesPlayed to this value if it is higher
        if ( notes > highestNotesPlayed ) {
          highestNotesPlayed = notes;

          // Set the instrument number as the program number of the guitar currently found to play the most notes
          instrumentNumber = i;
        }
      }
      if( instrumentNumber == 0){
        throw new InvalidMIDIFileException("Lead instrument not a guitar");
      }
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    return instrumentNumber;
  }

  /**
   * formatNote
   * Coverts a MIDI note to the correct format for the game note file
   * Finds the note number modulus with the amount of note buttons on the guitar
   * @param tick the tick that the note occurs on
   * @param n the number of the note
   * @param m the map to store the note
   */
  public static void formatNote( long tick, int n, Map<Long, String> m ) {

    final int note = n % BUTTONS;

    String format = "";

    // Each note in the song is formatted to a 3 digit number
    // Representing button 1 or button 2 in 3 lanes of guitar highway

    switch ( note ) {
      case 0: format = "100"; break;
      case 1: format = "200"; break;
      case 2: format = "010"; break;
      case 3: format = "020"; break;
      case 4: format = "001"; break;
      case 5: format = "002"; break;
    }
    // Combine notes where multiple notes occur at the same time
    // One note from each highway changed at a time
    if( m.containsKey( tick ) ){
      String b = m.get( tick ).toString();
      String val = compare( format, b );
      m.replace( tick, val );
    }
    else {
      m.put( tick, format );
    }
  }

  /**
   * Compares each digit in two 3-digit notes
   * Largest value for each digit is used for the new combined note
   * @param a A digit digit of the formatted note
   * @param b A digit of the formatted note
   * @return newNote
   */
  public static String compare( String a, String b ){
    String newNote = "";
    try {
      for ( int i = 0; i < LANES; i ++ ) {
        if ( Character.isDigit ( a.charAt( i ) ) && Character.isDigit( b.charAt( i ) ) ) {
          if ( Character.getNumericValue( a.charAt( i ) ) > Character.getNumericValue( b.charAt( i ) ) ){
            newNote = newNote + a.charAt(i);
          }
          else
          {
            newNote = newNote + b.charAt(i);
          }
        } else {
          throw new InvalidNoteException( "Note not composed of integers" );
        }
      }
    } catch (InvalidNoteException e) {
      e.printStackTrace();
    }
    return newNote;
  }

  /**
   * Creates a sorted map of formatted notes and their ticks
   * @param seq the sequence of the MIDI file
   * @param programNumber the program number of the instrument used on the track
   * @return a map of ticks and formatted notes
   */
  public static TreeMap<Long, String> createMap ( Sequence seq, int programNumber ) {
    // TreeMap stores the notes in order of ticks
    TreeMap<Long, String> m = new TreeMap<>();

    Track trks[] = seq.getTracks();

    int currentChannel = 0;

    // Loops through all tracks in the song
    for( int i = 0; i < trks.length; i++) {
      // Get the notes for the instrument on each track
      for( int j = 0; j < trks[i].size(); j++){
        MidiEvent evt = trks[i].get( j );
        MidiMessage msg = evt.getMessage();
        if ( msg instanceof ShortMessage ) {
          final long tick = evt.getTick();
          final ShortMessage smsg = (ShortMessage) msg;
          final int chan = smsg.getChannel();
          final int cmd = smsg.getCommand();
          final int dat1 = smsg.getData1();

          switch( cmd ) {
            case ShortMessage.PROGRAM_CHANGE :
              // if the note played is by the correct instrument
              if ( dat1 == programNumber ) {
                currentChannel = chan;
              }
              break;
            case ShortMessage.NOTE_ON :
              if( chan == currentChannel ){
                formatNote(tick, dat1, m);
              }
              break;
            default :
              break;
          }

        }
      }
    }
    return m;
  }

  /**
   * zeroPower
   * Determines when Zero Power Mode should start and end in the song
   * @param seq the MIDI sequence to analyse
   * @param notesMap the map of formatted notes to check
   */
  public static MyResult zeroPower( Sequence seq, TreeMap<Long, String> notesMap) {
    long start;
    long end;
    Map<Long, Integer> noteCount = new TreeMap<>();

    int bpm = Bpm.getBPM(seq);
    int ticksPerBeat = seq.getResolution();
    int ticksPerSec = (bpm*ticksPerBeat)/SECONDS;
    long range = ZERO_POWER_TIME * ticksPerSec;

    for (Map.Entry<Long, String> entry : notesMap.entrySet()) {
      start = entry.getKey();
      end = start + range;
      int count = 0;

      for (long i = start; i <= end; i ++) {
        String value = notesMap.get(i);
        if ( value != null) {
          count ++;
        }
      }
      noteCount.put(start, count);
    }

    start = Collections.max(noteCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    end = notesMap.floorEntry(start+range).getKey();

    return new MyResult(start, end);
  }

  /**
   * Writes the converted notes from the MIDI file to a text file
   * Adds notes that occur on a beat to the file
   * @param midiFilePath the file path of the MIDI file to be converted
   */
  public static void writeFile( String midiFilePath ) {
    try {
      Sequence seq = MidiSystem.getSequence( new File ( midiFilePath ) );
      TreeMap<Long, String> map = createMap( seq, mostNotes(seq));
      MyResult ticks = zeroPower(seq, map);
      boolean zeroStarted = false;
      boolean zeroEnded = false;

      // Amount of ticks that occur for each beat of the song (Pulse Per Quarter note)
      int ticksPerBeat = seq.getResolution();

      File file = new File( "noteFile.txt");
      // Clear the contents of the file if exists
      PrintWriter clear = new PrintWriter(file);
      clear.close();
      PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter( file, true ) ) );
      for (Map.Entry<Long, String> entry : map.entrySet()) {
        /* Only add note to file if it occurs on a beat
         * Filters notes out and makes the game easier to play
         */
        if (entry.getKey() % ticksPerBeat == 0) {
          // Add note to the note file
          if (ticks.getFirst() <= entry.getKey() && !zeroStarted) {
            zeroStarted = true;
            out.println(entry.getKey() + "," + entry.getValue() + ",1");
          } else if (ticks.getSecond() <= entry.getKey() && !zeroEnded) {
            zeroEnded = true;
            out.println(entry.getKey() + "," + entry.getValue() + ",1");
          } else {
            out.println(entry.getKey() + "," + entry.getValue() + ",0");
          }
        }
      }
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  public static final class MyResult {
    private final long first;
    private final long second;

    public MyResult(long first, long second) {
      this.first = first;
      this.second = second;
    }

    public long getFirst() {
      return first;
    }

    public long getSecond() {
      return second;
    }
  }

}
