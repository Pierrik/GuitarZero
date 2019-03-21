import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * StoreMode.
 * @author John Mercer
 * @version 2.13, March 2019
 */
public class StoreMode extends JPanel {

  // Server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;
  final static int MODE_DELAY = 150;

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

    model.popUp("../assets/LoadingStorePopUp.jpg", 1000);

    Thread controllerThread = new Thread(controller);
    controllerThread.start();
    this.add(view);
    //view.setVisible(true);

  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); // paint the background image and scale it to fill the entire space

    Image highway = null;
    try {
      highway = ImageIO.read(new File("../assets/Done/Highway.bmp"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    g.drawImage(highway, 0, 0, this);
  }

  public void sleep(int millis){
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}