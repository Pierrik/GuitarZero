import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
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

    AtomicBoolean playmode_running = new AtomicBoolean(false);

  /**
   * Sets up the Play Mode, Initialises Model, View and Controller
   * @param bundlePath the path of the bundle directory to play the song
   */
  public PlayMode(String bundlePath) {

        // Initialise the model, controller, view GUI classes
        view = new PlayModeView();
        //view.setPreferredSize(new Dimension(1000,500));
        view.setPreferredSize(new Dimension(1000,563));
        model = new PlayModeModel(bundlePath, view);
        controller = new PlayModeController(model);
        this.add(view);
        view.setVisible(true);

    }

  /**
   * The thread for Play Mode
   * Continuously repaints the screen and checks whether any notes have not been played
   */
  @Override
  public void run() {
    Thread modelThread = new Thread(model);
    Thread controllerThread = new Thread(controller);

    // Start the game
    modelThread.start();

    // Start the controller thread to run alongside the game
    // Limit the speed of the frames
    controllerThread.start();
    long targetTime = 30;

    playmode_running.set(true);
    while (playmode_running.get()) {
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

    }
  }
}
