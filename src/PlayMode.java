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
 * @version 1.00, February 2019.
*/
public class PlayMode extends JPanel{
    /**
     * Initialises the GUI classes for a PlayMode
     */
    public PlayMode() {
        // Initialise the model, controller, view GUI classes
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        PlayModeView view = new PlayModeView();
        //this.add(view);
        //PlayModeModel      model      = new PlayModeModel(view);
        //PlayModeController controller = new PlayModeController(model);
        //controller.pollGuitarForever();
    }
}
