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

  /**
   * Skeleton constructor for later use
   */
  public CarouselModel(CarouselView view) {
    this.view = view;
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

    System.out.println(view.chosenOption());
    return view.chosenOption();
  }

}
