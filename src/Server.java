import java.net.ServerSocket;
import java.net.Socket;
/*
 * Handler.
 *
 * @author  John Mercer
 * @version 1.00, February 2019.
 */
public class Server {
    public final static int PORT = 8888;

    public static void main( String[] argv ) {
        try {
            final ServerSocket ssck = new ServerSocket( PORT );
            int id = 0;

            while ( true ) {
                final Socket sck = ssck.accept();               // waits for a client to connect
                final Handler wkr = new Handler( sck );         // runs a handler for the client when it connects
                wkr.run();
                id += 1;
            }
        } catch ( Exception exn ) {
            System.out.println( exn ); System.exit( 1 );
        }
    }
}