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
 * @author  Pierrik Mellab
 * @author  Harper Ford (Javadoc)
 * @version 1.00, February 2019.
 */
public class CarouselView extends JFrame {

    private CarouselModel model;
    private JPanel panel;

    private static ArrayList<JLabel> menuOptions = new ArrayList<>();

    private static Rectangle [] bounds = {
            new Rectangle(0, 0, 140, 140),
            new Rectangle(150, 0, 140, 140),
            new Rectangle(300, 0, 140, 140),
            new Rectangle(450, 0, 140, 140),
            new Rectangle(600, 0, 140, 140)
    };

    /**
     * Alters the GUI by taking commands from a model class
     * @param controller: The object that reads the guitar/keyboard inputs
     * @param model:      The object that takes the controller inputs then calls the corresponding CarouselView function
     * @param allOptions: A list of options to display graphically
     */
    public CarouselView (CarouselController controller, CarouselModel model, ArrayList<JLabel> allOptions) {
        this.model = model;

        setContentPane(new JLabel(new ImageIcon("..\\assets\\carousel.PNG")));

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
        for (int i = 0; i < 5; i++) {
            menuOptions.get(i).setBounds(bounds[i]);
            panel.add(menuOptions.get(i));
        }

        setLayout(null);
        panel.setBounds(80,65,740,150);
        this.add(panel);
        this.pack();
        this.setSize( 900, 300 );
        this.setResizable(false);

    }

    /**
     * Selects the center option of the Carousel
     * @return The title of the selected song
     */
    public static String chosenOption () {

        String optionTitle = null;

        for (JLabel label : menuOptions) {

            if (label.getX() == bounds[2].x) {
                optionTitle = label.getText();
              System.out.println(optionTitle);
            }

        }

        return optionTitle;
    }

    /**
     * Shifts the JLabels left, called from the CarouselModel object
     */
    public static void leftMovement () {

        for (JLabel label : menuOptions) {

            if (label.getX() == bounds[0].x) {
                label.setBounds(bounds[4]);

            } else if (label.getX() == bounds[1].x) {
                label.setBounds(bounds[0]);

            } else if (label.getX() == bounds[2].x) {
                label.setBounds(bounds[1]);

            } else if (label.getX() == bounds[3].x) {
                label.setBounds(bounds[2]);

            } else if (label.getX() == bounds[4].x) {
                label.setBounds(bounds[3]);

            }

        }
    }

    /**
     * Shifts the JLabels right, called from the CarouselModel object
     */
    public static void rightMovement () {

        for (JLabel label : menuOptions) {

            if (label.getX() == bounds[0].x) {
                label.setBounds(bounds[1]);

            } else if (label.getX() == bounds[1].x) {
                label.setBounds(bounds[2]);

            } else if (label.getX() == bounds[2].x) {
                label.setBounds(bounds[3]);

            } else if (label.getX() == bounds[3].x) {
                label.setBounds(bounds[4]);

            } else if (label.getX() == bounds[4].x) {
                label.setBounds(bounds[0]);

            }

        }
    }


}
