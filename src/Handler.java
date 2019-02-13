import java.io.*;
import java.net.Socket;
/*
 * Handler.
 *
 * @author  John Mercer
 * @version 1.00, January 2019.
 */
public class Handler implements Runnable {
    private Socket sck;

    Handler( Socket sck ) {
        this.sck = sck;
    }

    public void run() {
        try {
            final InputStream  in  = sck.getInputStream();
            final OutputStream out = sck.getOutputStream();

            final BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
            // final PrintWriter    writer = new PrintWriter( out );

            // Making a local folder in current directory to store the zip files, and creating stream to local file
            String cd = System.getProperty("user.dir");
            String dir = cd + "\\server_files";
            String zip = dir + "file.zip";                      // zip name on server

            new File(dir).mkdirs();         // making the local folder if it doesnt already exist
            FileOutputStream fileOut = new FileOutputStream(zip);

            // Reading bytes from client and writing to local file
            int ch;
            while ( ( ch = reader.read() ) != -1 ) {
                fileOut.write( ch );
            }
            fileOut.close();
            sck.close();

        } catch ( Exception exn ) {
            System.out.println( exn ); System.exit( 1 );
        }
    }
}
