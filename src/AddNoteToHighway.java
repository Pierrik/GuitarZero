/**
 * AddNoteToHighway
 * Adds the current note to be played in the song to the view
 *
 * @author Tom Mansfield
 */
public class AddNoteToHighway implements Runnable {
  PlayModeModel model;
  PlayModeView view;

  private static final String EMPTY_NOTE = "000";

  public AddNoteToHighway(PlayModeModel model, PlayModeView view){
    this.model = model;
    this.view = view;
  }

  @Override
  public void run() {
    while(!model.isEndOfSong()){
      if(!(model.getCurrentNote().equals(EMPTY_NOTE)) && !model.getCurrentNote().equals("")){
        //view.addNote(model.getCurrentNote());
      }
    }
  }
}
