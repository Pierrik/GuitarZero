import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.java.games.input.Controller;
import org.junit.Assert;

/**
 * CarouselController Test
 * Junit 4.12
 * @author Kamila Hoffmann-Derlacka
 */

public class CarouselControllerTest {

  private CarouselController controller;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @org.junit.Before
  public void setUp() throws Exception {
    controller = new CarouselController(new CarouselModel());
    //controller.ctrls = new Controller[]{};
    System.setOut(new PrintStream(outContent));
  }

  @org.junit.After
  public void tearDown() throws Exception {
    // Reset the System.out
    System.setOut(originalOut);
  }

  @org.junit.Test
  public void CarouselControllerNotNull() {
    Assert.assertNotNull(controller);
  }

  @org.junit.Test
  public void GuitarIsNotFound() {
    // no guitar zero in ctrls
  }
}