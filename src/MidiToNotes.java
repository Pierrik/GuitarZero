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
 * Convert MIDI file to note file.
 *
 * Version 1 Harper
 * Version 2 Kamilla
 * Version 3 Tom
 * Version 4 John
 */
public class MidiToNotes {
    String file;
    MidiToNotes(String file){
        this.file = file;
    }

    final static String FILE = "C:\\Users\\tomma\\Desktop\\GuitarZero\\AC_DC_-_Back_In_Black.mid";


    public static void formatNote(long tick, int n, Map<Long, String> m) {
        final int note = n % 6;
        String format = "";
        switch ( note ) {
            case 0: format = "100"; break;
            case 1: format = "200"; break;
            case 2: format = "010"; break;
            case 3: format = "020"; break;
            case 4: format = "001"; break;
            case 5: format = "002"; break;
        }
        //Combine
        if(m.containsKey(tick)){
            String b = m.get(tick).toString();
            String val = compare(format, b);
            m.replace(tick, val);
        }
        else {
            m.put(tick,format);
        }
        //return formatted note e.i. "020"
    }

    public static String compare(String a, String b){
        String newNote = "";
        for (int i = 0; i < 3; i ++) {
            if (Character.getNumericValue(a.charAt(i)) > Character.getNumericValue(b.charAt(i))) {
                newNote = newNote + a.charAt(i);
            }
            else
            {
                newNote = newNote + b.charAt(i);
            }
        }
        return newNote;
    }


    static int newChannel = 0;
    public static void displayTrack( Track trk ) {
        //Table of ticks and final notes
        Map<Long, String> map = new HashMap<>();

        for ( int i = 0; i < trk.size(); i = i + 1 ) {
            MidiEvent   evt  = trk.get( i );
            MidiMessage msg = evt.getMessage();
            if ( msg instanceof ShortMessage ) {
                final long         tick = evt.getTick();
                final ShortMessage smsg = (ShortMessage) msg;
                final int          chan = smsg.getChannel();
                final int          cmd  = smsg.getCommand();
                final int          dat1 = smsg.getData1();

                switch( cmd ) {
                    case ShortMessage.PROGRAM_CHANGE :
                        if (dat1 == 27) {
                            newChannel = chan;
                        }
                        break;
                    case ShortMessage.NOTE_ON :
                        if(chan == newChannel){
                            formatNote(tick, dat1, map);
                        }
                        break;
                    default :
                        /* ignore other commands */
                        break;
                }
            }
        }
        //WRITE LINKED LIST TO FILE
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
     * Display a MIDI sequence.
     */
    public static void displaySequence( Sequence seq ) {
        Track[] trks = seq.getTracks();

        for ( int i = 0; i < trks.length; i++ ) {
            displayTrack( trks[ i ] );
        }
    }

    public static void writeFile( String file ) {
        try {
            Sequence seq = MidiSystem.getSequence( new File( file ) );
            displaySequence( seq );
        } catch ( Exception exn ) {
            System.out.println( exn ); System.exit( 1 );
        }
    }
}