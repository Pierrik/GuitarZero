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

  @org.junit.Before
  public void setUp() throws Exception {
    controller = new CarouselController(new CarouselModel());
  }

  @org.junit.Test
  public void CarouselControllerNotNull() {
    Assert.assertNotNull(controller);
  }
}