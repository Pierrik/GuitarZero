import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;


// Manages the display model


/*
 * CarouselView.
 *
 * @author  Pierrik Mellab
 * @version 1.00, February 2019.
 */
public class CarouselView extends JFrame {

    private CarouselModel model;
    private JPanel panel;

    private static ArrayList<JLabel> menuOptions = new ArrayList<>();

    private static Rectangle [] bounds = {
            new Rectangle(30, 50, 140, 140),
            new Rectangle(185, 50, 140, 140),
            new Rectangle(340, 50, 140, 140),
            new Rectangle(495, 50, 140, 140),
            new Rectangle(650, 50, 140, 140)
    };


    public CarouselView (CarouselController controller, CarouselModel model, ArrayList<JLabel> allOptions) {
        this.model = model;

        setContentPane(new JLabel(new ImageIcon("C:\\Users\\John\\Desktop\\GuitarZero\\assets\\carousel.PNG")));

        // Creates panel and sets to correct size/ layout
        panel = new JPanel();
        panel.setSize(750, 220);
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);


        // sets private menuOptions to, passed in variable, allOptions
        for (JLabel label : allOptions) {
            menuOptions.add(label);
        }


        // sets the initial menuOption bounds
        for (int i = 0; i < 5; i++) {
            menuOptions.get(i).setBounds(bounds[i]);
            add(menuOptions.get(i));
        }



        this.add(panel, BorderLayout.CENTER);
        this.pack();
        this.setSize( 900, 300 );
        this.setResizable(false);

    }


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