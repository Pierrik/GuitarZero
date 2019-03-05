import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * MockClient.
 *
 * @author  John Mercer
 * @author  Harper Ford (Javadoc)
 * @version 2.01, February 2019.
 */
public class MockClient {
  /**
   * Uploads the given bundle to the server
   * @param fileName: The filepath of the file to upload
   * @param method: Upload method (UPLOAD_BUNDLE or UPLOAD_PREVIEW)
   */
  public static void uploadFile(String host, int port, String fileName, String method){
    try {
      Socket sck = new Socket(host, port);

      // instantiating byte array of correct size to store file
      File file = new File(fileName);
      byte[] bytes = new byte[(int) file.length()];

      // streams for reading/writing the file
      DataInputStream dataIn = new DataInputStream(new FileInputStream(file));
      DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      // reading the file into the byte array
      dataIn.readFully(bytes, 0, bytes.length);

      // writing the file to the socket output stream (from the byte array)
      dataOut.writeUTF(method + "/" + fileName);
      dataOut.writeLong(file.length());
      dataOut.write(bytes, 0, bytes.length);
      dataOut.flush();

      // cleaning up
      dataOut.close();
      sck.close();

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }
  }

  /**
   * Downloads the given file from the server
   * @param fileName: The filepath of the file to download
   * @param method: Download method (DOWNLOAD_BUNDLE or DOWNLOAD_PREVIEW)
   */
  public static void downloadFile(String host, int port, String fileName, String method){
    try {
      // checking if local_store directory exists, and creates it if it doesn't yet
      String cd = System.getProperty("user.dir");
      String bundleDir = cd + "local_store\\bundle_files\\";
      String previewDir = cd + "local_store\\preview_files\\";

      if (Files.notExists(Paths.get(bundleDir))) {
        Files.createDirectories(Paths.get(bundleDir));
      }
      if (Files.notExists(Paths.get(previewDir))) {
        Files.createDirectories(Paths.get(previewDir));
      }

      // requesting to download the file
      Socket sck = new Socket(host, port);
      DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      dataOut.writeUTF(method + "/" + fileName);
      dataOut.flush();

      // attempting to receive the file
      DataInputStream  dataIn  = new DataInputStream(sck.getInputStream());
      BufferedOutputStream fileOut;

      if (method.equals("DOWNLOAD_BUNDLE")) {
         fileOut = new BufferedOutputStream(new FileOutputStream(bundleDir));
      }
      else if (method.equals("DOWNLOAD_PREVIEW")) {
        fileOut = new BufferedOutputStream(new FileOutputStream(previewDir));
      }
      else {
        return;       // returning nothing if method is invalid
      }

      byte[] bytes = dataIn.readAllBytes();
      fileOut.write(bytes);

      // cleaning up
      fileOut.close();
      sck.close();

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }

  }

  /**
   * Requests a directory listing of the previews directory on the server
   * @return ArrayList: ArrayList of filenames (songs available to buy)
   */
  /*
  public static ArrayList<String> listDirectory(String host, int port){
    try{
      // requesting the directory listing
      Socket sck = new Socket(host, port);
      ObjectOutputStream objOut = new ObjectOutputStream(sck.getOutputStream());

      objOut.writeUTF("LIST_DIRECTORY");
      objOut.flush();

      // attempting to receive the object (file array of previews from server)
      ObjectInputStream objIn = new ObjectInputStream(sck.getInputStream());
      ArrayList<String> songNames = (ArrayList<String>) objIn.readObject();

      // reading object contents and storing in String arraylist

      for (File preview : previews) {
        if (preview.isFile()) {
          filenames.add(preview.getName());
        }
      }


      // cleaning up and returning arraylist of filenames
      sck.close();
      return previews;

  } catch ( Exception exn ) {
    System.out.println(exn); return null;
    }
  }
  */
}
