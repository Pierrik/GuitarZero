import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Server.
 *
 * @author  John Mercer
 * @version 1.04, March 2019.
 */
public class Server {
  private static final int     PORT    = 8888;
  private static final boolean VERBOSE = true;

  public static void main(String[] argv) {
    try {
      // Checking if directories exist on server-side before starting
      String cd = System.getProperty("user.dir");
      String bundleDir = cd + "/bundle_files/";
      String previewDir = cd + "/preview_files/";

      if (Files.notExists(Paths.get(bundleDir))) {
        Files.createDirectories(Paths.get(bundleDir));
      }
      if (Files.notExists(Paths.get(previewDir))) {
        Files.createDirectories(Paths.get(previewDir));
      }


      // Starting the server
      final ServerSocket ssck = new ServerSocket(PORT);
      if (VERBOSE) {
        System.out.println("Server started.\nListening for connections on port : " +
            PORT + " ...\n");
      }

      // Listening until user halts execution of server
      while (true) {
        // Waiting for a client to connect
        final Socket sck = ssck.accept();
        if (VERBOSE) {System.out.println("Connection opened. (" + new Date() + ")");}
        try {
          // Create dedicated thread to handle client connection
          Handler handler = new Handler(sck);
          Thread thread = new Thread(handler);
          thread.start();
        }
        catch(Exception e){
          System.out.println("Unable to start handler thread. /SERVER");
        }
      }

    } catch (Exception exn) {
      System.out.println(exn); System.exit( 1 );
    }
  }
}