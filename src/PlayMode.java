import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * Play Mode.
 *
 * @author Harper Ford
 * @version 2.00, March 2019.
*/
public class PlayMode extends JPanel{
    /**
     * Initialises the GUI classes for a PlayMode
     */
    public PlayMode(JFrame frame) {

        // Initialise the model, controller, view GUI classes
        PlayModeView view = new PlayModeView();
        view.setPreferredSize(new Dimension(1000,500));
        PlayModeModel      model      = new PlayModeModel(view);
        PlayModeController controller = new PlayModeController(model);
        this.add(view);
        //controller.pollGuitarForever();
    }
}
