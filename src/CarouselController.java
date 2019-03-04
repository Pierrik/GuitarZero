import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
/**
 * CarouselController.
 *
 * @author  John Mercer
 * @author  Kamila Hoffmann-Derlacka
 * @version 1.2, March 2019.
 *
 *   Linux:
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

  final static int    BUTTONS          = 3;
  final static int    ZERO_POWER       = 8;
  final static int    ESCAPE           = 10;
  final static int    STRUM            = 16;

  final static double BUTTON_THRESHOLD = 1.0;
  final static double STRUM_THRESHOLD  = 0.75;


  private CarouselModel         model;
  private ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
  private Controller[]          ctrls = cenv.getControllers();

  public CarouselController(CarouselModel model){
    this.model = model;
  }

  /*
   * Poll forever, and altering model depending on buttons pressed
   */
  private void pollForever( Controller ctrl ) {
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
                model.select();
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
              /*
              try {
                Thread.sleep(DELAY);       // sleeping so strum is less sensitive
              } catch(Exception exn) {System.out.println(exn); System.exit(1);}
              */
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

  /*
   * Finds GH controller and polls it forever. If none found, error is printed and program
   * terminates.
   */
  public void pollGuitarForever(){
    for (Controller ctrl : ctrls) {
      if (ctrl.getName().contains(GUITAR_HERO)) {
        pollForever(ctrl);
      }
    }

    System.out.println(GUITAR_HERO + " controller not found");
    System.exit(1);
  }

}
