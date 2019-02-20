import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/*
 * Handler.
 *
 * @author  John Mercer
 * @version 1.00, February 2019.
 */
public class Handler implements Runnable {
  private Socket sck;

  Handler( Socket sck ) {
    this.sck = sck;
  }

  /**
   * Downloads and uploads files to clients
   */
  public void run() {
    try {
      final DataInputStream dataIn = new DataInputStream(sck.getInputStream());
      final DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

      String header = dataIn.readUTF();
      String[] headers = header.split("/");

      String method = headers[0].toUpperCase();
      String fileName = headers[1];

      String cd = System.getProperty("user.dir");
      String dir = cd + "\\server_files\\";
      String fileDir = dir + fileName;

      if (method.equals("UPLOAD")) {
        if (Files.notExists(Paths.get(dir))) {
          Files.createDirectories(Paths.get(dir));
        }

        BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(fileDir));

        byte[] bytes = dataIn.readAllBytes();
        fileOut.write(bytes);

        fileOut.close();
        sck.close();
      }

      else if (method.equals("DOWNLOAD")) {
        File file = new File(fileDir);
        byte[] bytes = new byte[(int) file.length()];

        DataInputStream fileIn = new DataInputStream(new FileInputStream(file));

        fileIn.readFully(bytes, 0, bytes.length);

        dataOut.write(bytes, 0, bytes.length);
        dataOut.flush();

        dataOut.close();
        sck.close();
      }

      else{
        System.out.println("Invalid request");
      }

    } catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }
}
