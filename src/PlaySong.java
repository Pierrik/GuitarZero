import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;

/**
 * Run midi file in different Thread
 * Version 2.1, February 2019
 * @author Tom Mansfield
 * @author Harper Ford
 */

public class PlaySong implements Runnable {
  public File midiFile;
  public String currentNote;
  public long currentTick;
  public AtomicBoolean endOfSong;
  public int ticksPerBeat;
  public int bpm;
  public int time;
  public double x = 1.49;

  public AtomicBoolean song_running = new AtomicBoolean(false);

  public PlaySong(File file){
    this.midiFile = file;
    this.currentNote="";
    this.currentTick = 0;
    this.endOfSong = new AtomicBoolean(false);
  }

  @Override
  /*
   * Threaded function that starts the MIDI sequence and then adds Notes to the highway in PlayModeView
   */
  public void run() {
    try {
      final Sequencer seq = MidiSystem.getSequencer();
      final Transmitter trans = seq.getTransmitter();
      this.ticksPerBeat = MidiSystem.getSequence(midiFile).getResolution();
      //System.out.println(this.bpm);
      long currentTick;
      seq.open();
      seq.setSequence(MidiSystem.getSequence(midiFile));
      seq.addMetaEventListener(new MetaEventListener() {
        public void meta(MetaMessage msg) {
          if (msg.getType() == 0x2F) {
            seq.close();
          }
        }
      });
      seq.start();

      try {
        //Calculate how many frames a note travels per second
        bpm = Bpm.getBPM(MidiSystem.getSequence(midiFile));
        time = (bpm * ticksPerBeat) / 60;
      } catch (Exception e) {
      }
      // Set the current tick pointer to the current tick of the song
      song_running.set(true);
      while (seq.isRunning() && song_running.get()) {
        currentTick = seq.getTickPosition() + Math.round(time * x);
        this.currentTick = currentTick;
        //System.out.println(this.currentTick);
      }
      seq.stop();
      this.endOfSong.set(true);
    } catch (Exception exn) {
      System.out.println(exn);
      System.exit(1);
    }

  }
}
