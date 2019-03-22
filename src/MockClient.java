import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * MockClient.
 *
 * @author  John Mercer
 * @version 2.5, March 2019.
 */
public class MockClient {
  private String host;
  private int    port;

  private final static int BUFFER_SIZE = 4092;
  private final static int BUNDLE_LEN_DIFF = 12;
  private final static int PREVIEW_LEN_DIFF = 13;

  MockClient(String host, int port){
    this.host = host;
    this.port = port;
  }

  /**
   * Uploads the given bundle to the server.
   *
   * @param fileName: The filepath of the file to upload.
   * @param method: Upload method (UPLOAD_BUNDLE or UPLOAD_PREVIEW).
   */
  public void uploadFile(String fileName, String method){
    try {
      Socket sck = new Socket(this.host, this.port);

      // Instantiating byte array of correct size to store file
      File file = new File(fileName);
      byte[] bytes = new byte[(int) file.length()];

      // Streams for reading/writing the file
      DataInputStream dataIn = new DataInputStream(new FileInputStream(file));
      DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      // Reading the file into the byte array
      dataIn.readFully(bytes, 0, bytes.length);

      // Writing the file to the socket output stream (from the byte array)
      dataOut.writeUTF(method + "/" + fileName);
      dataOut.writeLong(file.length());
      dataOut.write(bytes, 0, bytes.length);
      dataOut.flush();

      // Cleaning up
      sck.close();

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }
  }

  /**
   * Downloads the given file from the server.
   *
   * @param fileName: The filepath of the file to download.
   * @param method: Download method (DOWNLOAD_BUNDLE or DOWNLOAD_PREVIEW).
   */
  public void downloadFile(String fileName, String method){
    try {
      // Checking if local_store directory exists, and creates it if it doesn't yet
      String bundleDir = "../local_store/bundle_files/";
      String previewDir = "../local_store/preview_files/";

      if (Files.notExists(Paths.get(bundleDir))) {
        Files.createDirectories(Paths.get(bundleDir));
      }
      if (Files.notExists(Paths.get(previewDir))) {
        Files.createDirectories(Paths.get(previewDir));
      }

      // Requesting to download the file
      Socket sck = new Socket(this.host, this.port);
      DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());
      dataOut.writeUTF(method + "/" + fileName);
      dataOut.flush();

      // Attempting to receive the file
      DataInputStream dataIn = new DataInputStream(sck.getInputStream());
      BufferedOutputStream fileOut;
      String zipPath;

      if (method.equals("DOWNLOAD_BUNDLE")) {
        zipPath = bundleDir + fileName;
      }
      else if (method.equals("DOWNLOAD_PREVIEW")) {
        zipPath = previewDir + fileName;
      }
      else {
        sck.close();
        return;
      }

      File file = new File(zipPath);
      fileOut = new BufferedOutputStream(new FileOutputStream(file));

      int fileSize = (int)dataIn.readLong();
      int n;
      byte[] buf = new byte[BUFFER_SIZE];
      while (fileSize > 0
          && (n = dataIn.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {
        fileOut.write(buf, 0, n);
        fileSize -= 1;
      }

      // Cleaning up
      fileOut.close();
      sck.close();

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }

  }

  /**
   * Deletes specified file.
   *
   * @param filePath: File path of file to delete.
   */
  public static void deleteFile(String filePath){
    try {
      Path path = Paths.get(filePath);
      Files.delete(path);
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  /**
   * Removes the parentheses and file extension from a bundle file name.
   * e.g. "Song(bundle).zip" to "Song".
   *
   * @param bundle: Bundle file name.
   * @return: Song name.
   */
  public static String getSongBundle(String bundle){
    return bundle.substring(0, bundle.length() - BUNDLE_LEN_DIFF);
  }

  /**
   * Removes the parentheses and file extension from a preview file name.
   * e.g. "Song(preview).zip" to "Song".
   *
   * @param preview: Preview file name.
   * @return: Song name.
   */
  public static String getSongPreview(String preview){
    return preview.substring(0, preview.length() - PREVIEW_LEN_DIFF);
  }

  /**
   * Unzips the contents of a specified .zip file to a specified destination directory, and
   * deletes the original .zip file afterwards.
   *
   * @param zipFilePath: File path to zip file.
   * @param destDir: Destination directory.
   */
  public static void unzip(String zipFilePath, String destDir) {
    // Create output directory if it doesn't exist
    File dir = new File(destDir);
    if(!dir.exists()) {
      dir.mkdirs();
    }

    try {
      // Unzipping and writing each entry to disk
      ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
      ZipEntry entry = zipIn.getNextEntry();

      while (entry != null) {
        String filePath = destDir + "/" + entry.getName();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] buf = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(buf)) != -1) {
          bos.write(buf, 0, read);
        }
        bos.close();
        zipIn.closeEntry();
        entry = zipIn.getNextEntry();
      }
      zipIn.closeEntry();
      zipIn.close();

      // Deleting the remaining zip file
      deleteFile(zipFilePath);

    }
    catch (EOFException e){
      // JVM bug (JVM-6519463) - So catch every time and do nothing with it (extraction still works)
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Requests a directory listing of the previews directory on the server.
   *
   * @return ArrayList: ArrayList of filenames in previews directory (songs available to buy).
   */
  public ArrayList<String> listDirectory(){
    try{
      // Requesting the directory listing
      Socket sck = new Socket(this.host, this.port);
      DataOutputStream out = new DataOutputStream(sck.getOutputStream());
      out.writeUTF("LIST_DIRECTORY");
      out.flush();

      // Attempting to receive the filenames and build arraylist
      DataInputStream  dataIn  = new DataInputStream(sck.getInputStream());
      ArrayList<String> songNames = new ArrayList<>();

      String songName;
      while (!(songName = dataIn.readUTF()).equals("END")){
        songNames.add(songName);
      }

      // Cleaning up and returning arraylist of filenames
      sck.close();
      return songNames;

    } catch ( Exception exn ) {
      System.out.println(exn); return null;
    }
  }

}