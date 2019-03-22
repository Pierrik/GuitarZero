import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * StoreManagerModel.
 *
 * @author Pierrik Mellab
 * @author John Mercer
 * @author Harper Ford
 * @version 3.0, March 2019
 *
 */
public class StoreManagerModel {

  // Server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;
  private static final int    SIZE = 1024;

  // Storing bundle files
  private File titleFile = null;
  private File coverArtFile = null;
  private File musicFile = null;

  public StoreManagerModel () {
  }

  /**
   * Lets a user browse for a file on their computer
   * @return The selected file or null if file not approved
   */
  public static File fileFinder (String filetype) {
    JFileChooser jfc = null;
    try {
      jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    }
    catch(Exception e){
      System.out.println("Couldn't start JFileChooser. /STOREMANAGERMODEL");
    }
    FileNameExtensionFilter filter = null;
    try {
      filter = new FileNameExtensionFilter(filetype, filetype, filetype);
      jfc.setFileFilter(filter);
    }
    catch(Exception e){
      System.out.println("Unable to filter files. /STOREMANAGERMODEL");
    }

    try {
      int returnValue = jfc.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = jfc.getSelectedFile();
        if(filetype == "png"){
          return resizeImageFile(selectedFile, 140, 140);
        }
        return selectedFile;
      } else {
        return null;
      }
    }
    catch(Exception e){
      System.out.println("Can't return file. /STOREMANAGERMODEL");
      return null;
    }
  }

  /**
   * Reads an image file and resizes it to desired dimensions
   * @param file
   * @param width
   * @param height
   * @return Resized Image as Image
   */
  public static File resizeImageFile(File file, int width, int height){
    Image image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    BufferedImage resizedImage = (BufferedImage) image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    OutputStream out = null;
    try {
      out = new FileOutputStream(file);
      ImageIO.write(resizedImage, "png", out);
      File foo = new File(file.getPath());
      return foo;
    } catch (Exception e) {
      System.out.println("Failed image resize. /STOREMANAGERMODEL");
    }
    return null;
  }

  /**
   * Reads a text File
   * @param textFile:    The file to read
   * @return             The text from the given file
   * @throws IOException If the file is not found or corrupt
   */
  public static String textFileReader(File textFile) throws IOException {

    FileReader in = new FileReader(textFile);
    BufferedReader br = new BufferedReader(in);

    String text = br.readLine();
    in.close();

    return text;
  }

  public void setMusicFile (File file) { musicFile = file; }

  public void setCoverArtFile (File file) { coverArtFile = file; }

  public void setTitleFile (File file) { titleFile = file; }

  /**
   * Creates a 'bundle' (zip file) from three specified files.
   * @param titleFile:    .TXT file containing a song title
   * @param coverArtFile: .PNG file containing a songs cover art
   * @param musicFile:    .MIDI file containing the songs
   * @return              The contents of titleFile
   * @throws IOException  In case any of the files are corrupt or can't be found
   */
  public static String bundleZipper (File titleFile, File coverArtFile, File musicFile) throws IOException {
    File noteFile = null;
    FileOutputStream fos = null;
    ZipOutputStream zipOut = null;
    try {
      MidiToNotes.writeFile(musicFile.getPath());         // creates noteFile.txt in current directory
      noteFile = new File("noteFile.txt");
    }
    catch (Exception e){
      System.out.println("Unable to create note file. /STOREMANAGERMODEL");
    }

    List<File> srcFiles = Arrays.asList(coverArtFile, musicFile, noteFile);          // files to be zipped go here

    String songName = textFileReader(titleFile);
    songName += "(bundle).zip";

    try {
      fos = new FileOutputStream(songName);
      zipOut = new ZipOutputStream(fos);
    }
    catch(Exception e){
      System.out.println("Unable to create output streams. /STOREMANAGERMODEL");
    }

    //Zip files then send through outputstreams
    try {
      for (File srcFile : srcFiles) {
        FileInputStream fis = new FileInputStream(srcFile);
        ZipEntry zipEntry = new ZipEntry(srcFile.getName());
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[SIZE];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
          zipOut.write(bytes, 0, length);
        }
        fis.close();
      }
      zipOut.close();
      fos.close();
    }
    catch(Exception e){
      System.out.println("Unable to zip and send files. /STOREMANAGERMODEL");
      e.printStackTrace();
    }

    return songName;
  }

  /**
   * Creates a preview (title and cover image) zip file from two specified files.
   * @param titleFile:    .TXT file containing a song title
   * @param coverArtFile: .PNG file containing a songs cover art
   * @return              The contents of titleFile
   * @throws IOException  In case any of the files are corrupt or can't be found
   */
  public static String previewZipper (File titleFile, File coverArtFile) throws IOException {
    FileOutputStream fos = null;
    ZipOutputStream zipOut = null;
    FileInputStream fis = null;
    String songName = textFileReader(titleFile);
    songName += "(preview).zip";

    try {
      fos = new FileOutputStream(songName);
      zipOut = new ZipOutputStream(fos);
      fis = new FileInputStream(coverArtFile);
    }
    catch(Exception e){
      System.out.println("Unable to open output and input streams (Preview). /STOREMANAGERMODEL");
    }

    try {
      ZipEntry zipEntry = new ZipEntry(coverArtFile.getName());
      zipOut.putNextEntry(zipEntry);

      byte[] bytes = new byte[SIZE];
      int length;
      while ((length = fis.read(bytes)) >= 0) {
        zipOut.write(bytes, 0, length);
      }

      fis.close();
      zipOut.close();
      fos.close();
    }
    catch(Exception e){
      System.out.println("Unable to zip and send (Preview). /STOREMANAGERMODEL");
    }

    return songName;
  }

  /**
   * Deletes specified file
   * @param filePath: File to delete
   */
  public static void deleteFile(String filePath){
    try {
      File file = new File(filePath);
      file.delete();
    }
    catch(Exception e){
      System.out.println("Unable to delete file. /STOREMANAGERMODEL");
    }
  }

  /**
   * Saves files into zipped folders then uploads them to server
   */
  public void saveAction () {
      try {
        if (titleFile != null && coverArtFile !=null && musicFile != null){
          String bundlePath = null;
          String previewPath = null;
          try {
            // zipping files and sending to server
            bundlePath = bundleZipper(titleFile, coverArtFile, musicFile);
            previewPath = previewZipper(titleFile, coverArtFile);
          }
          catch(Exception e){
            System.out.println("Unable to zip files (saveAction). /STOREMANAGERMODEL");
          }

          try {
            MockClient client = new MockClient(HOST, PORT);
            client.uploadFile(bundlePath, "UPLOAD_BUNDLE");
            client.uploadFile(previewPath, "UPLOAD_PREVIEW");
          }
          catch(Exception e){
            System.out.println("Unable to upload files (saveAction). /STOREMANAGERMODEL");
          }

          // cleaning up (deleting zips and noteFile on client-side)
          deleteFile(bundlePath);
          deleteFile(previewPath);
          deleteFile("noteFile.txt");
        }
      } catch (Exception e) {
        System.out.println("Unable to save files. /STOREMANAGERMODEL");
      }
  }

}
