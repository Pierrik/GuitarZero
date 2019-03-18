import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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

  /**
   * Skeleton constructor for later use
   */
  public CarouselModel(CarouselView view) {
    this.view = view;
    this.totalCurrency = 5; //Currency.loadTotalCurrency();
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

    if (totalCurrency > 1 && !isInLocalDir(songName)) {
      totalCurrency --;

      MockClient client = new MockClient(HOST, PORT);
      client.downloadFile(bundleName, "DOWNLOAD_BUNDLE");
      String bundlesDir = BUNDLES + songName + "/";
      MockClient.unzip(BUNDLES + bundleName, bundlesDir);

    } else {
      // make them know they dont have enough money to buy a song (make the '0' in GUI bigger for a 2 secs maybe??)
    }
  }

  /**
   * Checks if the song is already in the local directory.
   */
  public static boolean isInLocalDir(String song) {
    // checking if local_store directory exists
    String cd = System.getProperty("user.dir");
    String bundleDir = cd + "/local_store/bundle_files/";

    if (Files.notExists(Paths.get(bundleDir))) {
      return false;
    } else {
      File tmpDir = new File("local_store/bundle_files/" + song);
      if (tmpDir.exists())
        return true;
      else
        return false;
    }
  }

}
