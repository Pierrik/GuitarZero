import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * MockClient.
 *
 * @author  John Mercer
 * @author  Harper Ford (Javadoc)
 * @version 2.01, February 2019.
 */
public class MockClient {
  private String host;
  private int    port;

  MockClient(String host, int port){
    this.host = host;
    this.port = port;
  }

  /**
   * Uploads the given bundle to the server
   * @param fileName: The filepath of the file to upload
   * @param method: Upload method (UPLOAD_BUNDLE or UPLOAD_PREVIEW)
   */
  public void uploadFile(String fileName, String method){
    try {
      Socket sck = new Socket(this.host, this.port);

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
   * Downloads the given file from the server, and unzips it in the corresponding directory
   * @param fileName: The filepath of the file to download
   * @param method: Download method (DOWNLOAD_BUNDLE or DOWNLOAD_PREVIEW)
   */
  public void downloadFile(String fileName, String method){
    try {
      // checking if local_store directory exists, and creates it if it doesn't yet
      String cd = System.getProperty("user.dir");
      String bundleDir = cd + "/local_store/bundle_files/";
      String previewDir = cd + "/local_store/preview_files/";

      if (Files.notExists(Paths.get(bundleDir))) {
        Files.createDirectories(Paths.get(bundleDir));
      }
      if (Files.notExists(Paths.get(previewDir))) {
        Files.createDirectories(Paths.get(previewDir));
      }

      // requesting to download the file
      Socket sck = new Socket(this.host, this.port);
      DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      dataOut.writeUTF(method + "/" + fileName);
      dataOut.flush();

      // attempting to receive the file
      DataInputStream  dataIn  = new DataInputStream(sck.getInputStream());
      BufferedOutputStream fileOut;

      if (method.equals("DOWNLOAD_BUNDLE")) {
        File file = new File(bundleDir + fileName);
        fileOut = new BufferedOutputStream(new FileOutputStream(file));
      }
      else if (method.equals("DOWNLOAD_PREVIEW")) {
        File file = new File(previewDir + fileName);
        fileOut = new BufferedOutputStream(new FileOutputStream(file));
      }
      else {
        sck.close();
        return;
      }

      byte[] bytes = dataIn.readAllBytes();
      fileOut.write(bytes);

      //unzip(previewDir + fileName, previewDir);

      // cleaning up
      fileOut.close();
      sck.close();

    } catch ( Exception exn ) {
      System.out.println( exn ); System.exit( 1 );
    }

  }

  /**
   * Unzips the contents of a specified .zip file to a specified destination directory
   * @param zipFilePath: File path to zip file.
   * @param destDir: Destination directory.
   */
  private static void unzip(String zipFilePath, String destDir) {
    // create output directory if it doesn't exist
    File dir = new File(destDir);
    if(!dir.exists()) {
      dir.mkdirs();
    }

    //buffer for read and write data to file
    FileInputStream fileIn;
    byte[] bytes = new byte[1024];

    try {
      fileIn = new FileInputStream(zipFilePath);
      ZipInputStream zipIn = new ZipInputStream(fileIn);

      // unzipping and writing each entry to disk
      ZipEntry entry = zipIn.getNextEntry();
      while(entry != null){
        String fileName = entry.getName();
        File newFile = new File(destDir + File.separator + fileName);

        // create directories for sub directories in zip
        new File(newFile.getParent()).mkdirs();
        FileOutputStream fileOut = new FileOutputStream(newFile);

        int length;
        while ((length = zipIn.read(bytes)) > 0) {
          fileOut.write(bytes, 0, length);
        }
        fileOut.close();

        // close the entry and reassigning to the next one
        zipIn.closeEntry();
        entry = zipIn.getNextEntry();
      }
      zipIn.closeEntry();

      // cleaning up
      zipIn.close();
      fileIn.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Requests a directory listing of the previews directory on the server
   * @return ArrayList: ArrayList of filenames (songs available to buy)
   */
  public ArrayList<String> listDirectory(){
    try{
      // requesting the directory listing
      Socket sck = new Socket(this.host, this.port);
      DataOutputStream out = new DataOutputStream(sck.getOutputStream());
      out.writeUTF("LIST_DIRECTORY");
      out.flush();

      // attempting to receive the filenames and build arraylist
      DataInputStream  dataIn  = new DataInputStream(sck.getInputStream());
      ArrayList<String> songNames = new ArrayList<>();

      String songName;
      while (!(songName = dataIn.readUTF()).equals("END")){
        songNames.add(songName);
      }

      // cleaning up and returning arraylist of filenames
      sck.close();
      return songNames;

  } catch ( Exception exn ) {
    System.out.println(exn); return null;
    }
  }

}
