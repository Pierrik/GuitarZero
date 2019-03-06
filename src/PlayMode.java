import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.lang.Thread;
/**
 * Play Mode.
 *
 * @author Harper Ford
 * @version 2.00, March 2019.
*/
public class PlayMode extends JPanel implements Runnable{
    /**
     * Initialises the GUI classes for a PlayMode
     */
    PlayModeView view;
    PlayModeModel model;
    public PlayMode(String bundlePath) {

        // Initialise the model, controller, view GUI classes
        view = new PlayModeView();
        view.setPreferredSize(new Dimension(1000,500));
        model = new PlayModeModel(bundlePath, view);
        //PlayModeController controller = new PlayModeController(model);
        this.add(view);
        view.setVisible(true);
        //controller.pollForever();
    }

    public void run() {
      Thread modelThread = new Thread(model);
      modelThread.start();
      long targetTime = 30;
      while(true){
        long s = System.nanoTime();
        view.repaint();
        long elapsed = System.nanoTime() - s;

        long wait = targetTime - elapsed / 1000000;
        //System.out.println(wait);
        try {
          Thread.sleep(wait);
        } catch (Exception o) {
          o.printStackTrace();
        }
      }
    }
}
