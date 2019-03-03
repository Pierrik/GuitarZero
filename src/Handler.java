import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handler.
 *
 * @author  John Mercer
 * @version 1.05, February 2019.
 */
public class Handler implements Runnable {
  private Socket sck;

  Handler( Socket sck ) {
    this.sck = sck;
  }
  /**
   * Downloads/uploads files to/from clients.
   */
  public void run() {
    try {
      // client socket streams
      final DataInputStream dataIn = new DataInputStream(sck.getInputStream());
      final DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      // parsing UTF - for method and file name
      String header = dataIn.readUTF();
      String[] headers = header.split("/");
      String method = headers[0].toUpperCase();
      char methodType = method.charAt(0);
      String fileName = headers[1];

      // getting file size
      long fileSize = dataIn.readLong();

      //processing request
      if (methodType == 'U'){
        processUpload(method, dataIn, fileName, fileSize);
      }
      else if (methodType == 'D'){
        processDownload(method, dataOut, fileName);
      }
    }
    catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }

  /**
   * Processes an upload request (sending files from client to server).
   * @param method: Method type. Either 'UPLOAD_BUNDLE' or 'UPLOAD_PREVIEW'.
   * @param dataIn: DataInputStream to read from.
   * @param fileName: Name of the file to upload.
   * @param fileSize: Size of file to upload (bytes).
   */
  public void processUpload(String method, DataInputStream dataIn, String fileName, long fileSize){
    // building file path string
    String filePath = "";
    String cd = System.getProperty("user.dir");

    if (method.equals("UPLOAD_BUNDLE")){
      filePath = cd + "\\bundle_files\\" + fileName;
    }
    else if (method.equals("UPLOAD_PREVIEW")){
      filePath = cd + "\\preview_files\\" + fileName;
    }

    try{
      // stream for file to be written to (local directory on server)
      BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(filePath));

      // reading file in from dataIn and writing it to fileOut stream
      int n;
      byte[] buf = new byte[4092];
      while (fileSize > 0 && (n = dataIn.read(buf, 0, (int)Math.min(buf.length, fileSize))) != -1){
        fileOut.write(buf, 0, n);
        fileSize -= 1;
      }

      // cleaning up
      fileOut.close();
      sck.close();
    }
    catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }

  /**
   * Processes a download request (retrieving files from server to client).
   * @param method: Download method. Either 'DOWNLOAD_BUNDLE' or 'DOWNLOAD_PREVIEW'.
   * @param dataOut: DataOutputStream to write to.
   * @param fileName: Name of file to download.
   */
  public void processDownload(String method, DataOutputStream dataOut, String fileName){
    // building file path string
    String filePath = "";
    String cd = System.getProperty("user.dir");

    if (method.equals("DOWNLOAD_BUNDLE")){
      filePath = cd + "\\bundle_files\\" + fileName;
    }
    else if (method.equals("DOWNLOAD_PREVIEW")){
      filePath = cd + "\\preview_files\\" + fileName;
    }

    try{
      // instantiating byte array of the correct size to store file
      File file = new File(filePath);
      byte[] bytes = new byte[(int) file.length()];

      // reading file into byte array
      DataInputStream fileIn = new DataInputStream(new FileInputStream(file));
      fileIn.readFully(bytes, 0, bytes.length);

      // writing file from byte array to dataOut stream
      dataOut.write(bytes, 0, bytes.length);
      dataOut.flush();

      // cleaning up
      fileIn.close();
      sck.close();
    }
    catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }
}
