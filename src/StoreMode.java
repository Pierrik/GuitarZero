import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * StoreMode.
 * @author John Mercer
 * @version 1.0, March 2019
 */
public class StoreMode {
  // server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;

  // store settings
  private static final String BUNDLES = System.getProperty("user.dir") + "/local_store/bundle_files/";
  private static final String PREVIEWS = System.getProperty("user.dir") + "/local_store/preview_files/";

  public static void main(String args[]) {
    MockClient client = new MockClient(HOST, PORT);

    // Getting all available songs on the server
    ArrayList<String> songNames = client.listDirectory();

    // Downloading and unzipping all previews, and giving each title/cover a JLabel
    ArrayList<JLabel> menuOptions = new ArrayList<>();

    for (String song:songNames){
      // Downloading preview and unzipping
      client.downloadFile(song, "DOWNLOAD_PREVIEW");
      // Reading in the cover image and song name, assigning to a JLabel and adding to ArrayList
      File[] cover = new File(PREVIEWS + song).listFiles();
      if (cover != null){
        JLabel label = new JLabel(new ImageIcon(cover[0].getAbsolutePath()));
        label.setText(song);
        menuOptions.add(label);
      }
    }

    // Initialise the model, controller, view GUI classes
    StoreModeModel      model      = new StoreModeModel();
    StoreModeController controller = new StoreModeController( model );
    StoreModeView       view       = new StoreModeView(model, menuOptions);
    view.setVisible(true);
    controller.pollGuitarForever();

  }
}
