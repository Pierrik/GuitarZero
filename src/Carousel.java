import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Carousel extends JFrame implements KeyListener {

    //Image Icons
    private ArrayList<JLabel> menuOptions = new ArrayList<>();


    static Rectangle [] bounds = {
            new Rectangle(30, 50, 140, 140),
            new Rectangle(185, 50, 140, 140),
            new Rectangle(340, 50, 140, 140),
            new Rectangle(495, 50, 140, 140),
            new Rectangle(650, 50, 140, 140)
    };


    public Carousel(String title, ArrayList<JLabel> allOptions) {
        //Set up frame
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 280);
        setLayout(null);
        setContentPane(new JLabel(new ImageIcon("../assets/carousel.PNG")));


        for (JLabel label : allOptions) {
            menuOptions.add(label);
        }

        for (int i = 0; i < 5; i++) {
            menuOptions.get(i).setBounds(bounds[i]);
            add(menuOptions.get(i));
        }


        addKeyListener(this);
        setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT) {

            for (JLabel label : menuOptions) {

                if (label.getX() == bounds[0].x) {
                    label.setBounds(bounds[1]);

                } else if (label.getX() == bounds[1].x) {
                    label.setBounds(bounds[2]);

                } else if (label.getX() == bounds[2].x) {
                    label.setBounds(bounds[3]);

                } else if (label.getX() == bounds[3].x) {
                    label.setBounds(bounds[4]);

                } else if (label.getX() == bounds[4].x) {
                    label.setBounds(bounds[0]);

                }

            }
       } else if (key == KeyEvent.VK_LEFT) {

            for (JLabel label : menuOptions) {

                if (label.getX() == bounds[0].x) {
                    label.setBounds(bounds[4]);

                } else if (label.getX() == bounds[1].x) {
                    label.setBounds(bounds[0]);

                } else if (label.getX() == bounds[2].x) {
                    label.setBounds(bounds[1]);

                } else if (label.getX() == bounds[3].x) {
                    label.setBounds(bounds[2]);

                } else if (label.getX() == bounds[4].x) {
                    label.setBounds(bounds[3]);

                }

            }
        } else if (key == KeyEvent.VK_ENTER) {

        for (JLabel label : menuOptions) {

            if (label.getX() == bounds[3].x) {
                System.out.println("You have chosen : ...");
            }

        }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String args[]) {

        ArrayList<ImageIcon> icons = new ArrayList<>();
        icons.add(new ImageIcon("../assets/TutorialLogo2.png"));
        icons.add(new ImageIcon("../assets/PlayLogo2.png"));
        icons.add(new ImageIcon("../assets/SelectLogo2.png"));
        icons.add(new ImageIcon("../assets/ExitLogo2.png"));
        icons.add(new ImageIcon("../assets/StoreLogo2.png"));



         ArrayList<JLabel> allOptions = new ArrayList<>();

        for (ImageIcon image: icons) {
            JLabel label = new JLabel(image);
            allOptions.add(label);
        }


        Carousel carousel = new Carousel("SlashMode", allOptions);
    }

}
