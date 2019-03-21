/**
 * GameUtils.
 *
 * @author John Mercer
 * @version 1.0, March 2019
 */
public class GameUtils {

  public static void sleep(int millis){
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void changeModeOnNewThread(Mode mode){
    ModeChanger changer = new ModeChanger(mode);
    new Thread(changer).start();
  }

}
