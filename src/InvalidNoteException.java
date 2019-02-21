/**
 * InvalidNoteException
 * Can be thrown when a note is formatted incorrectly
 * @author Tom Mansfield
 */
public class InvalidNoteException extends Exception {

  public InvalidNoteException(String message){
    super(message);
  }

}
