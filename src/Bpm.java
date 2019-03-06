import javax.sound.midi.*;
import java.io.File;

/**
 * BPM.
 *
 * @author  Harper Ford
 * @version 1.00, February 2019.
 */

public class Bpm {

  public static int getBPM(Sequence sequence){
    try {
      //Open sequencer
      Sequencer sequencer = MidiSystem.getSequencer();
      sequencer.open();

      //Get sequence from file
      return(60000000 / getMicrosecondsPerQuarterNote(sequence));
    }catch (Exception e) {
    	e.printStackTrace();
		}
		return 0;
  }

  private static int getMicrosecondsPerQuarterNote(Sequence sequence) {
  	// Check all MIDI tracks for MIDI_SET_TEMPO message
  	for (Track track : sequence.getTracks()) {
  		for (int i = 0; i < track.size(); i++) {
  			MidiEvent event = track.get(i);
  			MidiMessage message = event.getMessage();
  			if (message instanceof MetaMessage) {
  				MetaMessage m = (MetaMessage) message;
  				byte[] data = m.getData();
  				int type = m.getType();
  				if (type == 0x51){ //[0x51]MIDI_SET_TEMPO
  					return ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
  				}
  			}
  		}
  	}
  	return 0;
  }
}
