import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * CarouselView.
 *
 * @author  John Mercer
 * @version 1.00, February 2019.
 */

public class MockZip {
    public static void main(String[] args) throws IOException {
    List<String> srcFiles =
        Arrays.asList(
            "C:\\Users\\John\\Desktop\\GuitarZero\\src\\test1.txt",
            "C:\\Users\\John\\Desktop\\GuitarZero\\src\\test2.txt");          // files to be zipped go here

        FileOutputStream fos = new FileOutputStream("multiCompressed.zip");   // title goes here

        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
    }
}
