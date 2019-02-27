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
 * PlayModeController.
 *
 * @author  John Mercer
 * @author  Kamila Hoffmann-Derlacka
 * @version 1.00, February 2019.
 *
 *   Linux:
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
public class PlayModeController {
  final static String GUITAR_HERO = "Guitar Hero";
  final static int    DELAY       = 150;

  private  PlayModeModel model;

  private ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
  private Controller[]          ctrls = cenv.getControllers();

  public PlayModeController(PlayModeModel model){
    this.model = model;
  }

  /*
   * Poll forever, and altering model depending on buttons pressed
   */
  private void pollForever( Controller ctrl ) {
    Component[] allCmps    = ctrl.getComponents();
    float[]     vals       = new float[3];
    /*Component[] activeCmps = {allCmps[8], allCmps[10], allCmps[16]};

    while (true) {
      if (ctrl.poll()) {
        for ( int i = 0; i < 3; i++ ) {
          vals[i] = activeCmps[i].getPollData();
        }

        for ( int i = 0; i < 3; i++ ) {
          float val = vals[i];

          switch(i){
            // zero-power button
            case 0 :
              if (val == 1.0){
                model.select();
                System.out.println("Zero power pressed");
              }
              break;


            // escape button
            case 1 :
              if (val >= 1.0){
                model.select();
                System.out.println("Escape button pressed");
              }
              break;

            // strum
            case 2 :
              if (val >= 0.75){
                model.right();
                System.out.println("Strum right");
              } else if (val <= -0.75){
                model.left();
                System.out.println("Strum left");
              }
              /*
              try {
                Thread.sleep(DELAY);       // sleeping so strum is less sensitive
              } catch(Exception exn) {System.out.println(exn); System.exit(1);}
              *
              break;

          }
        }
      }

      try {
        Thread.sleep(DELAY);
      } catch (Exception exn) {
        System.out.println(exn); System.exit(1);
      }
    }*/
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
