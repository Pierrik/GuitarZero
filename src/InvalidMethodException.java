/**
 * InvalidMethodException.
 *
 * Can be thrown when an invalid method is provided.
 *
 * Valid methods are as follows:
 * Download - DOWNLOAD_BUNDLE, DOWNLOAD_PREVIEW
 * Upload   - UPLOAD_BUNDLE, UPLOAD_PREVIEW
 */
public class InvalidMethodException extends Exception {
  public InvalidMethodException(String message){
    super(message);
  }
}
