/**
 * NoPictureException.
 * Can be thrown when no pictures are found in the local store directory.
 *
 * @author Pierrik Mellab
 * @version 1.0, March 2019
 */
public class NoPictureException extends Exception {
  public NoPictureException(String message){
    super(message);
  }
}