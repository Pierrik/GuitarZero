import java.io.File;
import javax.swing.*;
import java.awt.*;

import java.lang.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Select Mode.
 *
 * @author Pierrik Mellab
 * @version 1.1, March 2019.
 *
 *   Windows:
 *   set CLASSPATH=jinput-2.0.9.jar;.
 *   javac SelectMode.java
 *   java -Djava.library.path=. SelectMode
 *
 *   Mac/ Linux:
 *   $ (cd src)
 *   $ CLASSPATH=jinput-2.0.9.jar:.
 *   $ export CLASSPATH
 *   $ javac SelectMode.java
 *   $ java -Djava.library.path=. SelectMode
 */
public class SelectMode extends JPanel {


  /**
   * Initialises the GUI classes for a carousel with menu options specific to Select Mode
   */
  public SelectMode() throws NoSongsException  {

    ArrayList<JLabel> menuOptions = new ArrayList<>();

    String cd = System.getProperty("user.dir");
    String bundleDirPath = "../local_store/bundle_files/";

    File[] song_folders = new File(bundleDirPath).listFiles();

    if (song_folders == null || song_folders.length == 0){
      System.out.println("No songs in local store.");
      throw new NoSongsException("No songs in local store");
    }

    for (File song_folder : song_folders) {

      File albumCover = new File("");
      String songName;

      if (!song_folder.getName().equals(".DS_Store")) {

        songName = song_folder.getName();

        File[] song_files = song_folder.listFiles();

        for (File song_file : song_files) {

          if (getExtension(song_file.getName()).equals("png") || getExtension(song_file.getName()).equals("PNG")) {
            albumCover = song_file;
          }


        }

        JLabel label = new JLabel(new ImageIcon(albumCover.getPath()));
        label.setText(songName);

        menuOptions.add(label);
      }
    }

    // Initialise the model, controller, view GUI classes
    CarouselView       view       = new CarouselView( menuOptions );
    CarouselModel      model      = new CarouselModel( view );
    CarouselController controller = new CarouselController( model , Mode.SELECT);
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