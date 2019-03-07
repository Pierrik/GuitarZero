import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;


// Manages the display model


/**
 * CarouselView.
 *
 * @author Pierrik Mellab
 * @modified by Harper Ford
 * @modified by Kamila Hoffmann-Derlacka
 * @version 1.00, February 2019.
 */
public class CarouselView extends JPanel {

  private CarouselModel model;
  private JPanel panel;

  private static ArrayList<JLabel> menuOptions = new ArrayList<>();
  private static ArrayList<Rectangle> bounds = new ArrayList<>();
  private static int carouselLength = 0;

  /**
   * Alters the GUI by taking commands from a model class
   *
   * @param allOptions: A list of options to display graphically
   */
  public CarouselView(ArrayList<JLabel> allOptions) {

    this.add(new JLabel(new ImageIcon("../assets/carousel.PNG")));

    carouselLength = allOptions.size();
    initialiseBounds(carouselLength);

    // Creates panel and sets to correct size/ layout
    panel = new JPanel();
    //panel.setSize(610, 150);
    panel.setBackground(Color.WHITE);
    panel.setLayout(null);

    // sets private menuOptions to, passed in variable, allOptions
    for (JLabel label : allOptions) {
      menuOptions.add(label);
    }

    // sets the initial menuOption bounds
    for (int i = 0; i < allOptions.size(); i++) {
      menuOptions.get(i).setBounds(bounds.get(i));
      panel.add(menuOptions.get(i));
    }

    panel.setBounds(80, 65, 740, 150);
    this.setSize(900, 300);
  }

  /**
   * Selects the center option of the Carousel
   *
   * @return The title of the selected song
   */
  public static String chosenOption() {

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
  public static void leftMovement() {

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
  public static void rightMovement() {

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

    public static void initialiseBounds( int labelLength){
      for (int i = 0; i <= labelLength; i++) {
          bounds.add(new Rectangle(i * 150, 0, 140, 140));
      }
    }
}
