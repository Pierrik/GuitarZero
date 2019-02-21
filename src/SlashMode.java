import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * Slash Mode.
 *
 * @author John Mercer
 * @author Pierrik Mellab
 * @version 1.00, February 2019.
 *
 *   Windows:
 *   set CLASSPATH=jinput-2.0.9.jar;.
 *   javac PlasticGuitar.java
 *   java -Djava.library.path=. PlasticGuitar
*/
public class SlashMode {


    /**
     * Initialises the GUI classes for a courssel with menu options specific to Slash Mode
     */
    public static void main(String args[]) {

        ArrayList<JLabel> menuOptions = new ArrayList<>();


        // Create all menu option labels with their image icon and title
        JLabel label1 = new JLabel(new ImageIcon("C:\\Users\\John\\Desktop\\GuitarZero\\assets\\ExitLogo2.png"));
        JLabel label2 = new JLabel(new ImageIcon("C:\\Users\\John\\Desktop\\GuitarZero\\assets\\StoreLogo2.png"));
        JLabel label3 = new JLabel(new ImageIcon("C:\\Users\\John\\Desktop\\GuitarZero\\assets\\SelectLogo2.png"));
        JLabel label4 = new JLabel(new ImageIcon("C:\\Users\\John\\Desktop\\GuitarZero\\assets\\PlayLogo2.png"));
        JLabel label5 = new JLabel(new ImageIcon("C:\\Users\\John\\Desktop\\GuitarZero\\assets\\TutorialLogo2.png"));

        label1.setText("Exit");
        label2.setText("Store");
        label3.setText("Select");
        label4.setText("Play");
        label5.setText("Tutorial");


        // Add labels to arrayList
        menuOptions.add(label1);
        menuOptions.add(label2);
        menuOptions.add(label3);
        menuOptions.add(label4);
        menuOptions.add(label5);

        // Initialise the model, controller, view GUI classes
        CarouselModel      model      = new CarouselModel();
        CarouselController controller = new CarouselController( model );
        CarouselView       view       = new CarouselView( controller, model, menuOptions);
        view.setVisible( true );
        controller.pollGuitarForever();

    }

}
