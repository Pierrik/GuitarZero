/**
 * NoSongsException.
 *
 * Can be thrown when no songs are found in the local store directory.
 *
 */
public class NoSongsException extends Exception {
  public NoSongsException(String message){
    super(message);
  }
}