import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Handler.
 *
 * @author  John Mercer
 * @version 1.17, March 2019.
 */
public class Handler implements Runnable {

  private Socket sck;
  final int BUFFER_SIZE = 4092;

  Handler(Socket sck) {
    this.sck = sck;
  }

  /**
   * Downloads/uploads files to/from clients.
   */
  public void run() {
    try {
      // Client socket streams
      final DataInputStream dataIn = new DataInputStream(sck.getInputStream());
      final DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      // Parsing UTF - getting method
      String header = dataIn.readUTF();
      String[] headers = header.split("/");
      String method = headers[0].toUpperCase();
      char methodType = method.charAt(0);

      // Getting file name (if uploading or downloading)
      String fileName = "";
      if (methodType == 'U' || methodType == 'D') {
        fileName = headers[1];
      }

      // Processing request
      if (methodType == 'U') {
        long fileSize = dataIn.readLong();
        processUpload(method, dataIn, fileName, fileSize);
      } else if (methodType == 'D') {
        processDownload(method, dataOut, fileName);
        dataIn.close();
      } else if (method.equals("LIST_DIRECTORY")) {
        processListing(dataOut);
      }

      sck.close();

    } catch (Exception exn) {
      System.out.println(exn);
      System.exit(1);
    }
  }

  /**
   * Processes an upload request (sending files from client to server).
   *
   * @param method: Method type. Either 'UPLOAD_BUNDLE' or 'UPLOAD_PREVIEW'.
   * @param dataIn: DataInputStream to read from.
   * @param fileName: Name of the file to upload.
   * @param fileSize: Size of file to upload (bytes).
   */
  public void processUpload(String method, DataInputStream dataIn, String fileName, long fileSize) {
    // Building file path string
    String filePath = "";
    try {
      filePath = getFilePath(method, fileName);
    } catch (InvalidMethodException exn) {
      System.out.println(exn);
      System.exit(1);
    }

    // Processing the upload - synchronized to prevent 'connection abort: recv failed'
    synchronized (this) {
      try {
        // Stream for file to be written to (local directory on server)
        BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(filePath));

        // Reading file in from dataIn and writing it to fileOut stream
        int n;
        byte[] buf = new byte[BUFFER_SIZE];
        while (fileSize > 0
            && (n = dataIn.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {
          fileOut.write(buf, 0, n);
          fileSize -= 1;
        }

        // Cleaning up
        fileOut.close();

      } catch (Exception exn) {
        System.out.println(exn);
        System.exit(1);
      }
    }
  }

  /**
   * Processes a download request (retrieving files from server to client).
   *
   * @param method: Download method. Either 'DOWNLOAD_BUNDLE' or 'DOWNLOAD_PREVIEW'.
   * @param fileName: Name of file to download.
   */
  public void processDownload(String method, DataOutputStream dataOut, String fileName) {
    // Building file path string
    String filePath = "";
    try {
      filePath = getFilePath(method, fileName);
    } catch (InvalidMethodException exn) {
      System.out.println(exn);
      System.exit(1);
    }

    // Processing the download - synchronized to prevent 'connection abort: recv failed'
    synchronized (this) {
      try {
        // Reading file into byte array
        Path fileLocation = Paths.get(filePath);
        byte[] data = Files.readAllBytes(fileLocation);

        // Writing file from byte array to dataOut stream
        dataOut.writeLong(data.length);
        dataOut.write(data, 0, data.length);
        dataOut.flush();

      } catch (Exception exn) {
        System.out.println(exn);
        System.exit(1);
      }
    }
  }

  /**
   * Builds (local) file path string from request method and file name.
   *
   * @param method: Request method (UPLOAD/DOWNLOAD + _BUNDLE/_PREVIEW)
   * @param fileName: File name for request (name of file to be uploaded/downloaded to/from
   * server).
   * @throws InvalidMethodException: Thrown when provided method is invalid.
   * @return: File path to requested file.
   */
  public static String getFilePath(String method, String fileName) throws InvalidMethodException {
    // Building file path string
    String cd = System.getProperty("user.dir");
    if (method.equals("DOWNLOAD_BUNDLE") || method.equals("UPLOAD_BUNDLE")) {
      return cd + "/bundle_files/" + fileName;
    } else if (method.equals("DOWNLOAD_PREVIEW") || method.equals("UPLOAD_PREVIEW")) {
      return cd + "/preview_files/" + fileName;
    } else {
      throw new InvalidMethodException("Invalid method provided.");
    }
  }

  /**
   * Processes a listing request to the server.
   *
   * @param dataOut: DataOutputStream to write directory listings to.
   */
  public void processListing(DataOutputStream dataOut) {
    File previewDir = new File(System.getProperty("user.dir") + "/preview_files/");
    String[] previews = previewDir.list();

    synchronized (this) {
      try {
        // Writing song names to textOut
        for (String preview : previews) {
          dataOut.writeUTF(preview);
        }
        dataOut.writeUTF("END");
        dataOut.flush();

        // Cleaning up
        sck.close();
      } catch (Exception exn) {
        System.out.println(exn);
        System.exit(1);
      }
    }

  }
}