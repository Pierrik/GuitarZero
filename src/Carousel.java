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


    public Carousel(String title, ArrayList<ImageIcon> imageIcons) {
        //Set up frame
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 280);
        setLayout(null);
        setContentPane(new JLabel(new ImageIcon("/Users/pierrikmellab/Desktop/carousel.PNG")));

        /*

        //Create 5 JLabels, with first 5 image icons
        for (int i = 0; i < 5; i++) {
            JLabel label1  = new JLabel(imageIcons.get(i));
            label1.setBounds(bounds[i]);
            menuOptions.add(label1);
            add(menuOptions.get(i));
        }
        
        
        */


        for (ImageIcon image: imageIcons) {
            JLabel label = new JLabel(image);
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
            System.out.println("switch image");


            //JLabel lastItemHolder = menuOptions.get(4);
            //JLabel tempLabel;

            for (int i = 0; i < 3; i++) {
                //tempLabel = menuOptions.get(i+1);
                
                //menuOptions.get(i+1).setBounds(menuOptions.get(i).getBounds());
                
                //ImageIcon image = new ImageIcon();
                
                //image = menuOptions.get(i).get;
                        
                //menuOptions.set(i+1, menuOptions.get(i));
            }

            //menuOptions.set(1, lastItemHolder);



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
        icons.add(new ImageIcon("/Users/pierrikmellab/Desktop/TutorialLogo2.png"));
        icons.add(new ImageIcon("/Users/pierrikmellab/Desktop/PlayLogo2.png"));
        icons.add(new ImageIcon("/Users/pierrikmellab/Desktop/SelectLogo2.png"));
        icons.add(new ImageIcon("/Users/pierrikmellab/Desktop/ExitLogo2.png"));
        icons.add(new ImageIcon("/Users/pierrikmellab/Desktop/StoreLogo2.png"));


        // METHOD #1

        //pass in array list of strings containing URL -- LATER

        //method to create an array list of image icons, one for each string in original the array list -- LATER

        //create an array list of labels for the image icons, setting the bounds for the first 5

        //when key pressed, change the bounds of the labels, setting some to invisible





        // METHOD #2 --- TRIED WILL NOT WORK SINCE I CAN"T GET THE IMAGE ICON FROM A LABEL

        // pass in array list of strings containing URL -- LEAVE TILL END

        // method to create an array list of image icons, one for each string in original the array list -- LEAVE TILL END

        // create 5 labels, with set bounds, setting them equal to the first 5 image icons -- DONE

        // add the 5 labels to the JFrame -- DONE

        // when key is pressed, set new image icons for the 5 labels, rotating through the array list of image icons


        Carousel carousel = new Carousel("SlashMode", icons);
    }

}
