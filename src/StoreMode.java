import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * StoreMode.
 * @author John Mercer
 * @version 2.13, March 2019
 */
public class StoreMode extends JPanel {

  // MVC attributes
  CarouselView view;
  StoreModeModel model;
  StoreModeController controller;

  // Server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;

  // Store settings
  private static final String PREVIEWS = "../local_store/preview_files/";

  public StoreMode() {
    MockClient client = new MockClient(HOST, PORT);

    // Getting preview filenames of all available songs on the server
    ArrayList<String> fileNames = client.listDirectory();

    // Downloading and unzipping all previews, and giving each title/cover a JLabel
    ArrayList<JLabel> menuOptions = new ArrayList<>();
    for (String fileName : fileNames){
      // Downloading and unzipping preview (and deleting zip after)
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
    CarouselView       view        = new CarouselView(menuOptions);
    CarouselModel      model       = new CarouselModel(view);
    CarouselController controller  = new CarouselController(model, Mode.STORE);
    try {
      Thread.sleep(200);
    } catch (InterruptedException e){
      e.printStackTrace();
    }
    Thread controllerThread = new Thread(controller);
    controllerThread.start();
    this.add(view);
    //view.setVisible(true);

  }
}