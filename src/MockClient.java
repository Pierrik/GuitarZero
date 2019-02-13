import java.io.*;
import java.net.Socket;
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

            String filePath = "C:\\Users\\John\\Desktop\\GuitarZero\\src\\zip.zip";      // your zip here!
            File file = new File(filePath);
            String fileName = file.getName();
            byte[] bytes = new byte[(int) file.length()];

            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            DataOutputStream dataOut = new DataOutputStream(sck.getOutputStream());

            dataIn.readFully(bytes, 0, bytes.length);

            dataOut.writeUTF(fileName);
            dataOut.write(bytes, 0, bytes.length);
            dataOut.flush();

            dataOut.close();
            sck.close();

        } catch ( Exception exn ) {
            System.out.println( exn ); System.exit( 1 );
        }
    }
}
