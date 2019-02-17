import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.Dimension;
import java.lang.Thread;

public class Main extends JPanel {
  static BufferedImage bg;
  static Note n1;

  public static void main(String[] args) {
    try{
      bg = ImageIO.read(new File("../assets/NoteHighway.bmp"));
    }
    catch(Exception e){
      e.printStackTrace();
    }
    n1 = new Note(false,2);
    JFrame window = new JFrame("Guitar Zero");
    window.setPreferredSize(new Dimension(bg.getWidth(null), bg.getHeight(null)));
    window.setContentPane(new GamePanel());
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
  }

  static class GamePanel extends JPanel implements ActionListener, Runnable {

    private Thread thread;

    Timer timer=new Timer(14, this);

    public GamePanel(){
      if(thread == null){
        thread = new Thread(this);
        thread.start();
      }
    }

    public void run(){
      timer.start();
    }

    public void actionPerformed(ActionEvent ev){
      if(ev.getSource()==timer){
        repaint();
      }
    }

    @Override
    public void paint(Graphics g) {
      g.drawImage(bg, 0, 0, null);
      n1.paintComponent(g);
    }
  }
}
