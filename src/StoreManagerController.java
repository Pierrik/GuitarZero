import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/*
 * Controller.
 *
 * @author  David Wakeling
 * @version 2.00, January 2019.
 */
class StoreManagerController implements ActionListener {
  private StoreManagerModel storeManagerModel;
  private StoreManagerView storeManagerView;

  public StoreManagerController( StoreManagerModel storeManagerModel ) {
    this.storeManagerModel = storeManagerModel;
    this.storeManagerView = storeManagerView;
  }

  public void actionPerformed( ActionEvent ev ) {
    File file = StoreManagerModel.fileFinder();
    String filePath = file.getPath();



    //musicField.setText(filePath);
    //musicFile = file;

    //System.out.println(ev.getSource());

    //System.out.println(ev.getSource().toString());

    System.out.println("tester");

  }
}
