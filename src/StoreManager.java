import java.awt.Color;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


public class StoreManager extends JFrame {
    final TextField      testerTextField   = new TextField();


    public StoreManager () {
        setTitle( "Store Manager" );
        setContentPane( new JLabel( new ImageIcon( "background.png" ) ) );
        setLayout( null );

    }

    public static void main( String[] argv ) {
        JFrame frame = new StoreManager();
        frame.setLocationRelativeTo( null );
        frame.setSize( 353, 353 ); /* title bar! */
        frame.setResizable( false );
        frame.setVisible( true );
    }
}
