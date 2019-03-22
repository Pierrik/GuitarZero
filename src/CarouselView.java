import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JLabel;

// Manages the display model

/**
 * CarouselView.
 *
 * @author Pierrik Mellab
 * @modified by Harper Ford
 * @modified by Kamila Hoffmann-Derlacka
 * @version 2.00, March 2019.
 */
public class CarouselView extends JPanel {

  private ArrayList<JLabel> menuOptions = new ArrayList<>();
  private ArrayList<Rectangle> bounds = new ArrayList<>();
  private JLabel currency = null;
  private int carouselLength = 0;
  Mode mode;

  final static int CV_WIDTH      = 740;
  final static int CV_HEIGHT     = 150;
  final static int CV_Y          = 65;
  final static int CV_X          = 80;
  final static int MAX_X_BOUND   = 630;
  final static int BOUNDS_X_1    = 180;
  final static int BOUNDS_X_2    = 330;
  final static int BOUNDS_X_3    = 480;
  final static int BOUNDS_Y      = 50;
  final static int BOUNDS_WIDTH  = 140;
  final static int BOUNDS_HEIGHT = 160;
  final static int EXTRA         = 30;

  /**
   * Alters the GUI by taking commands from a model class
   *
   * @param allOptions: A list of options to display graphically
   */
  public CarouselView(ArrayList<JLabel> allOptions, Mode mode) {

    this.mode = mode;

    JLabel carousel = new JLabel(new ImageIcon("../assets/carousel.png"));
    carousel.setLayout(null);
    carouselLength = allOptions.size();
    initialiseBounds(carouselLength);

    // sets private menuOptions to, passed in variable, allOptions
    for (JLabel label : allOptions) {
      label.setVerticalTextPosition(JLabel.BOTTOM);
      label.setHorizontalTextPosition(JLabel.CENTER);
      menuOptions.add(label);
      carousel.add(label);
    }

    // sets the initial menuOption bounds when 5 items or more are in the carousel
    if (allOptions.size() > 4) {
      for (int i = 0; i < allOptions.size(); i++) {
        menuOptions.get(i).setBounds(bounds.get(i));
        if (i > 4) {
          menuOptions.get(i).setVisible(false);
        } else {
          menuOptions.get(i).setVisible(true);
        }
      }
    }


    // sets the initial menuOption bounds when 4 items are in the carousel
    if (allOptions.size() == 4) {
      menuOptions.get(0).setBounds(bounds.get(0));
      menuOptions.get(1).setBounds(bounds.get(1));
      menuOptions.get(2).setBounds(bounds.get(2));
      menuOptions.get(3).setBounds(bounds.get(3));
    }

    // sets the initial menuOption bounds when 3 items are in the carousel
    if (allOptions.size() == 3) {
      menuOptions.get(0).setBounds(bounds.get(0));
      menuOptions.get(1).setBounds(bounds.get(1));
      menuOptions.get(2).setBounds(bounds.get(2));
    }

    this.add(carousel);
    this.setBounds(CV_X, CV_Y, CV_WIDTH, CV_HEIGHT);

  }

  /**
   * Displays the users currency when called
   */
  public void displayCurrency(){
    try {
      currency = new JLabel("Currency: " + Currency.loadCurrencyFile(Currency.findCurrencyFile()));
      currency.setBounds(0,0,40,40);
      currency.setVisible(true);
    }
    catch(Exception e){
      System.out.println("Currency JLabel can't be created. /CAROUSELVIEW");
    }
    this.add(currency);
  }

  /**
   * Selects the center option of the Carousel
   *
   * @return The title of the selected song
   */
  public String chosenOption() {

    String optionTitle = null;

    for (JLabel label : menuOptions) {

      if (carouselLength == 3) {
        if (label.getX() == bounds.get(1).x) {
          optionTitle = label.getText();
          System.out.println(optionTitle);
        }

      } else {
        if (label.getX() == bounds.get(2).x) {
          optionTitle = label.getText();
          System.out.println(optionTitle);
        }
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

        } else if (label.getX() == bounds.get(i).x) {
          label.setBounds(bounds.get(i - 1));
          break;
        }
      }
    }

    for (JLabel label : menuOptions) {
      if (label.getX() > MAX_X_BOUND) {
        label.setVisible(false);
      } else {
        label.setVisible(true);
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

        } else if (label.getX() == bounds.get(i).x) {
          label.setBounds(bounds.get(i + 1));
          break;
        }

      }
    }


    for (JLabel label : menuOptions) {
      if (label.getX() > MAX_X_BOUND) {
        label.setVisible(false);
      } else {
        label.setVisible(true);
      }
    }
  }

  public void initialiseBounds( int labelLength){

    if (carouselLength == 3) {
      bounds.add(new Rectangle(BOUNDS_X_1, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT));
      bounds.add(new Rectangle(BOUNDS_X_2, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT));
      bounds.add(new Rectangle(BOUNDS_X_3, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT));

    } else {
      for (int i = 0; i <= labelLength; i++) {
        bounds.add(new Rectangle((i * CV_HEIGHT) + EXTRA, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT));
      }
    }
  }

}
