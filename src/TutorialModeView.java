import javax.swing.*;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 * TutorialModeView.
 *
 * @author Pierrik Mellab
 * @version 1.01, March 2019.
 */
public class TutorialModeView extends JPanel {

  private ArrayList<ImageIcon> pictureIcons;

  JLabel currentPicture = null;
  ArrayList<JLabel> pictureLabels = new ArrayList<JLabel>();

  /**
   * Alters the GUI by taking commands from a model class
   *
   * @param pictureIcons: A list of pictures to display graphically
   */
  public TutorialModeView (ArrayList<ImageIcon> pictureIcons) {

    this.pictureIcons = pictureIcons;


    setBounds(0, 0, 1000, 500);

    for (ImageIcon pictureIcon : pictureIcons) {
      JLabel label = new JLabel(pictureIcon);
      label.setBounds(50, 50, 400, 400);
      add(label);
      label.setVisible(false);
      pictureLabels.add(label);
    }

    pictureLabels.get(0).setVisible(true);
    currentPicture = pictureLabels.get(0);

    setVisible(true);
  }


  /**
   * Switches images to the left, replacing with next picture
   */
  public void leftMovement() {

    for (int i = 0; i < pictureLabels.size(); i++) {

      if (pictureLabels.get(i) == currentPicture && i != 0) {

        pictureLabels.get(i).setVisible(false);
        pictureLabels.get(i-1).setVisible(true);
        currentPicture = pictureLabels.get(i-1);

        setVisible(true);

        break;

      } else if (pictureLabels.get(i) == currentPicture && i == 0){

        break;
      }
    }
  }



  /**
   * Switches images to the right, replacing with next picture
   */
  public void rightMovement() {

    for (int i = 0; i < pictureLabels.size(); i++) {

      if (pictureLabels.get(i) == currentPicture && i != pictureLabels.size()-1) {

        pictureLabels.get(i).setVisible(false);
        pictureLabels.get(i + 1).setVisible(true);
        currentPicture = pictureLabels.get(i + 1);

        setVisible(true);

        break;

      } else if (pictureLabels.get(i) == currentPicture && i == pictureLabels.size()-1) {

        break;

      }
    }

  }

}
