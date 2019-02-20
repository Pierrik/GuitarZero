import javax.sound.midi.*;
import java.io.File;

public class bpm {
  final static String FILE = "AC.mid";
  static public void main(String[] args){
    try {
      //Open sequencer
      Sequencer sequencer = MidiSystem.getSequencer();
      sequencer.open();

      //Get sequence from file
      Sequence seq = MidiSystem.getSequence(new File(FILE));
      System.out.println(60000000 / getMicrosecondsPerQuarterNote(seq));
    }catch (Exception e) {}
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
