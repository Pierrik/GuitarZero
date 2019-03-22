/**
 * StoreManagerMain.
 *
 * @author  Pierrik Mellab
 * @author Harper Ford
 * @version 2.0, March 2019.
 */
public class StoreManagerMain {

  public static void main( String[] argv ) {
    StoreManagerModel storeManagerModel = null;
    StoreManagerController saveButton = null;
    StoreManagerController songButton = null;
    StoreManagerController coverArtButton = null;
    StoreManagerController titleButton = null;
    StoreManagerView storeManagerView = null;

    try {
      storeManagerModel = new StoreManagerModel();
    }
    catch(Exception e){
      System.out.println("Unable to initialise Model. /STOREMANAGERMAIN");
    }

    try {
      titleButton = new StoreManagerController(storeManagerModel, StoreManagerButton.TITLE);
      coverArtButton = new StoreManagerController(storeManagerModel, StoreManagerButton.COVER);
      songButton = new StoreManagerController(storeManagerModel, StoreManagerButton.SONG);
      saveButton = new StoreManagerController(storeManagerModel, StoreManagerButton.SAVE);
    }
    catch(Exception e){
      System.out.println("Unable to initialise Buttons. /STOREMANAGERMAIN");
    }

    try{
      storeManagerView = new StoreManagerView( titleButton, coverArtButton, songButton, saveButton);
    }
    catch(Exception e){
      System.out.println("Unable to initialise View. /STOREMANAGERMAIN");
    }

    storeManagerView.setVisible(true);
  }

}
