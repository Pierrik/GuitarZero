import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;

public class Note extends Highway {
  Image blackNote = new ImageIcon("../assets/BlackNote.png").getImage();
  Image whiteNote = new ImageIcon("../assets/WhiteNote.png").getImage();
  Image sprite;
  int x;
  int y = 0;
  int xInc;
  int yInc;
  public Note(boolean white, int lane){
    if(white){ this.sprite = whiteNote; }
    else{ this.sprite = blackNote; }
    switch(lane){
      case 1:
      x = (bg[0].getWidth(null)/4);
      xInc = 0;
      yInc = 2;
      break;
      case 2:
        x = (bg[0].getWidth(null)/2) - blackNote.getWidth(null)/2;
        xInc = 0;
        yInc = 2;
        break;
      case 3: x = 250;
      break;
    }
  }
  public void paintComponent(Graphics g){
    g.drawImage(sprite,x, y, null);
    y += yInc;
    x += xInc;
    if(y>bg[0].getHeight(null)){y=0;}
  }
}
