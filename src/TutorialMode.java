import java.awt.Image;
import java.io.File;
import javax.swing.*;
import java.lang.*;
import java.util.ArrayList;


/**
 * Tutorial Mode
 *
 * @author Pierrik Mellab
 * @version 1.1, March 2019.
 *
 */
public class TutorialMode extends JPanel {


  /**
   * Initialises the GUI classes for a picture viewer with picture options specific to Tutorial Mode
   */
  public TutorialMode() throws NoPictureException  {

    ArrayList<ImageIcon> pictureIcons = new ArrayList<>();

    String bundleDirPath = "../assets/tutorialMode/";

    File[] pictureFiles = new File(bundleDirPath).listFiles();

    if (pictureFiles == null || pictureFiles.length == 0){
      System.out.println("No tutorial pictures available.");
      throw new NoPictureException ("No tutorial pictures available.");
    }

    for (File pictureFile : pictureFiles) {

      if (getExtension(pictureFile.getName()).equalsIgnoreCase("png")) {
        pictureIcons.add(new ImageIcon(pictureFile.getPath()));
        System.out.println(pictureFile.getPath());
      }
    }

    System.out.println(pictureIcons.size());

    // Initialise the model, controller, view GUI classes
    TutorialModeView       view       = new TutorialModeView( pictureIcons );
    TutorialModeModel      model      = new TutorialModeModel( view );
    TutorialModeController controller = new TutorialModeController( model , Mode.TUTORIAL );

    try {
      Thread.sleep(200);
    } catch (InterruptedException e){
      e.printStackTrace();
    }
    Thread controllerThread = new Thread(controller);
    controllerThread.start();
    this.add(view);
  }

  private String getExtension (String fileName) {

    String extension = "";

    int i = fileName.lastIndexOf('.');

    if (i > 0) {
      extension = fileName.substring(i + 1);
    }

    return extension;
  }
}