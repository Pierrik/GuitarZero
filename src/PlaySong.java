import java.io.File;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;

/**
 * PlaySong
 * Plays a MIDI and stores the current tick of the song
 * @author Tom Mansfield
 */
public class PlaySong implements Runnable {
  public File midiFile;
  public String currentNote;
  public long currentTick;
  public boolean endOfSong;

  public PlaySong(File file){
    this.midiFile = file;
    this.currentNote="";
    this.currentTick = 0;
    this.endOfSong = false;
  }

  @Override
  public void run() {
    try {
      final Sequencer seq = MidiSystem.getSequencer();
      final Transmitter trans  = seq.getTransmitter();
      long currentTick;
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


      // Set the current tick pointer to the current tick of the song
      while(seq.isRunning()){
        currentTick = seq.getTickPosition();
        this.currentTick = currentTick;

      }
      seq.stop();
      this.endOfSong = true;
    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }

  }


}

