import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

// Manages the display model


/**
 * CarouselView.
 *
 * @author Pierrik Mellab
 * @modified Harper Ford
 * @modified Kamila Hoffmann-Derlacka
 * @version 1.00, February 2019.
 */
public class CarouselView extends JPanel {

  private ArrayList<JLabel> menuOptions = new ArrayList<>();
  private ArrayList<Rectangle> bounds = new ArrayList<>();
  private int carouselLength = 0;

  /**
   * Alters the GUI by taking commands from a model class
   *
   * @param allOptions: A list of options to display graphically
   */
  public CarouselView(ArrayList<JLabel> allOptions) {

    JLabel carousel = new JLabel(new ImageIcon("../assets/carousel.png"));
    carousel.setLayout(null);
    carouselLength = allOptions.size();
    initialiseBounds(carouselLength);

    // sets private menuOptions to, passed in variable, allOptions
    for (JLabel label : allOptions) {
      menuOptions.add(label);
      carousel.add(label);
    }

    // sets the initial menuOption bounds
    for (int i = 0; i < allOptions.size(); i++) {
      menuOptions.get(i).setBounds(bounds.get(i));
    }
    this.add(carousel);
    this.setBounds(80, 65, 740, 150);
  }

  /**
   * Selects the center option of the Carousel
   *
   * @return The title of the selected song
   */
  public String chosenOption() {

    String optionTitle = null;

    for (JLabel label : menuOptions) {

      if (label.getX() == bounds.get(2).x) {
        optionTitle = label.getText();
        System.out.println(optionTitle);
      }
    }
    return optionTitle;
  }

  /**
   * Shifts the JLabels left, called from the CarouselModel object
   */
  public void leftMovement() {

    for (JLabel label : menuOptions) {

      for (int i = 0; i < carouselLength; i++) {

        if (label.getX() == bounds.get(0).x) {
          label.setBounds(bounds.get(carouselLength-1));
          break;

        } else if (label.getX() == bounds.get(i).x && bounds.get(i).x != 0) {
          label.setBounds(bounds.get(i - 1));
          break;
        }

        if(i-1 < 5) {
          label.setVisible(true);

        } else {
          label.setVisible(false);

        }
      }
    }
  }

  /**
   * Shifts the JLabels right, called from the CarouselModel object
   */
  public void rightMovement() {

    for (JLabel label : menuOptions) {

      for (int i = 0; i < carouselLength; i++) {

        if (label.getX() == bounds.get(carouselLength-1).x) {
          label.setBounds(bounds.get(0));
          break;

        } else if (label.getX() == bounds.get(i).x && bounds.get(i).x != bounds.get(carouselLength-1).x) {
          label.setBounds(bounds.get(i + 1));
          break;
        }

        if(i-1 < 5) {
          label.setVisible(true);

        } else {
          label.setVisible(false);

        }
      }
    }
  }
  
    /*
     * Sets the JLabel bounds so are displayed correctly
     */
    public void initialiseBounds( int labelLength){
      for (int i = 0; i <= labelLength; i++) {
          bounds.add(new Rectangle((i * 150)+40, 65, 140, 140));
      }
    }
}
