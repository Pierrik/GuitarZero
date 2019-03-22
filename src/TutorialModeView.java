import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JLabel;

// Manages the display model

/**
 * TutorialModeView.
 *
 * @author Pierrik Mellab
 * @version 1.01, March 2019.
 */
public class TutorialModeView extends JPanel {

  private ArrayList<ImageIcon> pictureIcons = new ArrayList<>();

  JLabel picture;

  /**
   * Alters the GUI by taking commands from a model class
   *
   * @param pictureIcons: A list of pictures to display graphically
   */
  public TutorialModeView (ArrayList<ImageIcon> pictureIcons) {

    this.pictureIcons = pictureIcons;

    picture = new JLabel(pictureIcons.get(0));
    picture.setBounds(50, 50, 300, 300);

    this.add(picture);
    this.setBounds(0, 0, 1000, 500);

  }


  /**
   * Switches images to the left, replacing with next picture
   */
  public void leftMovement() {

    for (int i = 0; i < pictureIcons.size(); i++) {

      if (picture.getIcon() == pictureIcons.get(i) && i != 0) {
        picture = new JLabel(pictureIcons.get(i-1));
        break;

      } else if (i == 0) {
        picture = new JLabel(pictureIcons.get(pictureIcons.size()-1));
        break;
      }
    }
  }



  /**
   * Switches images to the right, replacing with next picture
   */
  public void rightMovement() {

    for (int i = 0; i < pictureIcons.size(); i++) {

      if (picture.getIcon() == pictureIcons.get(i) && i != pictureIcons.size()-1) {
        picture = new JLabel(pictureIcons.get(i+1));
        break;

      } else if (i == pictureIcons.size()-1) {
        picture = new JLabel(pictureIcons.get(0));
        break;
      }
    }
  }


}
