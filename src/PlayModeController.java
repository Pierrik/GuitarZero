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
  public void pollForever( Controller ctrl ) {
    Component[] allCmps    = ctrl.getComponents();
    float[]     vals       = new float[allCmps.length];
    int         previous;

    while (true) {
      if (ctrl.poll()) {
        for ( int i = 0; i < allCmps.length; i++ ) {
          vals[i] = allCmps[i].getPollData();
        }
        previous = -1;
        for ( int i = 0; i < allCmps.length; i++ ) {
          float val = vals[i];

          switch(i){
            // white one
            case 0 :
              if (val == 1.0){
                previous = i;
              }
              break;

            // black one
            case 1 :
              if (val == 1.0){
                previous = i;
              }
              break;

            // black two
            case 2 :
              if (val == 1.0){
                previous = i;
              }
              break;

            // black three
            case 3 :
              if (val == 1.0){
                previous = i;
              }
              break;

            // white two
            case 4 :
              if (val == 1.0){
                previous = i;
              }
              break;

            // white three
            case 5 :
              if (val == 1.0){
                previous = i;
              }
              break;

            // zero power button
            case 8 :
              if (val == 1.0){
                //model.select();
              }
              break;

            // escape button
            case 10 :
              if (val == 1.0){
                // action
              }
              break;

            // bender button click
            case 12 :
              if (val == 1.0){
                // action
              }
              break;

            // bender button playing around (values 1/8, 2/8, 3/8...)
            case 13 :
              if (val > 0){
                // action
              }
              break;

            // whammy bar (responsible for strumming)
            case 14 :
              if (val >= 0){
                if (previous == 0) {
                  System.out.println("white one + strum");
                  model.checkNote("w1");
                }
                else if (previous == 1) {
                  System.out.println("black one + strum");
                  model.checkNote("b1");
                }
                else if (previous == 2) {
                  System.out.println("black two + strum");
                  model.checkNote("b2");
                }
                else if (previous == 3) {
                  System.out.println("black three + strum");
                  model.checkNote("b3");
                }
                else if (previous == 4) {
                  System.out.println("white two + strum");
                  model.checkNote("w2");
                }
                else if (previous == 5) {
                  System.out.println("white three + strum");
                  model.checkNote("w3");
                }
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

  /*public static void main( String[] argv ) {
    ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
    Controller[]          ctrls = cenv.getControllers();

    for ( Controller ctrl : ctrls ) {
      if ( ctrl.getName().contains( GUITAR_HERO ) ) {
        pollForever( ctrl );
      }
    }

    System.out.println( GUITAR_HERO + " controller not found" );
    System.exit( 1 );
  }*/

}
