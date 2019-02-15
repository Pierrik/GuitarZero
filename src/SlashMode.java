import javax.swing.*;
import java.awt.*;
import static javax.swing.text.StyleConstants.setBackground;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SlashMode {

    public static void main(String args[]) {

        JFrame frame = new JFrame("Store Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 280);

        //frame.setLayout(null);
        //frame.setContentPane(new JLabel(new ImageIcon("/Users/pierrikmellab/Desktop/carousel.PNG")));

        JLabel sampleLable = new JLabel("tester");

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(300, 310));

        //frame.add(layeredPane);

        sampleLable.setOpaque(true);
        sampleLable.setBackground(Color.BLUE);
        //sampleLable.setPreferredSize(new Dimension(100, 100));
        sampleLable.setBounds(10, 10, 100, 100);

        //sampleLable.setVerticalAlignment(JLabel.TOP);
        //sampleLable.setHorizontalAlignment(JLabel.CENTER);


        /**
        sampleLable.setBackground(Color.blue);
        sampleLable.setForeground(Color.black);
        sampleLable.setBorder(BorderFactory.createLineBorder(Color.black));
        sampleLable.setBounds(0, 0, 140, 140);
        **/

        frame.add(sampleLable);

        frame.setVisible(true);

    }

}
