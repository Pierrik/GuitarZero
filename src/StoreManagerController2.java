import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/*
 * Controller.
 *
 * @author  Pierrik Mellab
 * @version 2.00, January 2019.
 */
class StoreManagerController2 implements ActionListener {
  private StoreManagerModel storeManagerModel;
  private StoreManagerView storeManagerView;

  public StoreManagerController2 ( StoreManagerModel storeManagerModel ) {
    this.storeManagerModel = storeManagerModel;
    this.storeManagerView = storeManagerView;
  }

  public void actionPerformed( ActionEvent ev ) {
    File file = StoreManagerModel.fileFinder();
    String filePath = file.getPath();

    storeManagerModel.setCoverArtFile(file);

    storeManagerView.setCoverArtTitle(filePath);

  }
}
