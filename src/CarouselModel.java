import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * CarouselModel
 *
 * @author  Pierrik Mellab
 * @modified Harper Ford
 * @version 1.00, March 2019.
 */

public class CarouselModel {
  CarouselView view;
  private static int totalCurrency;
  final static String BUNDLES = "../local_store/bundle_files/";
  final static String HOST  = "localhost";
  final static int    PORT  = 8888;
  JLabel popUp;

  /**
   * Skeleton constructor for later use
   */
  public CarouselModel(CarouselView view) {
    this.view = view;
    this.totalCurrency = 1; //Currency.loadTotalCurrency();
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
      updateCurrencyAndLocalStore(bundleName, songName);
    } else if (totalCurrency < 1 && isInLocalDir(songName)){
      popUp("../assets/NoMoneyAndSongOwnedPopUp.jpg", 3000);
    } else if (totalCurrency > 0 && isInLocalDir(songName)){
      popUp("../assets/SongOwnedPopUp.jpg", 3000);
    } else {
      popUp("../assets/NoMoneyPopUp.jpg", 3000);
    }
  }

  public void updateCurrencyAndLocalStore(String bundleName, String songName){
    totalCurrency --;

    MockClient client = new MockClient(HOST, PORT);
    client.downloadFile(bundleName, "DOWNLOAD_BUNDLE");
    String bundlesDir = BUNDLES + songName + "/";
    MockClient.unzip(BUNDLES + bundleName, bundlesDir);
  }

  public void popUp(String pngPath, int time) {

    popUp = new JLabel(new ImageIcon(pngPath));
    popUp.setSize(260, 160);
    popUp.setBounds(275, 50, 260, 160);
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
