import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * StoreMode.
 * @author John Mercer
 * @version 1.0, March 2019
 */
public class StoreMode {
  // server settings
  private static final String HOST = "localhost";
  private static final int    PORT = 8888;

  /**
   * Uploads a file to the server
   * @param filePath: Location of the file to upload
   * @param method: Upload method (UPLOAD_BUNDLE or UPLOAD_PREVIEW)
   */
  /*
  public static void sendFileToServer(String filePath, String method){
    MockClient client = new MockClient(HOST, PORT);
    client.uploadFile(filePath, method);
  }
  */

  public static void main(String args[]) {

    /*MockClient client = new MockClient(HOST, PORT);
    Object fileNames = client.listDirectory();
    System.out.println(fileNames.toString());


    for (String string:fileNames){
      System.out.println(string);
    }*/



    ArrayList<JLabel> menuOptions = new ArrayList<>();

    // Create all menu option labels with their image icon and title
    JLabel label1 = new JLabel(new ImageIcon("../assets/ExitLogo2.png"));
    JLabel label2 = new JLabel(new ImageIcon("../assets/StoreLogo2.png"));
    JLabel label3 = new JLabel(new ImageIcon("../assets/SelectLogo2.png"));
    JLabel label4 = new JLabel(new ImageIcon("../assets/PlayLogo2.png"));
    JLabel label5 = new JLabel(new ImageIcon("../assets/TutorialLogo2.png"));

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
    StoreModeModel      model      = new StoreModeModel();
    StoreModeController controller = new StoreModeController( model );
    StoreModeView       view       = new StoreModeView(model, menuOptions);
    view.setVisible( true );
    controller.pollGuitarForever();

  }
}
