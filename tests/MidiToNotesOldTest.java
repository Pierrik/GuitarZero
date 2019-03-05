import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MidiToNotesOld Test
 * Junit 4.12
 * @author Tom Mansfield
 */

public class MidiToNotesOldTest {

  @Test
  public void testFormatNote0() {
    // Note 0 should be converted to "100";
    Map<Long, String> m = new HashMap<>();
    MidiToNotesOld.formatNote(1,0,m);
    Long key = new Long(1);
    assertEquals("100", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote1() {
    // Note 1 should be converted to "200";
    Map<Long, String> m = new HashMap<>();
    MidiToNotesOld.formatNote(1,1,m);
    Long key = new Long(1);
    assertEquals("200", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote2() {
    // Note 1 should be converted to "010";
    Map<Long, String> m = new HashMap<>();
    MidiToNotesOld.formatNote(1,2,m);
    Long key = new Long(1);
    assertEquals("010", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote3() {
    // Note 1 should be converted to "020";
    Map<Long, String> m = new HashMap<>();
    MidiToNotesOld.formatNote(1,3,m);
    Long key = new Long(1);
    assertEquals("020", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote4() {
    // Note 1 should be converted to "001";
    Map<Long, String> m = new HashMap<>();
    MidiToNotesOld.formatNote(1,4,m);
    Long key = new Long(1);
    assertEquals("001", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNote5() {
    // Note 1 should be converted to "002";
    Map<Long, String> m = new HashMap<>();
    MidiToNotesOld.formatNote(1,5,m);
    Long key = new Long(1);

    assertEquals("002", m.get(key));

    // Size of map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testFormatNoteCombineSameLane(){
    Map<Long, String> m = new HashMap<>();

    // Should be formatted as "100"
    MidiToNotesOld.formatNote(1,0,m);

    // Should be formatted as "200"
    MidiToNotesOld.formatNote(1,1,m);

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
    MidiToNotesOld.formatNote(1,0,m);

    // Should be formatted as "010"
    MidiToNotesOld.formatNote(1,2,m);

    Long key = new Long(1);

    // 2 buttons should be pressed at the same time
    assertEquals("110", m.get(key));

    // Size of the map should be equal to 1
    assertEquals(1, m.size());
  }

  @Test
  public void testCompare1() {
    // Should return the note "212";
    assertEquals("212", MidiToNotesOld.compare("210","012") );
  }

  @Test
  public void testCompare2() {
    // Should return "000"
    assertEquals("000", MidiToNotesOld.compare("000","000") );
  }

  @Test
  public void testCompareInvalid1() {
    // Should give exception
    try {
      MidiToNotesOld.compare("abc", "def");
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  @Test
  public void testCompareInvalid2() {
    // Should give exception
    try {
      MidiToNotesOld.compare("", "");
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  @Test
  public void testGetTracks() {
    // Exception should not be thrown
    try {
      MidiToNotesOld.findTracks(
          MidiSystem.getSequence(
              new File("assets\\AC_DC_-_Back_In_Black.mid")
          )
      );
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetTracksInvalid(){
    try {
      MidiToNotesOld.findTracks(MidiSystem.getSequence(new File("invalid file")));
      fail();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
      assertTrue(true);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(true);
    }
  }

  @Test
  public void testWriteTrackInvalid() {
    // Should throw exception
    try {
      MidiToNotesOld.writeTrack(MidiSystem.getSequence(new File("invalid file")).getTracks()[0]);
      fail();
    } catch (InvalidMidiDataException e) {
      assertTrue(true);
    } catch (IOException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testWriteTrack(){
    // Should run successfully
    try {
      MidiToNotesOld.writeTrack(MidiSystem.getSequence(new File("assets\\AC_DC_-_Back_In_Black.mid")).getTracks()[0]);
    } catch (InvalidMidiDataException e) {
      fail();
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void testWriteFileInvalid(){
    // Exception should be handled
    try {
      MidiToNotesOld.writeFile("invalid file");
    } catch (Exception e) {
      fail();
    }
  }

}