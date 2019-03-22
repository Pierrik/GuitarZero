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
 * @author Harper Ford (exception handling)
 * @version 2.13, March 2019
 */
public class StoreMode extends JPanel {

  // Server settings
  private static final String HOST       = "localhost";
  private static final int    PORT       = 8888;
  private final static int    MODE_DELAY = 150;
  private static final int    POINT_0_0  = 0;

  // Store settings
  private static final String PREVIEWS = "../local_store/preview_files/";

  public StoreMode() {
    String songName = null;
    String previewDir = null;
    MockClient client = null;
    try {
      client = new MockClient(HOST, PORT);
    }
    catch(Exception e){
      System.out.println("Enable to create MockClient. /STOREMODE");
      GameUtils.changeModeOnNewThread(Mode.SLASH);
    }

    // Getting preview filenames of all available songs on the server
    ArrayList<String> fileNames = client.listDirectory();

    // Downloading and unzipping all previews, and giving each title/cover a JLabel
    ArrayList<JLabel> menuOptions = new ArrayList<>();
    for (String fileName : fileNames){
      try {
        // Downloading and unzipping preview (and deleting zip after)
        client.downloadFile(fileName, "DOWNLOAD_PREVIEW");
        songName = MockClient.getSongPreview(fileName);
        previewDir = PREVIEWS + songName + "/";
        MockClient.unzip(PREVIEWS + fileName, previewDir);
      }
      catch(Exception e){
        System.out.println("Couldn't download files. /STOREMODE");
        GameUtils.changeModeOnNewThread(Mode.SLASH);
      }
      // Reading in the cover image and song name, assigning to a JLabel and adding to ArrayList
      File[] previewContents = new File(previewDir).listFiles();
      try {
        for (File cover : previewContents) {
          JLabel label = new JLabel(new ImageIcon(previewDir + cover.getName()));
          label.setText(songName);
          menuOptions.add(label);
        }
      }
      catch(Exception e){
        System.out.println("Can't create JLabels in Carousel. /STOREMODE");
        GameUtils.changeModeOnNewThread(Mode.SLASH);
      }
    }
    CarouselController controller = null;
    CarouselView view = null;
    try {
      // Initialise the model, controller, view GUI classes
      view = new CarouselView(menuOptions, Mode.STORE);
      view.displayCurrency();
      CarouselModel model = new CarouselModel(view);
      controller = new CarouselController(model, Mode.STORE);
    }
    catch(Exception e){
      System.out.println("Unable to start MVC. /STOREMODE");
      GameUtils.changeModeOnNewThread(Mode.SLASH);
    }
    try {
      Thread controllerThread = new Thread(controller);
      controllerThread.start();
    }
    catch(Exception e){
      System.out.println("Enable to start Thread for controller. /STOREMODE");
      GameUtils.changeModeOnNewThread(Mode.SLASH);
    }
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
      System.out.println("Unable to load highway");
      GameUtils.changeModeOnNewThread(Mode.SLASH);
    }
    g.drawImage(highway, -7, 5, this);
  }

  public void sleep(int millis){
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      System.out.println("Woken from sleep");
      GameUtils.changeModeOnNewThread(Mode.SLASH);
    }
  }

}