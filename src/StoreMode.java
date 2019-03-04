import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * StoreMode.
 * @author John Mercer
 * @version 1.0, March 2019
 */
public class StoreMode {
  public static void readPreviewsFromServer(){}
  //public static void

  public static void main(String args[]) {
    ArrayList<JLabel> menuOptions = new ArrayList<>();

    // Create all menu option labels with their image icon and title
    JLabel label1 = new JLabel(new ImageIcon("..\\assets\\ExitLogo2.png"));
    JLabel label2 = new JLabel(new ImageIcon("..\\assets\\StoreLogo2.png"));
    JLabel label3 = new JLabel(new ImageIcon("..\\assets\\SelectLogo2.png"));
    JLabel label4 = new JLabel(new ImageIcon("..\\assets\\PlayLogo2.png"));
    JLabel label5 = new JLabel(new ImageIcon("..\\assets\\TutorialLogo2.png"));

    label1.setText("Exit");
    label2.setText("Store");
    label3.setText("Select");
    label4.setText("Play");
    label5.setText("Tutorial");


    // Add labels to arrayList
    menuOptions.add(label1);
    menuOptions.add(label2);
    menuOptions.add(label3);
    menuOptions.add(label4);
    menuOptions.add(label5);

    // Initialise the model, controller, view GUI classes
    CarouselModel      model      = new CarouselModel();
    CarouselController controller = new CarouselController( model );
    CarouselView       view       = new CarouselView( controller, model, menuOptions);
    view.setVisible( true );
    controller.pollGuitarForever();
  }
}
