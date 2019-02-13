import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
/*
 * MockClient.
 *
 * @author  John Mercer
 * @version 1.00, February 2019.
 */
public class MockClient {
    final static String HOST = "localhost";
    final static int    PORT = 8888;

    public static void main( String[] argv ) {
        try {
            Socket sck = new Socket( HOST, PORT );

            // InputStream  in  = sck.getInputStream();
            OutputStream out = sck.getOutputStream();

            // BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
            // PrintWriter writer = new PrintWriter( out );

            // Writing a specified zip file to output stream (sending to the server)
            String filePath = "C:\\Users\\John\\Desktop\\GuitarZero\\src\\zip.zip";      // your zip here!
            File file = new File(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            out.write(fileContent);
            sck.close();

        } catch ( Exception exn ) {
            System.out.println( exn ); System.exit( 1 );
        }
    }
}
