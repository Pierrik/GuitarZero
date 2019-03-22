import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Controller.
 *
 * @author  Pierrik Mellab
 * @author  John Mercer
 * @version 2.00, January 2019.
 */
class StoreManagerController implements ActionListener {
  private StoreManagerModel storeManagerModel;
  private StoreManagerView  storeManagerView;
  private StoreManagerButton button;

  public StoreManagerController ( StoreManagerModel storeManagerModel, StoreManagerButton button ) {
    this.storeManagerModel = storeManagerModel;
    this.storeManagerView  = storeManagerView;
    this.button = button;
  }

  public void actionPerformed( ActionEvent ev ) {
    switch (this.button) {
      case TITLE:
        File titleFile = null;
        try {
          titleFile = StoreManagerModel.fileFinder("txt");
        }
        catch(Exception e){
          System.out.println("Null reference, StoreManagerModel. /STOREMANAGERCONTROLLER");
        }
        String titlePath = titleFile.getPath();
        storeManagerModel.setTitleFile(titleFile);
        storeManagerView.setTitleTitle(titlePath);
        break;

      case COVER:
        File coverFile = StoreManagerModel.fileFinder("png");
        String coverPath = coverFile.getPath();
        storeManagerModel.setCoverArtFile(coverFile);
        storeManagerView.setCoverArtTitle(coverPath);
        break;

      case SONG:
        File songFile = StoreManagerModel.fileFinder("mid");
        String songPath = songFile.getPath();
        storeManagerModel.setMusicFile(songFile);
        storeManagerView.setMusicTitle(songPath);
        break;

      case SAVE:
        storeManagerModel.saveAction();
        break;
    }

  }
}
