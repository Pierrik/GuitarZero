/**
 * InvalidNoteException
 * Can be thrown when a note is formatted incorrectly
 *
 * @author Tom Mansfield
 * @version 1.0, March 2019
 */
public class InvalidNoteException extends Exception {

  public InvalidNoteException(String message){
    super(message);
  }

}
