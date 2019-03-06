import java.io.File;
import java.util.Scanner;

/**
 * Currency.
 *
 * @author  Kamila Hoffmann-Derlacka
 * @version 1.00, March 2019.
 */

public class Currency {
  public static int loadTotalCurrency() {
    File inputFile = new File("currency");
    int currency = 0;

    try {
      Scanner input = new Scanner(inputFile);
      // Iterates through each line of the file
      while (input.hasNextLine()) {
        currency = Integer.valueOf(input.nextLine());
        if (currency > 0)
        {
          return currency;
        }
        else {
          return 0;
        }
      }
    } catch (Exception e) {}
    return currency;
  }
}