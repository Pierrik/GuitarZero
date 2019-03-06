import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * StoreModeModel
 *
 * @author  Pierrik Mellab
 * @author  Kamila Hoffmann-Derlacka
 * @author  John Mercer
 * @version 1.00, February 2019.
 */

public class StoreModeModel {


  private int totalCurrency;
  final String HOST  = "localhost";
  final int    PORT  = 8888;

  /**
   * Skeleton constructor for later use
   */
  public StoreModeModel( ) {
    this.totalCurrency = loadTotalCurrency();
  }

  public int loadTotalCurrency() {
    // Get the user's currency from a text file
    return 0;
  }

  /**
   * Calls the rightMovement method in the carouselView file causing icons to shift right
   */
  public void right() {

    CarouselView.rightMovement();
  }

  /**
   * Calls the leftMovement method in the carouselView file causing icons to shift left
   */
  public void left() {

    CarouselView.leftMovement();
  }


  /**
   * Returns a string from the chosenOption method in the carouselView file allowing a user to
   * choose a menu option
   */
  public String select() {

    return CarouselView.chosenOption();
  }


  public void buySong(String song) {

    if (totalCurrency > 1) {
      totalCurrency --;
      
      // Downloading the bundle
      MockClient client = new MockClient(HOST, PORT);
      client.downloadFile(song, "DOWNLOAD_BUNDLE");

      // add a song to local directory
    } else {
      // make them know they dont have enough money to buy a song (make the '0' in GUI bigger for a 2 secs maybe??)
    }
  }
}
