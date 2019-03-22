import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.Scanner;

/**
 * Currency.
 *
 * @author  Kamila Hoffmann-Derlacka
 * @authro Tom Mansfield
 * @version 2.00, March 2019.
 */

public class Currency {

  private static final String  CURRENCY_PATH     = "../currency/currency.txt";

  /**
   * findCurrencyFile
   * Finds the file used to store the user's earned currency
   * @return the currency file for the game
   * @throws Exception
   */
  public static File findCurrencyFile() throws Exception {
    // Search the currency directory
    File bundle = new File("../currency");

    // Find text files in the currency folder
    File[] files = bundle.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".txt");
      }
    });

    if ( files.length > 0 ) {
      // Returns first occurrence of txt file, should only be one
      return files[0];

    } else {
      // If there is no currency file found, create one
      File currencyFile = new File(CURRENCY_PATH);
      currencyFile.createNewFile();

      // Set the score value in the file to 0
      FileWriter writer = new FileWriter(CURRENCY_PATH);
      writer.write(Integer.toString(0));
      writer.close();

      // Returns the created currency file
      return currencyFile;
    }
  }

  /**
   * loadCurrencyFile
   * Reads the currency file and updates the total currency to the value contained in the file
   * @throws Exception when the file cannot be read
   */
  public static int loadCurrencyFile(File inputFile) throws Exception {

    int currency;

    Scanner input = new Scanner(inputFile);
    // Iterates through each line of the file
    while (input.hasNextLine()) {
      currency = Integer.valueOf(input.nextLine());
      if (currency > 0)
      {
        return currency;
      }
    }
    //System.out.println("this is currency value: " + currency);
    return 0;
  }

  /**
   * saveCurrencyFile
   * Updates the currency file when the song has finished with any earned currency
   * @throws Exception if the currency file cannot be written
   */
  public static void saveCurrencyFile(int newCurrency) throws Exception {
    FileWriter writer = new FileWriter("../currency/currency.txt");
    writer.write(Integer.toString(newCurrency));
    writer.close();
  }
}