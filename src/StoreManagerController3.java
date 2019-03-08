import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Controller.
 *
 * @author  Pierrik Mellab
 * @version 2.00, January 2019.
 */
class StoreManagerController3 implements ActionListener {
  private StoreManagerModel storeManagerModel;
  private StoreManagerView storeManagerView;

  public StoreManagerController3 ( StoreManagerModel storeManagerModel ) {
    this.storeManagerModel = storeManagerModel;
    this.storeManagerView = storeManagerView;
  }

  public void actionPerformed( ActionEvent ev ) {
    File file = StoreManagerModel.fileFinder();
    String filePath = file.getPath();

    storeManagerModel.setMusicFile(file);

    storeManagerView.setMusicTitle(filePath);

  }
}
