/**
 * GameUtils.
 *
 * @author John Mercer
 * @version 1.0, March 2019
 */
public class GameUtils {

  /**
   * Makes a thread sleep.
   */
  public static void sleep(int millis){
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Changes the mode on a newly created thread.
   */
  public static void changeModeOnNewThread(Mode mode){
    ModeChanger changer = new ModeChanger(mode);
    new Thread(changer).start();
  }

}
