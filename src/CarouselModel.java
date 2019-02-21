import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * CarouselView.
 *
 * @author  Pierrik Mellab
 * @version 1.00, February 2019.
 */

public class CarouselModel {


    public CarouselModel( ) {


    }

    public void right() {

        CarouselView.rightMovement();
    }

    public void left() {

        CarouselView.leftMovement();
    }

    public String select() {

        return CarouselView.chosenOption();
    }


}
