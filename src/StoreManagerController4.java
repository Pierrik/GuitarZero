import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Controller.
 *
 * @author  Pierrik Mellab
 * @version 2.00, January 2019.
 */
class StoreManagerController4 implements ActionListener {
  private StoreManagerModel storeManagerModel;
  private StoreManagerView storeManagerView;

  public StoreManagerController4 ( StoreManagerModel storeManagerModel ) {
    this.storeManagerModel = storeManagerModel;
    this.storeManagerView = storeManagerView;
  }

  public void actionPerformed( ActionEvent ev ) {
      storeManagerModel.saveAction();

  }
}
