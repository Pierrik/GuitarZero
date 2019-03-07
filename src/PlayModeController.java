import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
/**
 * PlayModeController.
 *
 * @author  Kamila Hoffmann-Derlacka
 * @version 1.5, February 2019.
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
  final static int    DELAY       = 150;

  private  PlayModeModel model;
  // make them non static when not using main anymore
  private static ControllerEnvironment cenv  = ControllerEnvironment.getDefaultEnvironment();
  private static Controller[]          ctrls = cenv.getControllers();

  final static double BUTTON_THRESHOLD = 1.0;

  // Buttons
  final static int    WHITE1       = 0;
  final static int    BLACK1       = 1;
  final static int    WHITE2       = 4;
  final static int    BLACK2       = 2;
  final static int    WHITE3       = 5;
  final static int    BLACK3       = 3;
  final static int    ZERO_POWER   = 8;
  final static int    ESCAPE       = 10;
  final static int    BENDER_CLICK = 12;

  // Variables that change for different operating systems, default: windows
  static int          BENDER_ROUND = 13;
  static int          WHAMMY       = 14;

  public PlayModeController(PlayModeModel model){
    this.model = model;
  }

  /*
   * Poll forever, and altering model depending on buttons pressed
   */
  public static void pollForever(Controller ctrl) {
    Component[] allCmps    = ctrl.getComponents();
    float[]     vals       = new float[allCmps.length];
    int         previous1;
    int         previous2;
    int         previous3;

    while (true) {
      if (ctrl.poll()) {
        for ( int i = 0; i < allCmps.length; i++ ) {
          vals[i] = allCmps[i].getPollData();
        }
        previous1 = -1;
        previous2 = -1;
        previous3 = -1;

        for ( int i = 0; i < allCmps.length; i++ ) {
          float val = vals[i];

            if (i == WHITE1) {
              if (val == BUTTON_THRESHOLD){
                previous1 = i;
              }
            } else if ( i == BLACK1) {
              if (val == BUTTON_THRESHOLD){
                previous1 = i;
              }
            } else if (i == BLACK2) {
              if (val == BUTTON_THRESHOLD){
                previous2 = i;
              }
            } else if (i == BLACK3) {
              if (val == BUTTON_THRESHOLD){
                previous3 = i;
              }
            } else if (i == WHITE2) {
              if (val == BUTTON_THRESHOLD){
                previous2 = i;
              }
            } else if (i == WHITE3) {
              if (val == BUTTON_THRESHOLD){
                previous3 = i;
              }
            } else if (i == ZERO_POWER) {
              if (val == BUTTON_THRESHOLD) {
                //model.select();
              }
            } else if (i == ESCAPE) { // escape button
              if (val == BUTTON_THRESHOLD) {
                System.out.println("escape");
              }
            } else if (i == WHAMMY) { //whammy  16 in linux
              if (val >=BUTTON_THRESHOLD) {
                //action
              }
            } else if (i == BENDER_CLICK) { // bender click
              if (val > 0) {
                // action
              }
            } else if (i == BENDER_ROUND) { //bender round linux 17
              if (val > 0){
                if (previous1 == WHITE1) {
                  if (previous2 == WHITE2) {
                    if (previous3 == WHITE3) {
                      System.out.println("white one + white two + white three + strum");
                      //model.checkNote("222");
                    } else if (previous3 == BLACK3) {
                      System.out.println("white one + white two + black three + strum");
                      //model.checkNote("221");
                    } else {
                      System.out.println("white one + white two + strum");
                      //model.checkNote("220");
                    }
                  }
                  else if (previous2 == BLACK2) {
                    if (previous3 == WHITE3) {
                      System.out.println("white one + black two + white three + strum");
                      //model.checkNote("212");
                    } else if (previous3 == BLACK3) {
                      System.out.println("white one + black two + black three + strum");
                      //model.checkNote("211");
                    } else {
                      System.out.println("white one + black two + strum");
                      //model.checkNote("210");
                    }
                  }
                  else {
                    if (previous3 == WHITE3) {
                      System.out.println("white one + white three + strum");
                      //model.checkNote("202");
                    }
                    else if (previous3 == BLACK3) {
                      System.out.println("white one + black three + strum");
                      //model.checkNote("201");
                    }
                    else {
                      System.out.println("white one + strum");
                      //model.checkNote("200");
                    }
                  }
                }
                else if (previous1 == BLACK1) {
                  if (previous2 == WHITE2) {
                    if (previous3 == WHITE3) {
                      System.out.println("black one + white two + white three + strum");
                      //model.checkNote("122");
                    } else if (previous3 == BLACK3) {
                      System.out.println("black one + white two + black three + strum");
                      //model.checkNote("121");
                    } else {
                      System.out.println("black one + white two + strum");
                      //model.checkNote("120");
                    }
                  } else if (previous2 == BLACK2) {
                    if (previous3 == WHITE3) {
                      System.out.println("black one + black two + white three + strum");
                      //model.checkNote("112");
                    } else if (previous3 == BLACK3) {
                      System.out.println("black one + black two + black three + strum");
                      //model.checkNote("111");
                    } else {
                      System.out.println("black one + black two + strum");
                      //model.checkNote("110");
                    }
                  } else {
                    if (previous3 == WHITE3) {
                      System.out.println("black one + white three + strum");
                      //model.checkNote("120");
                    } else if (previous3 == BLACK3) {
                      System.out.println("black one + black three + strum");
                      //model.checkNote("101");
                    } else {
                      System.out.println("black one + strum");
                      //model.checkNote("100");
                    }
                  }
                }
                else {
                  if (previous2 == WHITE2) {
                    if (previous3 == WHITE3) {
                      System.out.println("white two + white three + strum");
                      //model.checkNote("022");
                    } else if (previous3 == BLACK3) {
                      System.out.println("white two + black three + strum");
                      //model.checkNote("021");
                    } else {
                      System.out.println("white two + strum");
                      //model.checkNote("020");
                    }
                  }
                  else if (previous2 == BLACK2) {
                    if (previous3 == WHITE3) {
                      System.out.println("black two + white three + strum");
                      //model.checkNote("012");
                    } else if (previous3 == BLACK3) {
                      System.out.println("black two + black three + strum");
                      //model.checkNote("011");
                    } else {
                      System.out.println("black two + strum");
                      //model.checkNote("010");
                    }
                  }
                  else {
                    if (previous3 == WHITE3) {
                      System.out.println("white three + strum");
                      //model.checkNote("002");
                    }
                    else if (previous3 == BLACK3) {
                      System.out.println("black three + strum");
                      //model.checkNote("001");
                    }
                    else {
                      System.out.println("nothing + strum");
                      //model.checkNote("000");
                    }
                  }
                }
              }
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

  // Main commented out as pollForever can't be referenced from static

  public static void main(String[] args) {

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

    System.out.println( GUITAR_HERO + " controller not found" );
    System.exit( 1 );
  }
/*
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

    System.out.println( GUITAR_HERO + " controller not found" );
    System.exit( 1 );
  }*/

  @Override
  public void run() {
    //this.pollGuitarForever();
  }
}
