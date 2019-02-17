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

public class Highway extends JPanel {
  static int backgroundFrameCount = 4;
  static int backgroundFrameDelay = 10;
  static BufferedImage[] bg = new BufferedImage[backgroundFrameCount];
  static Note[] notes = new Note[2];
  static int frame = 0;

  public static void main(String[] args) {
    try{
      for(int i = 0; i<backgroundFrameCount; i++){
        bg[i] = ImageIO.read(new File("../assets/bg"+i+".bmp"));
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    notes[0] = new Note(false,1);
    notes[1] = new Note(false,2);
    JFrame window = new JFrame("Guitar Zero");
    window.setPreferredSize(new Dimension(bg[0].getWidth(null), bg[0].getHeight(null)));
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
      frame++;
      g.drawImage(bg[((frame/backgroundFrameDelay)%backgroundFrameCount)], 0, 0, null);
      for(Note n : notes){
        n.paintComponent(g);
      }
    }
  }
}
