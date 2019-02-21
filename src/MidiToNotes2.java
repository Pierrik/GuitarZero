import java.io.*;
import java.util.TreeMap;
import javax.sound.midi.MetaMessage;
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
 * @author Tom Mansfield
 * @version 2.2, February 2019
 */
public class MidiToNotes2 {


  /**
   * Gets the amount of notes played by an instrument
   * @param seq   The sequence to be analysed
   * @param instrumentNumber    The instrument number to search for total of notes played by
   * @return    The number of notes played by the instrument in the song
   */
  private static int getNotes ( Sequence seq , int instrumentNumber ){
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
              if ( dat1 == instrumentNumber ) {
                currentChannel = chan;
              }
              break;

            case ShortMessage.NOTE_ON :
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
   * Finds the instrument that plays the most notes in the song
   * @param seq The sequence of the MIDI file
   * @return The program number of the instrument playing the most notes
   */
  private static int mostNotes ( Sequence seq) {
    int instrumentNumber = 0;
    int highestNotesPlayed = 0;

    /* Guitar program numbers range from 24 to 39
     * Could filter through all instruments but would take longer to compute
     */
    try {
      for(int i = 24; i < 39; i++) {
        // Get the amount of notes the instrument plays in the song
        int notes = getNotes( seq, i );
        if ( notes > highestNotesPlayed ) {
          highestNotesPlayed = notes;
          instrumentNumber = i;
        }
      }
      if( instrumentNumber == 0){
        throw new InvalidMIDIFileException("Main instrument not a guitar");
      }
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    return instrumentNumber;
  }

  /**
   * Coverts a MIDI note to the correct format for the game note file
   * @param tick
   * @param n
   * @param m
   */
  private static void formatNote( long tick, int n, Map<Long, String> m ) {

    final int note = n % 6;

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
   * @param a
   * @param b
   * @return newNote
   */
  private static String compare( String a, String b ){
    String newNote = "";
    try {
      for ( int i = 0; i < 3; i ++ ) {
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
  private static Map<Long, String> createMap (Sequence seq, int programNumber ) {
    // TreeMap stores the notes in order of ticks
    Map<Long, String> m = new TreeMap<>();

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

  private static int getBPM(Sequence seq) {
    // Check all MIDI tracks for MIDI_SET_TEMPO message
    int microseconds;
    for (Track track : seq.getTracks()) {
      for (int i = 0; i < track.size(); i++) {
        MidiEvent event = track.get(i);
        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
          MetaMessage m = (MetaMessage) message;
          byte[] data = m.getData();
          int type = m.getType();
          if (type == 0x51) { // [0x51]MIDI_SET_TEMPO
            microseconds = ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
            return 60000000 / microseconds;
          }
        }
      }
    }
    return 0;
  }

  /**
   * Writes the converted notes from the MIDI file to a text file
   * @param midiFilePath
   */
  private static void writeFile( String midiFilePath ) {
    try {
      Sequence seq = MidiSystem.getSequence( new File ( midiFilePath ) );
      Map<Long, String> map = createMap( seq, mostNotes(seq));

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
          out.println(entry.getKey() + "," + entry.getValue());
        }
      }
      out.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  public static void main ( String args[] ) {
    writeFile("C:\\Users\\tomma\\Documents\\GuitarZero\\AC_DC_-_Back_In_Black.mid");
  }
}