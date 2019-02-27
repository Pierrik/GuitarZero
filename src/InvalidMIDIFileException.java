/**
 * Can be thrown when a selected MIDI file is not suitable for conversion for the game
 */
public class InvalidMIDIFileException extends Exception {
  public InvalidMIDIFileException(String message){
    super(message);
  }
}
