import javax.swing.JFrame;
/*
 * Tetris.
 *
 * @author  David Wakeling
 * @version 7.01a, January 2019.
 */
public class StoreManagerMain {

  public static void main( String[] argv ) {

    StoreManagerModel       storeManagerModel       = new StoreManagerModel();
    StoreManagerController storeManagerController = new StoreManagerController( storeManagerModel );
    StoreManagerView        storeManagerView        = new StoreManagerView( storeManagerController );

    storeManagerView.setVisible(true);
  }

}
