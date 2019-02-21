import java.io.*;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.Map;
import java.util.HashMap;

/**
 * MidiTo Notes
 * Original class, now unused.
 *
 * @author  Tom Mansfield
 * @author Kamila Hoffmann-Derlacka
 * @author  Harper Ford
 * @version 1.00, February 2019.
 */

/**
 * Class that converts a MIDI file to a note file.
 */
public class MidiToNotesOld {

  static int newChannel = 0;

  /**
   * Coverts a MIDI note to the correct format for the game note file
   * @param tick
   * @param n
   * @param m
   */
  public static void formatNote( long tick, int n, Map<Long, String> m ) {

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
  public static String compare( String a, String b ){
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
   * Formats each note in a track and writes to a text file
   * @param trk
   */
  public static void writeTrack( Track trk ) {
    //Table of ticks and formatted notes
    Map<Long, String> map = new HashMap<>();

    for ( int i = 0; i < trk.size(); i = i + 1 ) {
      MidiEvent evt = trk.get( i );
      MidiMessage msg = evt.getMessage();
      if ( msg instanceof ShortMessage ) {
        final long tick = evt.getTick();
        final ShortMessage smsg = (ShortMessage) msg;
        final int chan = smsg.getChannel();
        final int cmd = smsg.getCommand();
        final int dat1 = smsg.getData1();

        switch( cmd ) {
          case ShortMessage.PROGRAM_CHANGE :
            // if instrument is electric guitar
            if ( dat1 == 27 ) {
              newChannel = chan;
            }
            break;
          case ShortMessage.NOTE_ON :
            if( chan == newChannel ){
              formatNote(tick, dat1, map);
            }
            break;
          default :
            break;
        }
      }
    }
    // write the table of notes to the text file
    try {
      File file = new File ("noteFile.txt");
      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

      for (Map.Entry<Long, String> entry : map.entrySet()) {
        out.println(entry.getKey() + "," + entry.getValue());
      }

      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the tracks in the MIDI sequence and calls writeTrack on each one
   * @param seq
   */
  public static void findTracks(Sequence seq ) {
    Track[] trks = seq.getTracks();

    try {
      for ( int i = 0; i < trks.length; i++ ) {
        writeTrack( trks[ i ] );
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Calls function to convert MIDI file to note file and gives error if unsuccessful
   * @param midiFilePath
   */
  public static void writeFile( String midiFilePath ) {
    try {
      Sequence seq = MidiSystem.getSequence( new File( midiFilePath ) );
      findTracks( seq );
    } catch ( Exception e ) {
      e.printStackTrace();

    }
  }
}

