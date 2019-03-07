import java.io.File;
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
 * @author Harper Ford
 * @version 2.0, March 2019.
 *
 *   Linux/Mac:
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   javac SlashMode.java
 *   java -Djava.library.path=. SlashMode
 *
 *   Windows:
 *   set CLASSPATH=jinput-2.0.9.jar;.
 *   javac SlashMode.java
 *   java -Djava.library.path=. SlashMode
 *
 *
 *   Mac/ Linux:
 *   $ (CD src)
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac SlashMode.java
 *   $ java -Djava.library.path=. SlashMode
*/
public class SlashMode extends JPanel{

    CarouselView view;
    CarouselModel model;
    CarouselController controller;

    /**
     * Initialises the GUI classes for a courel with menu options specific to Slash Mode
     */
    public SlashMode() {
        ArrayList<JLabel> menuOptions = new ArrayList<>();


        // Create all menu option labels with their image icon and title
        JLabel label1 = new JLabel(new ImageIcon("../assets/ExitLogo2.png"));
        JLabel label2 = new JLabel(new ImageIcon("../assets/StoreLogo2.png"));
        JLabel label3 = new JLabel(new ImageIcon("../assets/SelectLogo2.png"));
        JLabel label4 = new JLabel(new ImageIcon("../assets/PlayLogo2.png"));
        JLabel label5 = new JLabel(new ImageIcon("../assets/TutorialLogo2.png"));

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
        CarouselView       view       = new CarouselView(menuOptions);
        CarouselModel      model      = new CarouselModel(view);
        CarouselController controller = new CarouselController( model );
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        this.add(view);
    }
}
