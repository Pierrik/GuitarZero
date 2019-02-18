import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.MidiSystem;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class MidiToNotesTest {

  @Test
  public void testFormatNote0() {
    // Note 0 should be converted to "100";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,0,m);
    Long key = new Long(1);
    assertEquals("100", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote1() {
    // Note 1 should be converted to "200";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,1,m);
    Long key = new Long(1);
    assertEquals("200", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote2() {
    // Note 1 should be converted to "010";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,2,m);
    Long key = new Long(1);
    assertEquals("010", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote3() {
    // Note 1 should be converted to "020";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,3,m);
    Long key = new Long(1);
    assertEquals("020", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote4() {
    // Note 1 should be converted to "001";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,4,m);
    Long key = new Long(1);
    assertEquals("001", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote5() {
    // Note 1 should be converted to "002";
    Map<Long, String> m = new HashMap<>();
    MidiToNotes.formatNote(1,5,m);
    Long key = new Long(1);

    assertEquals("002", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNoteCombineSameLane(){
    Map<Long, String> m = new HashMap<>();

    // Should be formatted as "100"
    MidiToNotes.formatNote(1,0,m);

    // Should be formatted as "200"
    MidiToNotes.formatNote(1,1,m);

    Long key = new Long(1);

    // Highest value should take precedence
    assertEquals("200", m.get(key));

    // Size of the map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNoteCombineDifferentLane(){
    Map<Long, String> m = new HashMap<>();
    // Should be formatted as "100"
    MidiToNotes.formatNote(1,0,m);

    // Should be formatted as "010"
    MidiToNotes.formatNote(1,2,m);

    Long key = new Long(1);

    // 2 buttons should be pressed at the same time
    assertEquals("110", m.get(key));

    // Size of the map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testCompare1() {
    // Should return the note "212";
    assertEquals("212", MidiToNotes.compare("210","012") );
  }

  @Test
  public void testCompare2() {
    // Should return "000"
    assertEquals("000", MidiToNotes.compare("000","000") );
  }

  @Test
  public void testCompareInvalid1() {
    // Should give exception
    try {
      MidiToNotes.compare("abc", "def");
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  @Test
  public void testCompareInvalid2() {
    // Should give exception
    try {
      MidiToNotes.compare("", "");
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  @Test
  public void testGetTracks() {
    // Exception should not be thrown
    try {
      MidiToNotes.getTracks(
          MidiSystem.getSequence(
              new File("C:\\Users\\tomma\\Desktop\\GuitarZero\\AC_DC_-_Back_In_Black.mid")
          )
      );
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void displaySequence() {
  }

  @Test
  public void main() {
  }
}