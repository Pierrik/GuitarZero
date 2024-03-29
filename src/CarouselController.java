import java.util.concurrent.atomic.AtomicBoolean;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
/**
 * CarouselController.
 *
 * @author  John Mercer
 * @author  Kamila Hoffmann-Derlacka
 * @version 1.5, March 2019.
 *
 */
public class CarouselController implements Runnable {

  final static String   GUITAR_HERO    = "Guitar Hero";
  final static int      POLL_DELAY     = 100;

  CarouselModel         model;
  ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
  Controller[]          ctrls = cenv.getControllers();
  Mode                  mode;

  final static int    BUTTONS          = 3;
  final static double BUTTON_THRESHOLD = 1.0;
  final static double STRUM_THRESHOLD  = 0.75;
  final static int    ZERO_POWER       = 8;
  final static int    ESCAPE           = 10;

  // variables that change for different operating systems, default: windows
  static int          STRUM            = 16;

  AtomicBoolean controllerOn = new AtomicBoolean(false);


  public CarouselController(CarouselModel model, Mode mode){
    this.model = model;
    this.mode = mode;
  }

  /*
   * Poll forever, and altering model depending on buttons pressed
   */
  private void pollForever(Controller ctrl) {
    Component[] allCmps    = ctrl.getComponents();
    float[]     vals       = new float[BUTTONS];
    Component[] activeCmps = {allCmps[ZERO_POWER], allCmps[ESCAPE], allCmps[STRUM]};

    controllerOn.set(true);
    while (controllerOn.get()) {
      GameUtils.sleep(POLL_DELAY);

      if (ctrl.poll()) {
        for ( int i = 0; i < BUTTONS; i++ ) {
          vals[i] = activeCmps[i].getPollData();
        }

        for ( int i = 0; i < BUTTONS; i++ ) {
          float val = vals[i];

          switch(i){
            // zero-power button
            case 0 :
              if (val == BUTTON_THRESHOLD) {
                switch (this.mode) {
                  case SLASH:
                    System.out.println("Zero power pressed - SLASH");
                    controllerOn.set(false);    // interrupting current controller thread
                    String selectedMode = model.select();
                    GameUtils.changeModeOnNewThread(Mode.valueOf(selectedMode));
                    break;

                  case SELECT:
                    System.out.println("Zero power pressed - SELECT");
                    controllerOn.set(false);
                    String selectedSong = model.select();
                    Run.currentBundleDir = "../local_store/bundle_files/" + selectedSong + "/";
                    GameUtils.changeModeOnNewThread(Mode.SLASH);
                    break;

                  case STORE:
                    System.out.println("Zero power pressed - STORE");
                    controllerOn.set(false);
                    String selectedBundle = model.select();
                    model.buySong(selectedBundle);
                    GameUtils.changeModeOnNewThread(Mode.SLASH);
                    break;

                }
                GameUtils.sleep(1250); // still needed?
              }
              break;


            // escape button
            case 1 :
              if (this.mode != Mode.SLASH && val >= BUTTON_THRESHOLD) {
                // go back to slash mode if outside of slash mode
                System.out.println("Escape button pressed");
                controllerOn.set(false);
                GameUtils.changeModeOnNewThread(Mode.SLASH);
              }
              break;

            // strum
            case 2 :
              if (val >= STRUM_THRESHOLD){
                model.right();
                System.out.println("Strum right");
              } else if (val <= -STRUM_THRESHOLD){
                model.left();
                System.out.println("Strum left");
              }
              break;
          }
        }
      }
    }
  }

  /*
   * Recognises the OS and sets the constants. Finds GH controller and polls it forever. If none found, error is printed and program
   * terminates.
   */
  public void pollGuitarForever(){

    if (Run.OSvalidator() == 'm') {
      STRUM            = 15;
    } else if (Run.OSvalidator() == 'u') {
      STRUM            = 14;
    }

    for (Controller ctrl : ctrls) {
      if (ctrl.getName().contains(GUITAR_HERO)) {
        pollForever(ctrl);
      }
    }

    /*
    System.out.println(GUITAR_HERO + " controller not found");
    System.exit(1);
    */
  }

  @Override
  public void run() {
    this.pollGuitarForever();
  }

}