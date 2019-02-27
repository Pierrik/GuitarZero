import java.io.*;
import java.net.Socket;
/**
 * MockClient.
 *
 * @author  John Mercer
 * @author  Harper Ford (Javadoc)
 * @version 1.00, February 2019.
 */
public class MockClient {
  String host;
  int    port;

  MockClient(String host, int port){
    this.host = host;
    this.port = port;
  }

  /**
   * Uploads the given bundle to the server
   * @param fileName: The filepath of the file to upload
   */
  public void uploadFile(String fileName, String method){
    try {
      Socket sck = new Socket( this.host, this.port );

      File file = new File(fileName);
      byte[] bytes = new byte[(int) file.length()];

      DataInputStream dataIn = new DataInputStream(new FileInputStream(file));
      DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      dataIn.readFully(bytes, 0, bytes.length);

      dataOut.writeUTF(method + fileName);
      dataOut.write(bytes, 0, bytes.length);
      dataOut.flush();

      dataOut.close();
      sck.close();

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }
  }



  /**
   * Downloads the given file to the server
   * @param fileName: The filepath of the file to download
   */
  public void downloadFile(String fileName){
    try {
      Socket sck = new Socket( this.host, this.port );

      // Requesting to download the file
      DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      dataOut.writeUTF("DOWNLOAD/" + fileName);
      dataOut.flush();

      // Attempting to receive the file
      DataInputStream  dataIn  = new DataInputStream(sck.getInputStream());
      BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(fileName));

      byte[] bytes = dataIn.readAllBytes();
      fileOut.write(bytes);

      fileOut.close();
      sck.close();

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }

  }
}
