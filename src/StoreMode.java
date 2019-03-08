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

  // mvc attributes
  CarouselView view;
  StoreModeModel model;
  StoreModeController controller;

  // server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;

  // store settings
  private static final String BUNDLES = "../local_store/bundle_files/";
  private static final String PREVIEWS = "../local_store/preview_files/";

  public StoreMode() {
    MockClient client = new MockClient(HOST, PORT);

    // Getting preview filenames of all available songs on the server
    ArrayList<String> fileNames = client.listDirectory();

    // Downloading and unzipping all previews, and giving each title/cover a JLabel
    ArrayList<JLabel> menuOptions = new ArrayList<>();
    for (String fileName : fileNames){
      // Downloading and unzipping preview, then deleting zip
      client.downloadFile(fileName, "DOWNLOAD_PREVIEW");
      String songName = MockClient.getSongPreview(fileName);
      String previewDir = PREVIEWS + songName + "/";
      MockClient.unzip(PREVIEWS + fileName, previewDir);

      // Reading in the cover image and song name, assigning to a JLabel and adding to ArrayList
      File[] previewContents = new File(previewDir).listFiles();

      for (File cover : previewContents){
        JLabel label = new JLabel(new ImageIcon(previewDir + cover.getName()));
        label.setText(songName);
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