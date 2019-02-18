import java.io.*;
import java.net.Socket;
/*
 * MockClient.
 *
 * @author  John Mercer
 * @version 1.00, February 2019.
 */
public class MockClient {
    String host;
    int    port;
    String filePath;

    MockClient(String host, int port, String filePath){
        this.host = host;
        this.port = port;
        this.filePath = filePath;
    }

    public void sendFile(){
        try {
            Socket sck = new Socket( this.host, this.port );

            File file = new File(this.filePath);
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
