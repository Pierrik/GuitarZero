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
/*
 * Plastic guitar test (Sony PS3).
 *
 * @author  David Wakeling
 * @version 1.00, January 2019.
 *
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac PlasticGuitar.java
 *   $ java -Djava.library.path=. PlasticGuitar
 *
 *   set CLASSPATH=jinput-2.0.9.jar;.
 *   javac PlasticGuitar.java
 *   java -Djava.library.path=. PlasticGuitar
 */
public class CarouselController {
  final static String GUITAR_HERO = "Guitar Hero"; /* Identifier       */
  final static int    DELAY       = 50;            /* 20th of a second */
  private CarouselModel model;


  /*
   * Poll forever, and storing and displaying values of components lal
   */
  private static void pollForever( Controller ctrl ) {
    Component[] allCmps    = ctrl.getComponents();
    float[]     vals       = new float[3];
    Component[] activeCmps = {allCmps[10], allCmps[12], allCmps[16]};

    while (true) {
      if (ctrl.poll()) {
        for ( int i = 0; i < 3; i++ ) {
          vals[i] = activeCmps[i].getPollData();
        }

        for ( int i = 0; i < 3; i++ ) {
          float val = vals[i];

          //escape
          if (i == 0) {
            if (val == 1){
              // escape button action here
            }
          }
          //bender
          if (i == 1){
            if (val == 1){
              // bender button action here
            }
          }
          //strum
          if (i == 2) {
            if (val == 1.0) {
              // action go right
            } else if (val == -1.0 ) {
              // action go left
            }
          }
        }
      }

      try {
        Thread.sleep( DELAY );
      } catch ( Exception exn ) {
        System.out.println( exn ); System.exit( 1 );
      }
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main( String[] args ) {
    ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
    Controller[]          ctrls = cenv.getControllers();

    for ( Controller ctrl : ctrls ) {
      if ( ctrl.getName().contains( GUITAR_HERO ) ) {
        pollForever( ctrl );
      }
    }

    System.out.println( GUITAR_HERO + " controller not found" );
    System.exit( 1 );
  }
}
