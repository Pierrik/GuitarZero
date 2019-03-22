/**
 *
 * @author Tom Mansfield
 * @version 1.0, March 2019
 *
 * Can be thrown when a selected MIDI file is not suitable for conversion for the game
 */
public class InvalidMIDIFileException extends Exception {
  public InvalidMIDIFileException(String message){
    super(message);
  }
}
