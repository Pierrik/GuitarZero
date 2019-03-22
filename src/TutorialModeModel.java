/**
 * CarouselModel
 *
 * @author  Pierrik Mellab
 * @version 1.00, March 2019.
 */

public class TutorialModeModel {
  TutorialModeView view;

  public TutorialModeModel (TutorialModeView view) {
    this.view = view;
  }

  /**
   * Calls the rightMovement method in the tutorialView file causing icons to shift right
   */
  public void right() {
    view.rightMovement();
  }

  /**
   * Calls the leftMovement method in the tutorialView file causing icons to shift left
   */
  public void left() {
    view.leftMovement();
  }

}
