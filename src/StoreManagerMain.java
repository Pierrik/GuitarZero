/**
 * StoreManagerMain.
 *
 * @author  Pierrik Mellab
 * @version 1.0, February 2019.
 */
public class StoreManagerMain {

  public static void main( String[] argv ) {

    StoreManagerModel       storeManagerModel       = new StoreManagerModel();
    StoreManagerController titleButton = new StoreManagerController ( storeManagerModel, StoreManagerButton.TITLE );
    StoreManagerController coverArtButton = new StoreManagerController ( storeManagerModel, StoreManagerButton.COVER );
    StoreManagerController songButton = new StoreManagerController ( storeManagerModel, StoreManagerButton.SONG );
    StoreManagerController saveButton = new StoreManagerController ( storeManagerModel, StoreManagerButton.SAVE );

    StoreManagerView        storeManagerView        = new StoreManagerView( titleButton, coverArtButton, songButton, saveButton);

    storeManagerView.setVisible(true);
  }

}
