import javax.sound.midi.*;

/**
 * BPM.
 *
 * @author  Harper Ford
 * @version 1.00, March 2019.
 */

public class Bpm {
  /*
   * Returns the BPM of the given MIDI sequence
   */

	final static int SECONDS = 60000000;

  public static int getBPM(Sequence sequence){
    try {

      //Get sequence from file
      return(SECONDS / getMicrosecondsPerQuarterNote(sequence));
    }catch (Exception e) {
    	e.printStackTrace();
		}

		return 0;
  }

  /*
   * Returns the microseconds per quarter note of a given sequence
   */
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
  					// returns mpqm
  				  return ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
  				}
  			}
  		}
  	}
  	return 0;
  }
}
