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
 * @version 1.13, February 2019.
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
      // client socket streams
      final DataInputStream dataIn = new DataInputStream(sck.getInputStream());
      final DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      // parsing UTF - getting method
      String header = dataIn.readUTF();
      String[] headers = header.split("/");
      String method = headers[0].toUpperCase();
      char methodType = method.charAt(0);

      // getting file name (if uploading or downloading)
      String fileName = "";
      if (methodType == 'U' || methodType == 'D') {
        fileName = headers[1];
      }

      // processing request
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
    // building file path string
    String filePath = "";
    try {
      filePath = getFilePath(method, fileName);
    } catch (InvalidMethodException exn) {
      System.out.println(exn);
      System.exit(1);
    }

    // processing the upload - synchronized to prevent 'connection abort: recv failed'
    synchronized (this) {
      try {
        // stream for file to be written to (local directory on server)
        BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(filePath));

        // reading file in from dataIn and writing it to fileOut stream
        int n;
        byte[] buf = new byte[BUFFER_SIZE];
        while (fileSize > 0
            && (n = dataIn.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {
          fileOut.write(buf, 0, n);
          fileSize -= 1;
        }

        // cleaning up
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
    // building file path string
    String filePath = "";
    try {
      filePath = getFilePath(method, fileName);
    } catch (InvalidMethodException exn) {
      System.out.println(exn);
      System.exit(1);
    }

    // processing the download - synchronized to prevent 'connection abort: recv failed'
    synchronized (this) {
      try {
        // reading file into byte array
        Path fileLocation = Paths.get(filePath);
        byte[] data = Files.readAllBytes(fileLocation);

        // writing file from byte array to dataOut stream
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
    // building file path string
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
   */
  public void processListing(DataOutputStream dataOut) {
    File previewDir = new File(System.getProperty("user.dir") + "\\preview_files\\");
    String[] previews = previewDir.list();

    synchronized (this) {
      try {
        // writing song names to textOut
        for (String preview : previews) {
          dataOut.writeUTF(preview);
        }
        dataOut.writeUTF("END");
        dataOut.flush();

        // cleaning up
        sck.close();
      } catch (Exception exn) {
        System.out.println(exn);
        System.exit(1);
      }
    }

  }
}