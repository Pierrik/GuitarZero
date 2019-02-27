import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
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

      switch (method) {
        case "UPLOAD_BUNDLE":
          String bundlePath = cd + "\\bundle_files\\" + fileName;
          BufferedOutputStream bundleOut = new BufferedOutputStream(new FileOutputStream(bundlePath));

          byte[] bundleBytes = dataIn.readAllBytes();
          bundleOut.write(bundleBytes);

          bundleOut.close();
          sck.close();
          break;

        case "UPLOAD_PREVIEW":
          String previewPath = cd + "\\preview_files\\" + fileName;
          BufferedOutputStream previewOut = new BufferedOutputStream(new FileOutputStream(previewPath));

          byte[] previewBytes = dataIn.readAllBytes();
          previewOut.write(previewBytes);

          previewOut.close();
          sck.close();
          break;

        case "DOWNLOAD_BUNDLE":
          String newBundle = cd + "\\bundle_files\\" + fileName;

          File bundle = new File(newBundle);
          byte[] bundleDownloadBytes = new byte[(int) bundle.length()];

          DataInputStream bundleIn = new DataInputStream(new FileInputStream(bundle));

          bundleIn.readFully(bundleDownloadBytes, 0, bundleDownloadBytes.length);

          dataOut.write(bundleDownloadBytes, 0, bundleDownloadBytes.length);
          dataOut.flush();

          bundleIn.close();
          dataOut.close();
          sck.close();
          break;

        case "DOWNLOAD_PREVIEW":
          String newPreview = cd + "\\preview_files\\" + fileName;

          File preview = new File(newPreview);
          byte[] previewDownloadBytes = new byte[(int) preview.length()];

          DataInputStream previewIn = new DataInputStream(new FileInputStream(preview));

          previewIn.readFully(previewDownloadBytes, 0, previewDownloadBytes.length);

          dataOut.write(previewDownloadBytes, 0, previewDownloadBytes.length);
          dataOut.flush();

          previewIn.close();
          dataOut.close();
          sck.close();
          break;

        default:
          System.out.println("Invalid request");
          break;
      }
    }
    catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }
}
