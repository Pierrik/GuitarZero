import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import static org.junit.Assert.*;

import org.junit.Test;

public class MidiToNotesTest {

  @Test
  public void formatNoteFormat0() {
    // Note 0 should be converted to "100";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,0,m);
    Long key = new Long(1);
    assertEquals("100", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void formatNoteFormat1() {
    // Note 1 should be converted to "200";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,0,m);
    Long key = new Long(1);
    assertEquals("100", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());

  }

  @Test
  public void compare() {
  }

  @Test
  public void displayTrack() {
  }

  @Test
  public void displaySequence() {
  }

  @Test
  public void main() {
  }
}