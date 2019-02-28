import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handler.
 *
 * @author  John Mercer
 * @version 1.05, February 2019.
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

      String cd = System.getProperty("user.dir");

      String header = dataIn.readUTF();
      String[] headers = header.split("/");

      String method = headers[0].toUpperCase();
      char methodType = method.charAt(0);

      String fileName = headers[1];
      long fileSize = dataIn.readLong();

      if (methodType == 'U'){
        processUpload(method, dataIn, cd, fileName, fileSize);
      }
      else if (methodType == 'D'){
        processDownload(method, dataOut, cd, fileName);
      }
    }
    catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }

  public void processUpload(String method, DataInputStream dataIn, String cd, String fileName,
                            long fileSize){

    String filePath = "";

    if (method.equals("UPLOAD_BUNDLE")){
      filePath = cd + "\\bundle_files\\" + fileName;
    }
    else if (method.equals("UPLOAD_PREVIEW")){
      filePath = cd + "\\preview_files\\" + fileName;
    }

    try{
      BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(filePath));

      int n;
      byte[] buf = new byte[4092];

      while (fileSize > 0 && (n = dataIn.read(buf, 0, (int)Math.min(buf.length, fileSize))) != -1){
        fileOut.write(buf, 0, n);
        fileSize -= 1;
      }

      fileOut.close();
      sck.close();
    }
    catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }

  public void processDownload(String method, DataOutputStream dataOut, String cd, String fileName){
    String filePath = "";

    if (method.equals("DOWNLOAD_BUNDLE")){
      filePath = cd + "\\bundle_files\\" + fileName;
    }
    else if (method.equals("DOWNLOAD_PREVIEW")){
      filePath = cd + "\\preview_files\\" + fileName;
    }

    try{
      File file = new File(filePath);
      byte[] bytes = new byte[(int) file.length()];

      DataInputStream fileIn = new DataInputStream(new FileInputStream(file));
      fileIn.readFully(bytes, 0, bytes.length);

      dataOut.write(bytes, 0, bytes.length);
      dataOut.flush();

      fileIn.close();
      sck.close();
    }
    catch (Exception exn) {
      System.out.println(exn); System.exit(1);
    }
  }
}
