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

    public void run() {
        try {
            final InputStream  in  = sck.getInputStream();
            final DataInputStream  reader  = new DataInputStream( in );
            String fileName = reader.readUTF();
            String cd = System.getProperty("user.dir");
            String dir = cd + "\\server_files\\";
            String fileDir = dir + fileName;

            if (Files.notExists(Paths.get(dir))){
                Files.createDirectories(Paths.get(dir));
            }

            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream( fileDir ));

            byte[] bytes = reader.readAllBytes();
            fileOut.write(bytes);

            fileOut.close();
            sck.close();

        } catch ( Exception exn ) {
            System.out.println( exn ); System.exit( 1 );
        }
    }
}
