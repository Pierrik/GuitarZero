import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.lang.Thread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Note extends Highway {
  Image blackNote = new ImageIcon("../assets/BlackNote.png").getImage();
  Image whiteNote = new ImageIcon("../assets/WhiteNote.png").getImage();
  Image sprite;
  int x;
  int y = 0;
  public Note(boolean white, int lane){
    if(white){ this.sprite = whiteNote; }
    else{ this.sprite = blackNote; }
    switch(lane){
      case 1: x = 0;
      break;
      case 2: x = (bg.getWidth(null)/2) - blackNote.getWidth(null)/2;
      break;
      case 3: x = 250;
      break;
    }
  }
  public void paintComponent(Graphics g){
    g.drawImage(sprite,x, y, null);
    y ++;
    if(y>bg.getHeight(null)){y=0;}
  }
}
