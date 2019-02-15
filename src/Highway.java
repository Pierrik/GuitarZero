import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Highway extends Canvas {

    Image bg = new ImageIcon("../assets/NoteHighway.png").getImage();
    public static void main(String[] args) {
        JFrame frame = new JFrame("mainWindow");
        Canvas canvas = new Highway();
        canvas.setSize(506, 429);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        g.drawImage(bg, 0, 0, null);
    }
}
