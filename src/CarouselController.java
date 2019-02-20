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

  private static JButton[] buttons;

  /*
   * Make a frame of buttons for controller components.
   */
  private static JFrame makeFrame( Controller ctrl ) {
    JFrame frm = new JFrame();
    JPanel pan = new JPanel( new GridLayout( 0, 2 ) );

    Component[] cmps = ctrl.getComponents();
    buttons = new JButton[ cmps.length ];

    for ( int i = 0; i < buttons.length; i = i + 1 ) {
      JButton button = new JButton();
      button.setPreferredSize( new Dimension( 100, 40 ) );
      buttons[ i ] = button;
      pan.add( button );
    }

    frm.add( pan );
    frm.pack();

    return frm;
  }

  /*
   * Poll forever, and storing and displaying values of components lal
   */
  private static void pollForever( Controller ctrl ) {
    Component[] cmps = ctrl.getComponents();
    float[]     vals = new float[3];
    Component[] activeCmps = {cmps[10], cmps[12], cmps[16]};

    while (true) {
      if (ctrl.poll()) {
        for ( int i = 0; i < activeCmps.length; i++ ) { /* store */
          vals[i] = activeCmps[i].getPollData();
        }

        for ( int i = 0; i < activeCmps.length; i++ ) { /* display */
          float val = vals[i];

          //strum
          if (i == 2) {
            if (val == 1.0) {
              // action go right
            } else if (val == -1.0 ) {
              // action go left
            }
          }
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

          buttons[ i ].setText( "" + val );
          buttons[ i ].setOpaque( true );
          buttons[ i ].repaint();
        }
      }

      try { /* delay */
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
        JFrame frm = makeFrame( ctrl );
        frm.setVisible( true );
        pollForever( ctrl );
      }
    }

    System.out.println( GUITAR_HERO + " controller not found" );
    System.exit( 1 );
  }
}
