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
 *   Linux/Mac:
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac CarouselController.java
 *   $ java -Djava.library.path=. CarouselController
 *
 *   Windows:
 *   set CLASSPATH=jinput-2.0.9.jar;.
 *   javac CarouselController.java
 *   java -Djava.library.path=. CarouselController
 */
public class CarouselController {

  final static String GUITAR_HERO      = "Guitar Hero";
  final static int    DELAY            = 150;

  private CarouselModel         model;
  private ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
  private Controller[]          ctrls = cenv.getControllers();

  final static int    BUTTONS          = 3;
  final static double BUTTON_THRESHOLD = 1.0;
  final static double STRUM_THRESHOLD  = 0.75;
  final static int    ZERO_POWER       = 8;
  final static int    ESCAPE           = 10;

  // variables that change for different operating systems, default: windows
  static int    STRUM                  = 16;


  public CarouselController(CarouselModel model){
    this.model = model;
  }

  /**
   * Poll forever, calling different model methods depending on the buttons pressed
   */
  private void pollForever(Controller ctrl) {
    Component[] allCmps    = ctrl.getComponents();
    float[]     vals       = new float[BUTTONS];
    Component[] activeCmps = {allCmps[ZERO_POWER], allCmps[ESCAPE], allCmps[STRUM]};

    while (true) {
      if (ctrl.poll()) {
        for ( int i = 0; i < BUTTONS; i++ ) {
          vals[i] = activeCmps[i].getPollData();
        }

        for ( int i = 0; i < BUTTONS; i++ ) {
          float val = vals[i];

          switch(i){
            // zero-power button
            case 0 :
              if (val == BUTTON_THRESHOLD){
                model.select();
                System.out.println("Zero power pressed");
              }
              break;


            // escape button
            case 1 :
              if (val >= BUTTON_THRESHOLD){
                // stay in slash mode
                System.out.println("Escape button pressed");
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

      try {
        Thread.sleep(DELAY);
      } catch (Exception exn) {
        System.out.println(exn); System.exit(1);
      }
    }
  }

  /**
   * Recognises the OS and sets the constants. Finds GH controller and polls it forever.
   * If none found, error is printed and program terminates.
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

    System.out.println(GUITAR_HERO + " controller not found");
    System.exit(1);
  }

}
