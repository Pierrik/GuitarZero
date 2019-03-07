import java.awt.*;
import javax.swing.JPanel;
import java.lang.Thread;
/**
 * Play Mode.
 *
 * @author Harper Ford
 * @author Tom Mansfield
 * @version 2.00, March 2019.
*/
public class PlayMode extends JPanel implements Runnable{

    PlayModeView view;
    PlayModeModel model;
    PlayModeController controller;

  /**
   * Sets up the Play Mode, Initialises Model, View and Controller
   * @param bundlePath the path of the bundle directory to play the song
   */
  public PlayMode(String bundlePath) {

        // Initialise the model, controller, view GUI classes
        view = new PlayModeView();
        view.setPreferredSize(new Dimension(1000,500));
        model = new PlayModeModel(bundlePath, view);
        controller = new PlayModeController(model);
        this.add(view);
        view.setVisible(true);

    }

  /**
   * The thread for Play Mode
   */
  public void run() {
      Thread modelThread = new Thread(model);
      Thread controllerThread = new Thread(controller);

      // Start the game
      modelThread.start();

      // Start the controller thread to run alongside the game
      // Limit the speed of the frames
      controllerThread.start();
      long targetTime = 30;
      while(true){
        long s = System.nanoTime();
        view.repaint();
        long elapsed = System.nanoTime() - s;

        long wait = targetTime - elapsed / 1000000;

        try {
          Thread.sleep(wait);
        } catch (Exception o) {
          o.printStackTrace();
        }

        // If a note has passed the screen without being played, drop the note
        if(view.dropNote) {
          model.dropNote();

          // Testing
          System.out.println("Note Dropped");

          view.dropNote = false;
        }
      }
    }
}
