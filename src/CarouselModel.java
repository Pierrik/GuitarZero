import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * CarouselModel
 *
 * @author  Pierrik Mellab
 * @version 1.00, February 2019.
 */

public class CarouselModel {


    /**
     * Skeleton constructor for later use
     */
    public CarouselModel( ) {

    }

    /**
     * Calls the rightMovement method in the carouselView file causing icons to shift right
     */
    public void right() {

        CarouselView.rightMovement();
    }

    /**
     * Calls the leftMovement method in the carouselView file causing icons to shift left
     */
    public void left() {

        CarouselView.leftMovement();
    }


    /**
     * Returns a string from the chosenOption method in the carouselView file allowing a user to
     * choose a menu option
     */
    public String select() {

        return CarouselView.chosenOption();
    }


}
