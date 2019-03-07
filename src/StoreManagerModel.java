import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class StoreManagerModel {

  // server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;

  // storing bundle files
  private File titleFile = null;
  private File coverArtFile = null;
  private File musicFile = null;



  public StoreManagerModel () {

  }

  /**
   * Lets a user browse for a file on their computer
   * @return The selected file or null if file not approved
   */
  public static File fileFinder () {
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    int returnValue = jfc.showOpenDialog(null);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = jfc.getSelectedFile();
      return selectedFile;
    } else {
      return null;
    }
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

    MidiToNotes.writeFile(musicFile.getPath());         // creates noteFile.txt in current directory
    File noteFile = new File("noteFile.txt");

    List<File> srcFiles = Arrays.asList(coverArtFile, musicFile, noteFile);          // files to be zipped go here

    String songName = textFileReader(titleFile);
    songName += "(bundle).zip";

    FileOutputStream fos = new FileOutputStream(songName);   // title goes here
    ZipOutputStream zipOut = new ZipOutputStream(fos);

    for (File srcFile : srcFiles) {
      FileInputStream fis = new FileInputStream(srcFile);
      ZipEntry zipEntry = new ZipEntry(srcFile.getName());
      zipOut.putNextEntry(zipEntry);

      byte[] bytes = new byte[1024];
      int length;
      while((length = fis.read(bytes)) >= 0) {
        zipOut.write(bytes, 0, length);
      }
      fis.close();
    }
    zipOut.close();
    fos.close();

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

    String songName = textFileReader(titleFile);
    songName += "(preview).zip";

    FileOutputStream fos = new FileOutputStream(songName);
    ZipOutputStream zipOut = new ZipOutputStream(fos);
    FileInputStream fis = new FileInputStream(coverArtFile);

    ZipEntry zipEntry = new ZipEntry(coverArtFile.getName());
    zipOut.putNextEntry(zipEntry);

    byte[] bytes = new byte[1024];
    int length;
    while((length = fis.read(bytes)) >= 0) {
      zipOut.write(bytes, 0, length);
    }

    fis.close();
    zipOut.close();
    fos.close();

    return songName;
  }

  /**
   * Deletes specified file
   * @param filePath: File to delete
   */
  public static void deleteFile(String filePath){
    File file = new File(filePath);
    file.delete();
  }

  public void saveAction () {
      try {
        if (titleFile != null && coverArtFile !=null && musicFile != null){
          // zipping files and sending to server
          String bundlePath = bundleZipper(titleFile, coverArtFile, musicFile);
          String previewPath = previewZipper(titleFile, coverArtFile);

          MockClient client = new MockClient(HOST, PORT);
          client.uploadFile(bundlePath, "UPLOAD_BUNDLE");
          client.uploadFile(previewPath, "UPLOAD_PREVIEW");

          // cleaning up (deleting zips and noteFile on client-side)
          deleteFile(bundlePath);
          deleteFile(previewPath);
          deleteFile("noteFile.txt");
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
  }

}
