/**
 * NoSongsException.
 * Can be thrown when no songs are found in the local store directory.
 *
 * @author John Mercer
 * @version 1.0, March 2019
 */
public class NoSongsException extends Exception {
  public NoSongsException(String message){
    super(message);
  }
}