import java.util.concurrent.atomic.AtomicBoolean;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 * PlayModeController.
 *
 * @author  Kamila Hoffmann-Derlacka
 * @author  John Mercer
 * @author Harper Ford
 * @version 2.0, March 2019.
 *
 *   Linux/Mac:
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac PlayModeController.java
 *   $ java -Djava.library.path=. PlayModeController
 *
 *   Windows:
 *   set CLASSPATH=jinput-2.0.9.jar;.
 *   javac PlayModeController.java
 *   java -Djava.library.path=. PlayModeController
 */
public class PlayModeController implements Runnable {
  final static String GUITAR_HERO = "Guitar Hero";
  final static int    BUTTON_DELAY     = 250;
  final static int    POLL_DELAY       = 150;

  private  PlayModeModel model;
  // make them non static when not using main anymore
  private ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
  private net.java.games.input.Controller[] ctrls = cenv.getControllers();

  final static double BUTTON_THRESHOLD = 1.0;

  // Buttons
  final static int    WHITE1           = 0;
  final static int    BLACK1           = 1;
  final static int    WHITE2           = 4;
  final static int    BLACK2           = 2;
  final static int    WHITE3           = 5;
  final static int    BLACK3           = 3;
  final static int    ZERO_POWER       = 8;
  final static int    ESCAPE           = 10;
  final static int    BENDER_CLICK     = 12;

  // Variables that change for different operating systems, default: windows
  static int          BENDER_ROUND     = 13;
  static int          WHAMMY           = 14;

  private final AtomicBoolean controller_running = new AtomicBoolean(false);

  public PlayModeController(PlayModeModel model){
    this.model = model;
  }

  /*
   * Poll forever, and altering model depending on buttons pressed
   */
  public void pollForever(Controller ctrl) {
    Component[] allCmps    = ctrl.getComponents();
    float[]     vals       = new float[allCmps.length];
    int         previous1;
    int         previous2;
    int         previous3;

    controller_running.set(true);
    while(controller_running.get()) {
      GameUtils.sleep(POLL_DELAY);

      if (ctrl.poll()) {

        model.resetAll();
        for ( int i = 0; i < allCmps.length; i++ ) {
          vals[i] = allCmps[i].getPollData();
        }
        previous1 = -1;
        previous2 = -1;
        previous3 = -1;

        for ( int i = 0; i < allCmps.length; i++ ) {
          float val = vals[i];
            if (i == WHITE1 || i == BLACK1) {
              if (val == BUTTON_THRESHOLD  && previous1 == -1){
                previous1 = i;
                model.displayNoteOn(i);
              }
            } else if (i == WHITE2 || i == BLACK2) {
              if (val == BUTTON_THRESHOLD  && previous2 == -1){
                previous2 = i;
                model.displayNoteOn(i);
              }
            } else if (i == WHITE3 || i == BLACK3) {
              if (val == BUTTON_THRESHOLD  && previous3 == -1){
                previous3 = i;
                model.displayNoteOn(i);
              }
            } else if (i == ZERO_POWER) {
              if (val == BUTTON_THRESHOLD) {
                if (model.getCurrentTick() >= model.startZeroPower
                    && model.getCurrentTick() <= model.endZeroPower) {
                  clickedButtons(previous1, previous2, previous3);
                }
              }
            } else if (i == ESCAPE) { // escape button
              if (val == BUTTON_THRESHOLD) {
                System.out.println("Escape - PLAY");
                this.model.playSong.song_running.set(false);
                controller_running.set(false);
                PlayMode.playmode_running.set(false);
              }
            } else if (i == WHAMMY) { // whammy  16 in linux
              if (val >= BUTTON_THRESHOLD) {
                if (model.getCurrentTick() >= model.startZeroPower
                    && model.getCurrentTick() <= model.endZeroPower) {
                  clickedButtons(previous1, previous2, previous3);
                }
              }
            } else if (i == BENDER_CLICK) { // bender click
              if (val > 0) {
                // action
              }
            } else if (i == BENDER_ROUND) { // bender round linux 17
              if (val > 0) {
                if (model.getCurrentTick() <= model.startZeroPower
                    || model.getCurrentTick() >= model.endZeroPower)
                  clickedButtons(previous1, previous2, previous3);
              }
          }
        }
      }
    }
  }

  private void clickedButtons(int previous1, int previous2, int previous3) {
     if (previous1 == WHITE1) {
        if (previous2 == WHITE2) {
          if (previous3 == WHITE3) {
            model.checkNote("222");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("221");
          }
          else {
            model.checkNote("220");
          }
        }
        else if (previous2 == BLACK2) {
          if (previous3 == WHITE3) {
            model.checkNote("212");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("211");
          }
          else {
            model.checkNote("210");
          }
        }
        else {
          if (previous3 == WHITE3) {
            model.checkNote("202");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("201");
          }
          else {
            model.checkNote("200");
          }
        }
      }
      else if (previous1 == BLACK1) {
        if (previous2 == WHITE2) {
          if (previous3 == WHITE3) {
            model.checkNote("122");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("121");
          }
          else {
            model.checkNote("120");
          }
        } else if (previous2 == BLACK2) {
          if (previous3 == WHITE3) {
            model.checkNote("112");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("111");
          }
          else {
            model.checkNote("110");
          }
        } else {
          if (previous3 == WHITE3) {
            model.checkNote("102");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("101");
          }
          else {
            model.checkNote("100");
          }
        }
      }
      else {
        if (previous2 == WHITE2) {
          if (previous3 == WHITE3) {
            model.checkNote("022");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("021");
          }
          else {
            model.checkNote("020");
          }
        }
        else if (previous2 == BLACK2) {
          if (previous3 == WHITE3) {
            model.checkNote("012");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("011");
          }
          else {
            model.checkNote("010");
          }
        }
        else {
          if (previous3 == WHITE3) {
            model.checkNote("002");
          }
          else if (previous3 == BLACK3) {
            model.checkNote("001");
          }
          else {
            model.checkNote("000");
          }
        }
      }
  }

  /*
   * Finds GH controller and polls it forever. If none found, error is printed and program
   * terminates.
   */
  public void pollGuitarForever() {

    if (Run.OSvalidator() == 'm') {
      BENDER_ROUND     = 13;
      WHAMMY           = 17;
    } else if (Run.OSvalidator() == 'u') {
      BENDER_ROUND     = 17;
      WHAMMY           = 16;
    }

    for ( Controller ctrl : ctrls ) {
      if ( ctrl.getName().contains( GUITAR_HERO ) ) {
        pollForever( ctrl );
      }
    }
  }

  @Override
  public void run() {
    this.pollGuitarForever();
  }
}
