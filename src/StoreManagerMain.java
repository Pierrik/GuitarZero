/*
 * StoreManagerMain.
 *
 * @author  Pierrik Mellab
 * @version 1.0, January 2019.
 */
public class StoreManagerMain {

  public static void main( String[] argv ) {

    StoreManagerModel       storeManagerModel       = new StoreManagerModel();
    StoreManagerController1 storeManagerController1 = new StoreManagerController1 ( storeManagerModel );
    StoreManagerController2 storeManagerController2 = new StoreManagerController2 ( storeManagerModel );
    StoreManagerController3 storeManagerController3 = new StoreManagerController3 ( storeManagerModel ) ;
    StoreManagerController4 storeManagerController4 = new StoreManagerController4 ( storeManagerModel );
    StoreManagerView        storeManagerView        = new StoreManagerView( storeManagerController1, storeManagerController2, storeManagerController3, storeManagerController4 );

    storeManagerView.setVisible(true);
  }

}
