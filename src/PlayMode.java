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
public class PlayMode extends JPanel implements Runnable {

  // Play Mode MVC
  PlayModeView view;
  PlayModeModel model;
  PlayModeController controller;

  // Stores the current state of the game
  static AtomicBoolean playmode_running = new AtomicBoolean(false);

  // Screen dimensions
  private static final int SCREEN_WIDTH  = 1000;
  private static final int SCREEN_HEIGHT = 563;

  // Limit the speed of the frames
  private static long      TARGET_TIME   = 30;
  private static long      MILLION       = 1000000;

  /**
   * Sets up the Play Mode, Initialises Model, View and Controller
   * @param bundlePath the path of the bundle directory to play the song
   */
  public PlayMode(String bundlePath) {

    // Initialise the model, controller, view GUI classes
    view = new PlayModeView();
    model = new PlayModeModel(bundlePath, view);
    controller = new PlayModeController(model);

    // Set the screen size of the view
    view.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
    this.add(view);
    view.setVisible(true);

    if(!model.startGame) {
      // print error?
      GameUtils.changeModeOnNewThread(Mode.SLASH);
    }

    }

  /**
   * The thread for Play Mode
   * Continuously repaints the screen and checks whether any notes have not been played
   */
  @Override
  public void run() {

    // Start the game if no exceptions have occurred in loading the game
    if(model.startGame) {

      // Start model and controller threads
      new Thread(model).start();
      new Thread(controller).start();
      playmode_running.set(true);
    }

    // Repaints the screen constantly while the game is still playing
    while (playmode_running.get()) {

      long s = System.nanoTime();
      view.revalidate();
      view.repaint();

      // Limit the screen refresh to 30 frames per second
      long elapsed = System.nanoTime() - s;
      long wait = TARGET_TIME - elapsed / MILLION;
      try {
        Thread.sleep(wait);
      } catch (Exception o) {
        o.printStackTrace();
      }

    }

    GameUtils.changeModeOnNewThread(Mode.SLASH);
  }
}
