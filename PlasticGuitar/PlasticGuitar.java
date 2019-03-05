import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
 *   $ (CD src)
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac PlasicGuitar.java
 *   $ java -Djava.library.path=. PlasticGuitar
 */
public class PlasticGuitar {
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
   * Poll forever, and storing and displaying values of components.
   */ 
  private static void pollForever( Controller ctrl ) {
    Component[] cmps = ctrl.getComponents();
    float[]     vals = new float[  cmps.length ]; 

    while( true ) {
      if ( ctrl.poll() ) {
        for ( int i = 0; i < cmps.length; i = i + 1 ) { /* store */
          vals[ i ] = cmps[ i ].getPollData();
        }

        for ( int i = 0; i < cmps.length; i = i + 1 ) { /* display */
          float val = vals[ i ];
          Color col;
          if ( val == 0.0 ) {
	    col = Color.WHITE;
            val = i;
          } else if ( val == 1.0) {
            //down
            col = Color.CYAN;
          } else if (val == -1.0 ) {
	    col = Color.BLUE; //up
          } else {
            col = Color.YELLOW;
          }
          buttons[ i ].setBackground( col );
	  buttons[ i ].setText( "" + i );
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
  public static void main( String[] argv ) {
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
