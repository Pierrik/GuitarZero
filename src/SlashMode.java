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



    public static void main(String args[]) {

        /**
        ArrayList<ImageIcon> icons = new ArrayList<>();
        icons.add(new ImageIcon("assets\\TutorialLogo2.png"));
        icons.add(new ImageIcon("assets\\PlayLogo2.png"));
        icons.add(new ImageIcon("assets\\SelectLogo2.png"));
        icons.add(new ImageIcon("assets\\ExitLogo2.png"));
        icons.add(new ImageIcon("assets\\StoreLogo2.png"));
        **/


        ArrayList<JLabel> menuOptions = new ArrayList<>();


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


        menuOptions.add(label1);
        menuOptions.add(label2);
        menuOptions.add(label3);
        menuOptions.add(label4);
        menuOptions.add(label5);

        CarouselModel      model      = new CarouselModel();
        CarouselController controller = new CarouselController( model );
        CarouselView       view       = new CarouselView( controller, model, menuOptions);
        view.setVisible( true );
        controller.pollGuitarForever();

    }

}
