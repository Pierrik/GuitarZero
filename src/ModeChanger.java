/**
 * Mode Changer.
 *
 * @author John Mercer
 * @version 1.0, March 2019
 */
public class ModeChanger implements Runnable{
  private Mode mode;

  public ModeChanger(Mode mode){
    this.mode = mode;
  }

  public void run() {
    Run.changeMode(mode);
  }
}
