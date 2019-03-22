import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;

/**
 * CarouselModel
 *
 * @author  Pierrik Mellab
 * @author Harper Ford
 * @author Kamila Hoffmann-Derlacka
 * @author John Mercer
 * @version 2.00, March 2019.
 */

public class CarouselModel {
  CarouselView view;
  File currencyFile;
  private static int  totalCurrency;
  final static String BUNDLES       = "../local_store/bundle_files/";
  final static String HOST          = "localhost";
  final static int    PORT          = 8888;
  final static int    POP_UP_TIME   = 3000;
  final static int    POP_UP_WIDTH  = 260;
  final static int    POP_UP_HEIGHT = 160;
  final static int    POP_UP_Y      = 50;
  final static int    POP_UP_X      = 275;
  JLabel popUp;

  /**
   * Skeleton constructor for later use
   */
  public CarouselModel(CarouselView view) {
    this.view = view;

    // If the currency file can't be loaded, do not start the game
    try {
      this.currencyFile = Currency.findCurrencyFile();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // If currency file cannot be read properly, do not start the game
    try {
      this.totalCurrency = Currency.loadCurrencyFile(this.currencyFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Calls the rightMovement method in the carouselView file causing icons to shift right
   */
  public void right() {
    view.rightMovement();
  }

  /**
   * Calls the leftMovement method in the carouselView file causing icons to shift left
   */
  public void left() {
    view.leftMovement();
  }


  /**
   * Returns a string from the chosenOption method in the carouselView file allowing a user to
   * choose a menu option
   */
  public String select() {

    //System.out.println(view.chosenOption());
    return view.chosenOption();
  }

  /**
   * Downloads a song to local directory.
   */
  public void buySong(String songName) {

    String bundleName = songName + "(bundle).zip";

    if (totalCurrency > 0 && !isInLocalDir(songName)) {
      try {
        updateCurrencyAndLocalStore(bundleName, songName);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (totalCurrency < 1 && isInLocalDir(songName)){
      popUp("../assets/NoMoneyAndSongOwnedPopUp.jpg", POP_UP_TIME);
    } else if (totalCurrency > 0 && isInLocalDir(songName)){
      popUp("../assets/SongOwnedPopUp.jpg", POP_UP_TIME);
    } else {
      popUp("../assets/NoMoneyPopUp.jpg", POP_UP_TIME);
    }
  }

  public void updateCurrencyAndLocalStore(String bundleName, String songName) throws Exception {
    totalCurrency --;
    Currency.saveCurrencyFile(totalCurrency);

    MockClient client = new MockClient(HOST, PORT);
    client.downloadFile(bundleName, "DOWNLOAD_BUNDLE");
    String bundlesDir = BUNDLES + songName + "/";
    MockClient.unzip(BUNDLES + bundleName, bundlesDir);
  }

  public void popUp(String pngPath, int time) {
    popUp = new JLabel(new ImageIcon(pngPath));
    popUp.setSize(POP_UP_WIDTH, POP_UP_HEIGHT);
    popUp.setBounds(POP_UP_X, POP_UP_Y, POP_UP_WIDTH, POP_UP_HEIGHT);
    this.view.add(popUp, 0);
    this.view.repaint();
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.view.remove(popUp);
  }

  /**
   * Checks if the song is already in the local directory.
   */
  public static boolean isInLocalDir(String song) {
    // checking if local_store directory exists
    String bundleDir = "../local_store/bundle_files/";

    if (Files.notExists(Paths.get(bundleDir))) {
      return false;
    } else {
      File tmpDir = new File("../local_store/bundle_files/" + song);
      if (tmpDir.exists())
        return true;
      else
        return false;
    }
  }

}
