import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
/*
 * Server.
 *
 * @author  John Mercer
 * @version 1.00, February 2019.
 */
public class Server {
  private static final int PORT = 8888;
  private static final boolean verbose = true;

  public static void main(String[] argv) {
    try {
      // starting the server
      final ServerSocket ssck = new ServerSocket(PORT);
      if (verbose) {
        System.out.println("Server started.\nListening for connections on port : " +
            PORT + " ...\n");
      }

      // listening until user halts execution of server
      while (true) {
        // waiting for a client to connect
        final Socket sck = ssck.accept();
        if (verbose) {System.out.println("Connection opened. (" + new Date() + ")");}

        // create dedicated thread to handle client connection
        Handler handler = new Handler(sck);
        Thread thread = new Thread(handler);
        thread.start();
      }

    } catch (Exception exn) {
      System.out.println(exn); System.exit( 1 );
    }
  }
}