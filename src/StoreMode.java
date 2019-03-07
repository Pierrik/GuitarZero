import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * StoreMode.
 * @author John Mercer
 * @author Kamila Hoffmann-derlacka
 * @version 1.1, March 2019
 */
public class StoreMode extends JPanel {

  CarouselView view;
  StoreModeModel model;
  StoreModeController controller;

  // server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;

  // store settings
  private static final String BUNDLES = System.getProperty("user.dir") + "/local_store/bundle_files/";
  private static final String PREVIEWS = System.getProperty("user.dir") + "/local_store/preview_files/";

  public StoreMode() {
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
    StoreModeController controller = new StoreModeController(model);
    CarouselView        view       = new CarouselView(menuOptions);
    Thread controllerThread = new Thread(controller);
    controllerThread.start();
    this.add(view);
    //view.setVisible(true);

  }
}
